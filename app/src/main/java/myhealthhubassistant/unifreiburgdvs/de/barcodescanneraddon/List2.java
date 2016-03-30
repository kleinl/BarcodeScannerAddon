package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lukas on 10.11.15.
 */
public class List2 extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] values = getResources().getStringArray(R.array.list2);
        final String[] examples = getResources().getStringArray(R.array.examples2);
        final java.util.List<String> list = new ArrayList<>(Arrays.asList(values));
        ArrayAdapter adapter = new ArrayAdapter (this, android.R.layout.simple_list_item_2, android.R.id.text1, list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(values[position]);
                text1.setTypeface(null, Typeface.BOLD);
                text2.setText(examples[position]);
                return view;
            }
        };
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        String selectedItem = (String) getListView().getItemAtPosition(position);
        Intent sendIntent = new Intent(List2.this, Item.class);
        sendIntent.putExtra("name", selectedItem);
        startActivity(sendIntent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent sendIntent = new Intent(List2.this, List.class);
        startActivity(sendIntent);
        finish();
    }
}

