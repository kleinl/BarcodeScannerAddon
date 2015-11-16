package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Gets called when the system reboots to reset the alarmSetter variable.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.alarmForSurvey(context, false);
    }
}