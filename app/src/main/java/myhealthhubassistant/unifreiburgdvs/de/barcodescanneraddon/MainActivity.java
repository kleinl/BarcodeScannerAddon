package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("ID", "");
        // Check if survey has started yet.
        if (userId.isEmpty()) {
            setContentView(R.layout.activity_main);

            final Button startSurvey = (Button) findViewById(R.id.start_survey);
            startSurvey.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startSurvey();
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, NewDrink.class);
            startActivity(intent);
            finish();
        }
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
                prefs.edit().putInt("RUNTIME", input2.getValue()).apply();
                prefs.edit().putLong("STARTTIME", System.currentTimeMillis()).apply();
                Intent intent = new Intent(MainActivity.this, NewDrink.class);
                startActivity(intent);
                finish();
            }
        });
        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                prefs.edit().putString("ID", "").apply();
            }
        });
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
                case 0: calendars[i].set(Calendar.HOUR_OF_DAY, 8);
                    break;
                case 1: calendars[i].set(Calendar.HOUR_OF_DAY, 10);
                    break;
                case 2: calendars[i].set(Calendar.HOUR_OF_DAY, 12);
                    break;
                case 3: calendars[i].set(Calendar.HOUR_OF_DAY, 14);
                    break;
                case 4: calendars[i].set(Calendar.HOUR_OF_DAY, 16);
                    break;
                case 5: calendars[i].set(Calendar.HOUR_OF_DAY, 18);
                    break;
                case 6: calendars[i].set(Calendar.HOUR_OF_DAY, 20);
                    break;
            }
            if (first) {
                calendars[i].add(Calendar.DATE, 1);
            }
            intents[i] = new Intent(c.getApplicationContext(), AlarmReceiver.class);
            intents[i].putExtra("time", i);
            intents[i].putExtra("usage", "create");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(c.getApplicationContext(), i, intents[i], PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
