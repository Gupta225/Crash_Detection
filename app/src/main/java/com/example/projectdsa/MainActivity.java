package com.example.projectdsa;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView xaxis, yaxis, zaxis;
    private Button button1,button2,button3,button0,button4;
    private TextView no1,no2,no3,entry;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private MyArrayList<String> phoneNumbers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get a reference to the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Get a reference to the accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Register the accelerometer sensor with the SensorManager
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        xaxis = findViewById(R.id.xaxis);
        yaxis = findViewById(R.id.yaxis);
        zaxis = findViewById(R.id.zaxis);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button0=findViewById(R.id.button0);
        button4=findViewById(R.id.button4);
        no1=findViewById(R.id.no1);
        no2=findViewById(R.id.no2);
        no3=findViewById(R.id.no3);
        entry=findViewById(R.id.entry);
        phoneNumbers = new MyArrayList<>();
        loadPhoneNumbers();
        if(phoneNumbers.size()==1)
            no1.setText(phoneNumbers.get(0).toString());
        if(phoneNumbers.size()==2){
            no1.setText(phoneNumbers.get(0).toString());
            no2.setText(phoneNumbers.get(1).toString());
        }
        if(phoneNumbers.size()==3){
            no1.setText(phoneNumbers.get(0).toString());
            no2.setText(phoneNumbers.get(1).toString());
            no3.setText(phoneNumbers.get(2).toString());
        }

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num1=entry.getText().toString();
                if(num1.length()==10&& phoneNumbers.size()<3){

                    phoneNumbers.add(num1);
                    if(phoneNumbers.size()==1)
                        no1.setText(phoneNumbers.get(0).toString());
                    if(phoneNumbers.size()==2){
                        no1.setText(phoneNumbers.get(0).toString());
                        no2.setText(phoneNumbers.get(1).toString());
                    }
                    if(phoneNumbers.size()==3){
                        no1.setText(phoneNumbers.get(0).toString());
                        no2.setText(phoneNumbers.get(1).toString());
                        no3.setText(phoneNumbers.get(2).toString());
                    }
                    savePhoneNumbers();
                    entry.setText("");
                    recreate();
                }
                else{
                    Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumbers.size()>=1){
                phoneNumbers.remove(0);
                savePhoneNumbers();
                recreate();}
                else{
                    recreate();
                    Toast.makeText(MainActivity.this, "invalid action", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumbers.size()>=2){
                phoneNumbers.remove(1);
                savePhoneNumbers();
                recreate();}
                else {
                    recreate();
                    Toast.makeText(MainActivity.this, "invalid action", Toast.LENGTH_SHORT).show();

                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumbers.size()>=3) {
                    phoneNumbers.remove(2);
                    savePhoneNumbers();
                    recreate();
                }
                else{
                    recreate();
                    Toast.makeText(MainActivity.this, "invalid action", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumbers.clear();
                savePhoneNumbers();
                recreate();
            }
        });
    }

    private void loadPhoneNumbers() {
        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Retrieve the stored phone numbers as a comma-separated string
        String phoneNumberString = sharedPreferences.getString("phoneNumbers", "");
        // Split the comma-separated string into individual phone numbers and add them to the ArrayList
        if (!phoneNumberString.isEmpty())
        {
            String[] phoneNumberArray = phoneNumberString.split(",");
            phoneNumbers.addAll(Arrays.asList(phoneNumberArray));
        }
    }
    private void savePhoneNumbers() {
        // Get the SharedPreferences editor to store the phone numbers
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Convert the ArrayList of phone numbers to a comma-separated string and store it
        StringBuilder phoneNumberString = new StringBuilder();
        for (int i=0;i<phoneNumbers.size();i++) {
            phoneNumberString.append(phoneNumbers.get(i)).append(",");
        }
        editor.putString("phoneNumbers", phoneNumberString.toString());
        // Commit the changes to SharedPreferences
        editor.apply();
    }
    private void getCurrentLocation() {
        // Check if the app has location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request a single location update
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Use the location object to get latitude and longitude
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                // Do something with the latitude and longitude values
                                // For example, display them in TextViews or use them to update the UI
                                // For demonstration purposes, let's display the location in a Toast message
                                String locationMsg = "Latitude: " + latitude + ", Longitude: " + longitude;
                                Toast.makeText(MainActivity.this, locationMsg, Toast.LENGTH_SHORT).show();
                                // Detect crash based on magnitude of acceleration
                               // detectCrash(location);
                                // Send SMS with location info to specific contacts
                                sendLocationSMS(latitude, longitude);
                            } else {
                                // Location is null, handle the case when the location is not available
                                Toast.makeText(MainActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendLocationSMS(double latitude, double longitude) {
        String smsMessage = "I am in trouble!  My location is: " + getAddressFromCoordinates(latitude, longitude);
        try {
            // Send SMS using SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            if(phoneNumbers.size()!=0) {
                for (int i = 0; i < phoneNumbers.size(); i++) {
                    smsManager.sendTextMessage(phoneNumbers.get(i).toString(), null, smsMessage, null, null);
                }
                Toast.makeText(MainActivity.this, "Location SMS sent!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "No phone numbers", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle exceptions if SMS sending fails

            Toast.makeText(MainActivity.this, "Failed to send Location SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        String addressText = "Unknown location latitude :"+latitude+"longitude"+longitude;
        try {
            // Get addresses from the given latitude and longitude
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText = address.getAddressLine(0); // You can customize this to get additional address details if needed.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressText;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        x=Math.abs(x);
        y=Math.abs(y);
        z=Math.abs(z);
        xaxis=findViewById(R.id.xaxis);
        yaxis=findViewById(R.id.yaxis);
        zaxis=findViewById(R.id.zaxis);
        xaxis.setText(x+"");
        yaxis.setText(y+"");
        zaxis.setText(z+"");
        float val= (float) Math.sqrt(x*x+y*y+z*z);
        if(val>60){
            getCurrentLocation();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the accelerometer sensor when the activity is paused
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register the accelerometer sensor again when the activity is resumed
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}