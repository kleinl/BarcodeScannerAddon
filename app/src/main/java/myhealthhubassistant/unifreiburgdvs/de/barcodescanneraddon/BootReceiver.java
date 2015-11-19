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

    private SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = context.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        String startDate = prefs.getString("STARTTIME", "");
        if (!startDate.equals(Calendar.DATE)) {
            MainActivity.alarmForSurvey(context, false);
        }
    }
}