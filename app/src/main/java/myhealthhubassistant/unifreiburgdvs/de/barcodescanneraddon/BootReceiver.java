package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Gets called when the system reboots to reset the alarmSetter variable.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR, 0);
        Long startDate = prefs.getLong("STARTTIME", 0);
        if (startDate != 0) {
            if (startDate <= cal.getTimeInMillis()) {
                MainActivity.alarmForSurvey(context, false);
            } else {
                MainActivity.alarmForSurvey(context, true);
            }
        }

    }
}