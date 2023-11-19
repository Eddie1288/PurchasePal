package com.example.purchasepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStoreActivity extends AppCompatActivity {
    ArrayList<String> addedItems = new ArrayList<String>();
    Button addButton;
    Button confirmButton;
    EditText itemQueued;
    EditText storeName;

    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        account = CurrentAccount.getAccount();

        ListView listView = findViewById(R.id.addItemsDisplay);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.item_list_view, addedItems);

        listView.setAdapter(adapter);

        itemQueued = findViewById(R.id.itemQueued);
        addButton = findViewById(R.id.addItemBtn);
        confirmButton = findViewById(R.id.confirmAddButton);
        storeName = findViewById(R.id.storeName);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = itemQueued.getText().toString();
                addedItems.add(item);

                adapter.notifyDataSetChanged();

                itemQueued.setText("");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStore();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                addedItems.remove(pos);
                adapter.notifyDataSetChanged();
                Toast.makeText(AddStoreActivity.this, "Removed item", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

private void addStore() {

        String json = "{\n" +
                "        \"username\": " + " \"philiponions\" " + ",\n" +
                "        \"name\": " + "\"" + storeName.getText().toString() + "\"" + ",\n" +
                "       \"items\": [ ";

    int itemCount = addedItems.size();
    for (int i = 0; i < itemCount; i++) {
        String item = addedItems.get(i);
        json += "{\"item_name\": " + "\"" + item + "\"}";

        // Add a comma if it's not the last item
        if (i < itemCount - 1) {
            json += ",\n";
        }
    }
        json += "]}";

//
//
//                "}";

    Log.d("yeet", "addStore: " + json);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        String url = "http://192.168.1.72:5000/store";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(AddStoreActivity.this, "server down", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                startActivity(new Intent(AddStoreActivity.this, MainMenuActivity.class));
            }
        });


    }
}
