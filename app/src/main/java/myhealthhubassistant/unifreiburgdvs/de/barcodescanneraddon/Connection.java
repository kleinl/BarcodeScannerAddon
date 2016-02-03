package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lukas on 10.11.15.
 */
public class Connection extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("json"));
            MainActivity.commUnit.storeEntry(obj);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
        int survey = getIntent().getIntExtra("survey", -1);
        if ((survey == 5 || survey == -1) && MainActivity.self != null) {
            MainActivity.self.finish();
        }
        super.finish();
    }
}
