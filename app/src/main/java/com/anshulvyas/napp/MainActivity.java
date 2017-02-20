package com.anshulvyas.napp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button mButtonContacts;
    private Button mButtonGPS;
    private Button mButtonIMEI;
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonContacts = (Button) findViewById(R.id.button_contacts);
        mButtonGPS = (Button) findViewById(R.id.button_GPS);
        mButtonIMEI = (Button) findViewById(R.id.button_IMEI);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                String message = String.format(
                        "New Location \n Longitude: %1$s \n Latitude: %2$s",
                        location.getLongitude(), location.getLatitude()
                );
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String s, int i, Bundle b) {

            }

            public void onProviderDisabled(String s) {

            }

            public void onProviderEnabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800, 3, locationListener);


//        mButtonContacts.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intentContacts = new Intent(MainActivity.this, ContactsActivity.class);
//                startActivity(intentContacts);
//            }
//        });

        mButtonGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.w("readCoordinates", showCurrentLocation());
                Intent intentGPS = new Intent(MainActivity.this, GPSActivity.class);
                intentGPS.putExtra("GPSCoordinates", showCurrentLocation());
                startActivity(intentGPS);
            }
        });

        mButtonIMEI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.w("readIMEI", getIMEI(MainActivity.this));
                Intent intentIMEI = new Intent(MainActivity.this, IMEIActivity.class);
                intentIMEI.putExtra("IMEI", getIMEI(MainActivity.this));
                startActivity(intentIMEI);
            }
        });
    }

    public String getIMEI(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        //String imei = SystemPropertiesProxy.get(this, "ro.gsm.imei");
        return imei;
    }

    public String showCurrentLocation() {
        String message = "";
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
//            Toast.makeText(MainActivity.this, message,
//                    Toast.LENGTH_LONG).show();
        }
        return message;
    }
}
