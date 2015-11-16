package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.BarcodeItem;
import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.BarcodeItemToJson;

/**
 * Created by lukas on 09.11.15.
 */
public class Item extends AppCompatActivity {
    private String barcode;
    private ProgressBar bar;
    private EditText nameEdit;
    private EditText editTextAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        nameEdit = (EditText) findViewById(R.id.name);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        editTextAmount = (EditText) findViewById(R.id.edit_amount);
        editTextAmount.setText("250");

        Button plus = (Button) findViewById(R.id.plus_btn);
        plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ml = Integer.parseInt(editTextAmount.getText().toString());
                ml += 250;
                editTextAmount.setText(String.valueOf(ml));
            }
        });

        Button minus = (Button) findViewById(R.id.minus_btn);
        minus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ml = Integer.parseInt(editTextAmount.getText().toString());
                ml -= 250;
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
        if (name == null) {
            barcode = intent.getStringExtra("barcode");
            if (isNetworkAvailable()) {
                new MyTask().execute();
            }
        } else {
            nameEdit.setText(name);
            barcode = "-";
        }
    }

    private void save() {
        String id = MainActivity.prefs.getString("ID", "");
        String name = nameEdit.getText().toString();
        String ssb;
        if (barcode.equals("-")) {
            if (name.equals(getResources().getString(R.string.ssb1)) ||
                    name.equals(getResources().getString(R.string.ssb2)) ||
                    name.equals(getResources().getString(R.string.ssb3)) ||
                    name.equals(getResources().getString(R.string.ssb4))) {
                ssb = "1";
            } else {
                ssb = "0";
            }
        } else {
            ssb = "-";
        }
        String date;
        String time;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        date = sdf.format(c.getTime());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        time = sdf2.format(c.getTime());
        BarcodeItem barcodeItem = new BarcodeItem("item", id, date, time, name,
                Integer.valueOf(editTextAmount.getText().toString()), barcode, ssb);
        JSONObject key;
        try {
            key = BarcodeItemToJson.getJSONfromBarcode(barcodeItem);
            Intent intent = new Intent(Item.this, Connection.class);
            intent.putExtra("json", key.toString());
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class MyTask extends AsyncTask<String, String, String> {
        public MyTask() {
        }

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            URL url;
            String[] data = new String[19];
            for (int i = 0; i < 19; i++) {
                data[i] = "";

            }
            int errorLine = 0;
            InputStream inStream = null;
            StringBuilder sb = new StringBuilder();
            sb.append("http://opengtindb.org/?ean=");
            sb.append(barcode);
            sb.append("&cmd=query&queryid=477909028");
            try {
                url = new URL(sb.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp;
                int i = 0;
                while ((temp = bReader.readLine()) != null) {
                    if (temp.contains("error")) {
                        errorLine = i;
                    }
                    data[i] = temp;
                    i++;
                }
                if (errorLine == 0) {
                    return "";
                }
            } catch (Exception e) {

            } finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            if (!data[errorLine].split("=")[1].equals("0")) {
                return "";
            }
            String name = data[errorLine + 4].split("=")[1];
            if (name.equals("")) {
                return data[errorLine + 5].split("=")[1];
            }
            return name;
        }

        @Override
        protected void onPostExecute(String result) {
            bar.setVisibility(View.GONE);
            if (!result.equals("")) {
                nameEdit.setText(result);
            }
        }
    }
}
