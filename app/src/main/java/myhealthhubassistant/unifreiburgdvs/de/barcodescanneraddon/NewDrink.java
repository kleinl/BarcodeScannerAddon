package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by lukas on 09.11.15.
 */
public class NewDrink extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_drink);

        Button newItemByBarcode = (Button) findViewById(R.id.NewItemByBarcode);
        Button newItemByList = (Button) findViewById(R.id.NewItemByList);

        newItemByBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });
    }

    private void startScanning() {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // result of scan bar code
            if (resultCode == Activity.RESULT_OK) {
                String barcode = intent.getStringExtra("SCAN_RESULT");
                Intent sendIntent = new Intent(NewDrink.this, Item.class);
                sendIntent.putExtra("barcode", barcode);
                startActivity(sendIntent);
                finish();
            }
        }
    }
}
