package com.example.purchasepal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ItemListActivity extends AppCompatActivity {

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);



        Intent intent = getIntent();
        ArrayList<String> receivedList = intent.getStringArrayListExtra("itemList");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.item_list_view, receivedList);

        ListView listView = (ListView) findViewById(R.id.itemsListDisplay);
        listView.setAdapter(adapter);

    }
}