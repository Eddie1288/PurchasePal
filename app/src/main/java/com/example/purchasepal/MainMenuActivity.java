package com.example.purchasepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainMenuActivity extends AppCompatActivity {
    private Button addButton;
    private String username = "Daniel Thai";
    private TextView pagenameTextView;
    String latitudeTextView, longitTextView;
    // initializing FusedLocationProviderClient object
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<String> usersStores = new ArrayList<String>();

    // Initializing other items
    // from layout file
    private int PERMISSION_ID = 44;

    ListView storeListView;

    ArrayList<String> storeArray = new ArrayList<String>();
    ArrayAdapter storeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usersStores.add("University of Alberta");
        usersStores.add("HUB Mall");

        getStores();

         storeArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.store_list_view, storeArray);

         addButton = findViewById(R.id.addStore);

        storeListView = (ListView) findViewById(R.id.storeListDisplay);
        storeListView.setAdapter(storeArrayAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, AddStoreActivity.class));
            }
        });

        storeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Perform actions when an item is long-clicked
                // For example, start a new activity to delete the store
                startActivity(new Intent(MainMenuActivity.this, DeleteStoreActivity.class));

                // Return true to indicate that the long click event has been handled
                return true;
            }
        });


        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainMenuActivity.this, ItemListActivity.class));
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView = location.getLatitude() + "";
                            longitTextView = location.getLongitude() + "";
                            getPlaces(latitudeTextView, longitTextView);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private void getStores() {

        try {
            String url = "http://192.168.1.72:5000/store/" + username;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    Log.d("yeet", "onFailure: " + "not working");
                    Log.d("yeet", "onFailure: " + e.toString());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    // Use Jackson to parse the JSON response
                    try {
                        // Read the JSON response as a JsonNode

                        String responseBody = response.body().string();

                        // Use Jackson to parse the JSON response
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {

                            ArrayList<String> userStores = new ArrayList<String>();

                            // Convert JSON array to an array of Store objects
                            Store[] stores = objectMapper.readValue(responseBody, Store[].class);

                            // Print the result
                            for (Store store : stores) {
                                userStores.add(store.getName());
                                Log.d("yeet", "onResponse: " + store.getName());
                            }

                            // Update the adapter's data
                            storeArray.clear();
                            storeArray.addAll(userStores);

                            // Notify the adapter that the data has changed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    storeArrayAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (Exception e) {
                            Log.e("yeet", e.toString());
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        Log.d("yeet", "onResponse: Bruh");
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("yeet", e.toString());
            e.printStackTrace();
        }

    }

    private void getPlaces(String latitude, String longitude) {
        // https://places.googleapis.com/v1/places:searchNearby
        String json = "{\n" +
                "  \"maxResultCount\": 10,\n" +
                "  \"locationRestriction\": {\n" +
                "    \"circle\": {\n" +
                "      \"center\": {\n" +
                "        \"latitude\": " + latitude + ",\n" +
                "        \"longitude\": " + longitude + "},\n" +
                "      \"radius\": 500.0\n" +
                "    }\n" +
                "  }" +
                "}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        String url = "https://places.googleapis.com/v1/places:searchNearby";

        Request request = new Request.Builder()
                .url(url)
                //.addHeader("Content-Type", "application/json")
                .addHeader("X-Goog-Api-Key", "AIzaSyA3OfvVJ5o-5fsRdkOsvgSk4ZMb-MyYAo0")
                .addHeader("X-Goog-FieldMask", "places.displayName")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(MainMenuActivity.this, "server down", Toast.LENGTH_SHORT).show();
                pagenameTextView.setText("error connecting to the server");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Use Jackson to parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Read the JSON response as a JsonNode

                    String responseBody = response.body().string();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);

                    // Get the "places" array
                    JsonNode placesNode = jsonNode.get("places");

                    ArrayList<String> storesFound = new ArrayList<String>();
                    // Convert the "places" array to a Java array (if it's an array)
                    if (placesNode.isArray()) {
                        // Iterate through the array
                        for (JsonNode placeNode : placesNode) {
                            // Access individual elements
                            String displayName = placeNode.get("displayName").get("text").asText();
                            // Log.d("yeet", "onResponse: " + displayName);
                            if (usersStores.contains(displayName)) {
                                storesFound.add(displayName);
                            }

                        }
                    }

                    if (storesFound.size() != 0) {
                        runOnUiThread(() -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                            builder.setTitle("You are near by these stores. Check your grocery list");
                            // add a list
                            builder.setItems(storesFound.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("yeet", "onClick: " + which);
                                    startActivity(new Intent(MainMenuActivity.this, ItemListActivity.class));
                                }
                            });
                            // create and show the alert dialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });
                    }



                } catch (Exception e) {
                    Log.d("yeet", "onResponse: Bruh");
                    e.printStackTrace();
                }
            }

        }); }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView = "Latitude: " + mLastLocation.getLatitude() + "";
            longitTextView = "Longitude: " + mLastLocation.getLongitude() + "";
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}