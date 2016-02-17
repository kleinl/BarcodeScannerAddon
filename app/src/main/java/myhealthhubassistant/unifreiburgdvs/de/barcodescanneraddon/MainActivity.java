package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Intent myHealthHubIntent;
    private boolean isConnectedToMhh;
    private SharedPreferences prefs;
    public static CommBroadcastReceiver commUnit;
    public static Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("ID", "");
        // Check if survey has started yet.
        self = this;
        commUnit = new CommBroadcastReceiver(this);
        connectToMhh();
        int survey = getIntent().getIntExtra("survey", -1);
        if ((survey) != -1) {
            int time = getIntent().getIntExtra("time", -1);
            Intent openSurvey = new Intent(this, Survey.class);
            openSurvey.putExtra("time", time);
            openSurvey.putExtra("survey", survey);
            openSurvey.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openSurvey);
        } else if (userId.isEmpty()) {
            setContentView(R.layout.activity_main);
            final Button startSurvey = (Button) findViewById(R.id.start_survey);
            startSurvey.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startSurvey();
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, List.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        self = null;
        disconnectMHH();
    }
    private void startSurvey() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

        alert.setTitle("Settings");
        alert.setMessage("Please enter id.");

        alert2.setTitle("Settings");
        alert2.setMessage("Select Survey Runtime.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        // Set an spinner view to get user input
        final NumberPicker input2 = new NumberPicker(this);
        input2.setMaxValue(365);
        input2.setMinValue(1);
        alert2.setView(input2);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                prefs.edit().putString("ID", input.getText().toString()).apply();
                alert2.show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();

        alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                alarmForSurvey(MainActivity.this, true);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
                cal.set(Calendar.HOUR, 0);
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.HOUR, 0);
                long time  = cal.getTimeInMillis();
                time = time + TimeUnit.DAYS.toMillis(input2.getValue());
                prefs.edit().putLong("STOPTIME", time).apply();
                prefs.edit().putLong("STARTTIME", cal2.getTimeInMillis()).apply();
                prefs.edit().putInt("SURVEY_DAY", 1).apply();
                prefs.edit().putInt("SURVEY_SIGNAL", 1).apply();
                finish();
            }
        });
        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                prefs.edit().putString("ID", "").apply();
            }
        });
    }

    public static void cancelAlarm(Context c) {
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar[] calendars = new Calendar[7];
        Intent[] intents = new Intent[7];
        for (int i = 0; i < 7; i++) {
            calendars[i] = Calendar.getInstance();
            calendars[i].setTimeInMillis(System.currentTimeMillis());
            switch(i) {
                case 0: calendars[i].set(Calendar.HOUR_OF_DAY, 9);
                    calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 1: calendars[i].set(Calendar.HOUR_OF_DAY, 10);
                    calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 2: calendars[i].set(Calendar.HOUR_OF_DAY, 12);
                    calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 3: calendars[i].set(Calendar.HOUR_OF_DAY, 13);
                    calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 4: calendars[i].set(Calendar.HOUR_OF_DAY, 15);
                    calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 5: calendars[i].set(Calendar.HOUR_OF_DAY, 16);
                    calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 6: calendars[i].set(Calendar.HOUR_OF_DAY, 18);
                    calendars[i].set(Calendar.MINUTE, 0);
                    break;
            }
            intents[i] = new Intent(c.getApplicationContext(), AlarmReceiver.class);
            intents[i].putExtra("time", i);
            intents[i].putExtra("usage", "create");
            intents[i].setAction("actionstring" + System.currentTimeMillis() * i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(c.getApplicationContext(), i, intents[i], PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    // Notification for survey
    public static void alarmForSurvey(Context c, boolean first) {
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar[] calendars = new Calendar[7];
        Intent[] intents = new Intent[7];
        for (int i = 0; i < 7; i++) {
            calendars[i] = Calendar.getInstance();
            calendars[i].setTimeInMillis(System.currentTimeMillis());
            switch(i) {
                case 0: calendars[i].set(Calendar.HOUR_OF_DAY, 9);
                        calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 1: calendars[i].set(Calendar.HOUR_OF_DAY, 10);
                        calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 2: calendars[i].set(Calendar.HOUR_OF_DAY, 12);
                        calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 3: calendars[i].set(Calendar.HOUR_OF_DAY, 13);
                        calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 4: calendars[i].set(Calendar.HOUR_OF_DAY, 15);
                        calendars[i].set(Calendar.MINUTE, 0);
                    break;
                case 5: calendars[i].set(Calendar.HOUR_OF_DAY, 16);
                        calendars[i].set(Calendar.MINUTE, 30);
                    break;
                case 6: calendars[i].set(Calendar.HOUR_OF_DAY, 18);
                        calendars[i].set(Calendar.MINUTE, 0);
                    break;
            }
            if (first) {
                calendars[i].add(Calendar.DATE, 1);
            }
            intents[i] = new Intent(c.getApplicationContext(), AlarmReceiver.class);
            intents[i].putExtra("time", i);
            intents[i].putExtra("usage", "create");
            intents[i].setAction("actionstring" + System.currentTimeMillis() * i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(c.getApplicationContext(), i, intents[i], PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
    /**
     * Service connection to myHealthHub remote service. This connection is
     * needed in order to start myHealthHub. Furthermore, it is used inform the
     * application about the connection status.
     */
    private final ServiceConnection myHealthAssistantRemoteConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(getApplicationContext(),
                    "Connected to myHealthAssistant", Toast.LENGTH_SHORT)
                    .show();
            isConnectedToMhh = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(),
                    "disconnected with myHealthAssistant", Toast.LENGTH_SHORT)
                    .show();
            isConnectedToMhh = false;
        }
    };

    /** setting up the connection with myHealthHub */
    private void connectToMhh() {
        if (!isConnectedToMhh) {
            myHealthHubIntent = new Intent("de.tudarmstadt.dvs.myhealthassistant.myhealthhub.IMyHealthHubRemoteService");
            myHealthHubIntent.setPackage("de.tudarmstadt.dvs.myhealthassistant.myhealthhub");
            this.getApplicationContext()
                    .bindService(myHealthHubIntent,
                            myHealthAssistantRemoteConnection,
                            Context.BIND_AUTO_CREATE);
        }
    }

    private void disconnectMHH() {
        if (isConnectedToMhh) {
            this.getApplicationContext().unbindService(
                    myHealthAssistantRemoteConnection);
            isConnectedToMhh = false;
        }
        if (myHealthHubIntent != null)
            this.getApplicationContext().stopService(myHealthHubIntent);
    }
}
