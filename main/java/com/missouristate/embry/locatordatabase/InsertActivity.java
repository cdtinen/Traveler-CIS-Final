package com.missouristate.embry.locatordatabase;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.AccessControlException;

public class InsertActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private static final String[] INITIAL_PERMS={Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INIT_REQ=42;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DatabaseManager(this);
        setContentView(R.layout.activity_insert);

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void insert(View v) {
        EditText firstEditText = findViewById(R.id.input_name);
        String lat = "0"; //if GPS is broken somehow this acts as a replacement
        String lon = "0";
        String name = firstEditText.getText().toString();

        try {
            
            int locationProviders = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            
            if (locationProviders == 0) {
                throw new Settings.SettingNotFoundException("Location Services Disabled");
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //not granted
                throw new AccessControlException("Location Permissions Disabled");
            }

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null){
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
            } //else they equal 0.0 if null

            Locat locat = new Locat(0, name, lat, lon);
            dbManager.insert(locat);
            Toast.makeText(this,"Location added", Toast.LENGTH_SHORT).show();

        }
        catch(AccessControlException e){
            Toast.makeText(this, "Please Enable Location Permissions", Toast.LENGTH_LONG).show();
            requestPermissions(INITIAL_PERMS, INIT_REQ);
        }
        catch(Settings.SettingNotFoundException e){
            Toast.makeText(this, "Please Enable Location", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        firstEditText.setText("");
    }

    public void goBack(View v) {
        this.finish();
    }
}
