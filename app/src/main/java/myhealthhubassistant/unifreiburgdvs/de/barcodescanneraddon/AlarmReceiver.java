package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;


/*
 * Receives the alarm from the MainActivity and sets up a notification. passes it on to the second
 * alarm receiver.
 */
public class AlarmReceiver extends BroadcastReceiver {
    SharedPreferences preferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        Calendar cal = Calendar.getInstance();
        if (preferences.getLong("STOPTIME", 0) > cal.getTimeInMillis()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentNew = new Intent(context, AlarmReceiver2.class);
            int time = intent.getIntExtra("time", -1);
            int hour = 0;
            int minute = 0;
            Random r = new Random();
            switch (time) {
                case 0:
                    hour = 9;
                    minute = r.nextInt(60);
                    break;
                case 1:
                    hour = r.nextInt(2) + 10;
                    if (hour == 10) {
                        minute = r.nextInt(30) + 30;
                    } else if (hour == 11) {
                        minute = r.nextInt(31);
                    }
                    break;
                case 2:
                    hour = 12;
                    minute = r.nextInt(60);
                    break;
                case 3:
                    hour = r.nextInt(2) + 13;
                    if (hour == 13) {
                        minute = r.nextInt(30) + 30;
                    } else if (hour == 14) {
                        minute = r.nextInt(31);
                    }
                    break;
                case 4:
                    hour = 15;
                    minute = r.nextInt(60);
                    break;
                case 5:
                    hour = r.nextInt(2) + 16;
                    if (hour == 16) {
                        minute = r.nextInt(30) + 30;
                    } else if (hour == 17) {
                        minute = r.nextInt(31);
                    }
                    break;
                case 6:
                    hour = 18;
                    minute = r.nextInt(60);
            }
            intentNew.putExtra("time", time);
            intentNew.setAction(String.valueOf(time));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, time, intentNew, PendingIntent.FLAG_UPDATE_CURRENT);
            String usage = intent.getStringExtra("usage");
            cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            if (usage.equals("create")) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), 600000, pendingIntent);
            } else if (usage.equals("delete")) {
                AlarmReceiver2.counter = 3;
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }
}
