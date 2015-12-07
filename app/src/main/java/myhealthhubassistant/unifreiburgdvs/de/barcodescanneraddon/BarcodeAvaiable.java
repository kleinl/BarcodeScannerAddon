package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.BarcodeItem;
import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.BarcodeItemToJson;

/**
 * Created by klein on 19.11.2015.
 */
public class BarcodeAvaiable extends AppCompatActivity {
    private String id;
    private String name;
    private String date;
    private String time;
    private String amount;
    private String ssb;
    private String lng;
    private String lat;
    private String barcode;
    private String barcodeName;
    private ProgressBar bar;
    private TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");
        amount = intent.getStringExtra("AMOUNT");
        ssb = intent.getStringExtra("SSB");
        lng = intent.getStringExtra("LNG");
        lat = intent.getStringExtra("LAT");
        barcode = "-";
        barcodeName = "-";
        setContentView(R.layout.barcode_avaiable);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textViewLocation);
        bar.setVisibility(View.INVISIBLE);
        Button save = (Button) findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.barcode_avaiable));
        alert.setPositiveButton(getString(R.string.barcode_avaiable_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startScanning();
            }
        });
        alert.setNegativeButton(getString(R.string.barcode_avaiable_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
        SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(),
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        lat = String.valueOf(location.latitude);
                        lng = String.valueOf(location.longitude);
                    }
                });
        MyTask task = new MyTask();
        task.execute();
    }

    private void startScanning () {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // result of scan bar code
            if (resultCode == Activity.RESULT_OK) {
                barcode = intent.getStringExtra("SCAN_RESULT");
            }
        }
    }

    private void save() {
        BarcodeItem barcodeItem = new BarcodeItem("item", id, date, time, name,
                amount, barcode, barcodeName, ssb, lng, lat);
        JSONObject key;
        try {
            key = BarcodeItemToJson.getJSONfromBarcode(barcodeItem);
            Intent intent = new Intent(BarcodeAvaiable.this, Connection.class);
            intent.putExtra("json", key.toString());
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class MyTask extends AsyncTask<String, String, String> {
        public MyTask() {
        }

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            while (lat.equals("-")) {

            }


  /*          HttpURLConnection urlConnection = null;
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
            */
            return "A";
        }

        @Override
        protected void onPostExecute(String result) {
            /*
            editName.setText(result);
            */
            bar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
    }
}
