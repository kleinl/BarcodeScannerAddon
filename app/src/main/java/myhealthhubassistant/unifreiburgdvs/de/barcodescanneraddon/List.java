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
public class List extends ListActivity {
    String[] values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        values = getResources().getStringArray(R.array.list1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        String selectedItem = (String) getListView().getItemAtPosition(position);
        if (selectedItem.equals(getString(R.string.more_drinks))) {
            Intent sendIntent = new Intent(List.this, List2.class);
            startActivity(sendIntent);
        } else {
            Intent sendIntent = new Intent(List.this, Item.class);
            sendIntent.putExtra("name", selectedItem);
            startActivity(sendIntent);
            finish();
        }
    }
}