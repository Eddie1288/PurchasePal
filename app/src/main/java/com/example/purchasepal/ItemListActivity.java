package com.example.purchasepal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class ItemListActivity extends AppCompatActivity {

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.item_list_view, mobileArray);

        ListView listView = (ListView) findViewById(R.id.itemsListDisplay);
        listView.setAdapter(adapter);

    }
}