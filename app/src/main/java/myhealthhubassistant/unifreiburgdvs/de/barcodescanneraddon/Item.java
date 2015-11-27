package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private TextView nameEdit;
    private EditText editTextAmount;
    private SharedPreferences prefs;
    private String ssb;
    private String amount = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        nameEdit = (TextView) findViewById(R.id.name);
        editTextAmount = (EditText) findViewById(R.id.edit_amount);
        editTextAmount.setText("0.5");
        prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);
        Button plus = (Button) findViewById(R.id.plus_btn);
        plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                float ml = Float.parseFloat(editTextAmount.getText().toString());
                if (ml < 5) {
                    ml += 0.5;
                }
                editTextAmount.setText(String.valueOf(ml));
            }
        });

        Button minus = (Button) findViewById(R.id.minus_btn);
        minus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                float ml = Float.parseFloat(editTextAmount.getText().toString());
                if (ml > 0) {
                    ml -= 0.5;
                }
                editTextAmount.setText(String.valueOf(ml));
            }
        });

        Button save = (Button) findViewById(R.id.scan_save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
            amount = "-";
            save();
        }
    }

    private void save() {
        String lng = "-";
        String lat = "-";
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
