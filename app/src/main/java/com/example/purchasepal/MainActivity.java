package com.example.purchasepal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    // declare attribute for textview
    private TextView pagenameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        MaterialButton login_btn = (MaterialButton) findViewById(R.id.login_btn);

        OkHttpClient okhttpclient = new OkHttpClient();
        Request request = new Request.Builder().url("http://172.31.124.58:5000/").build();


        try {
            // making call asynchronously
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                // called if server is unreachable
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "server down", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                // called if we get a
                // response from the server
                public void onResponse(
                        @NotNull Call call,
                        @NotNull Response response)
                        throws IOException {
                    Log.d("Yes", response.body().string());
                }
            });
        }
        catch(Exception e){
            Log.d("Hello", e.toString());
        }

        //admin and admin

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    //correct
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
                } else
                    //incorrect
                    Toast.makeText(MainActivity.this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton signup_btn = (MaterialButton) findViewById(R.id.sign_up);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }
}