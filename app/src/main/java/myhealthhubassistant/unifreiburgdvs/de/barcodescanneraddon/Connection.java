package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lukas on 10.11.15.
 */
public class Connection extends AppCompatActivity {

    public CommBroadcastReceiver commUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commUnit = new CommBroadcastReceiver(this.getApplicationContext());
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("json"));
            commUnit.storeEntry(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.finish();
    }
}
