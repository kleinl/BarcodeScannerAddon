package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by lukas on 09.11.15.
 */
public class Item extends AppCompatActivity {
    private EditText nameEdit;
    private EditText editTextAmount;
    private Location location;
    private SharedPreferences prefs;
    private String ssb;
    private String amount = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        nameEdit = (EditText) findViewById(R.id.name);
        TextView text = (TextView) findViewById(R.id.text_amount);
        editTextAmount = (EditText) findViewById(R.id.edit_amount);
        editTextAmount.setText("1");
        prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        Button plus = (Button) findViewById(R.id.plus_btn);
        plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ml = Integer.parseInt(editTextAmount.getText().toString());
                ml += 1;
                editTextAmount.setText(String.valueOf(ml));
            }
        });

        Button minus = (Button) findViewById(R.id.minus_btn);
        minus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ml = Integer.parseInt(editTextAmount.getText().toString());
                ml -= 1;
                editTextAmount.setText(String.valueOf(ml));
            }
        });

        Button save = (Button) findViewById(R.id.scan_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = MainActivity.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String title = nameEdit.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    nameEdit.setError(getResources().getString(R.string.forgot_name));
                    return;
                } else {
                    save();
                }
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        nameEdit.setText(name);
        if (name.equals(getResources().getString(R.string.ssb1)) ||
                name.equals(getResources().getString(R.string.ssb2)) ||
                name.equals(getResources().getString(R.string.ssb3)) ||
                name.equals(getResources().getString(R.string.ssb4))) {
            ssb = "1";
        } else {
            ssb = "0";
        }
        if (ssb.equals("0")) {
            editTextAmount.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.INVISIBLE);
            minus.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            amount = "-";
        }
    }

    private void save() {
        String lng = "-";
        String lat = "-";
        if (location != null) {
            lng = String.valueOf(location.getLongitude());
            lat = String.valueOf(location.getLatitude());
        }
        String date;
        String time;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        date = sdf.format(c.getTime());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        time = sdf2.format(c.getTime());
        if (!amount.equals("-")) {
            amount = editTextAmount.getText().toString();
        }
        Intent intent = new Intent(Item.this, BarcodeAvaiable.class);
        intent.putExtra("ID", prefs.getString("ID", "no id"));
        intent.putExtra("DATE", date);
        intent.putExtra("TIME", time);
        intent.putExtra("NAME", nameEdit.getText().toString());
        intent.putExtra("AMOUNT", amount);
        intent.putExtra("SSB", ssb);
        intent.putExtra("LNG", lng);
        intent.putExtra("LAT", lat);
        startActivity(intent);
        finish();
    }
}
