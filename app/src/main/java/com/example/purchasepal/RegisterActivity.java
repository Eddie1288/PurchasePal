package com.example.purchasepal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        MaterialButton continue_btn = (MaterialButton) findViewById(R.id.continue_btn);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText usernameEditText = findViewById(R.id.username);
                String username = usernameEditText.getText().toString();

                EditText passwordEditText = findViewById(R.id.password);
                String password = passwordEditText.getText().toString();

                // Create a JSON object with the username and password
                String jsonPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
                Log.d("Username", jsonPayload);
                // Create a request body with the data to be sent
                RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json"), jsonPayload);

                Request request = new Request.Builder()
                        .url("http://172.31.124.58:5000/user")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .build();


                try {
                    // making call asynchronously
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        // called if server is unreachable
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "server down", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }
}
