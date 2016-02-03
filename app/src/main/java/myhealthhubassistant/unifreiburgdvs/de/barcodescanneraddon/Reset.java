package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/**
 * Created by klein on 19.01.2016.
 */
public class Reset extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset);

        Button resetButton = (Button) findViewById(R.id.yes_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
    }

    private void clearData() {
        SharedPreferences prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
        MainActivity.cancelAlarm(this);
        Toast.makeText(this, "Reset successful.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
