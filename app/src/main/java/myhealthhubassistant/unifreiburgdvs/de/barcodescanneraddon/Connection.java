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
<<<<<<< HEAD

    public CommBroadcastReceiver commUnit;

=======
    private Intent myHealthHubIntent;
    private ProgressDialog progressDialog;
    private boolean isConnectedToMhh;
    public CommBroadcastReceiver commUnit;


>>>>>>> refs/remotes/origin/master
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commUnit = new CommBroadcastReceiver(this.getApplicationContext());
<<<<<<< HEAD
=======
        connectToMhh();
>>>>>>> refs/remotes/origin/master
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("json"));
            commUnit.storeEntry(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD
        super.finish();
    }
=======
        disconnectMHH();
        super.finish();
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

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
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
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Connect to myHealthHub");
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectMHH();
    }
>>>>>>> refs/remotes/origin/master
}
