package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by lukas on 10.11.15.
 */
public class List2 extends ListActivity {
    String[] values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        values = getResources().getStringArray(R.array.list2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
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
}

