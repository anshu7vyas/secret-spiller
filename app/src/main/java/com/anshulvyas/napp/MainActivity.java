package com.anshulvyas.napp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import com.anshulvyas.napp.utils.SystemPropertiesProxy;

public class MainActivity extends AppCompatActivity {
    private Button mButtonContacts;
    private Button mButtonGPS;
    private Button mButtonIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonContacts = (Button) findViewById(R.id.button_contacts);
        mButtonGPS = (Button) findViewById(R.id.button_GPS);
        mButtonIMEI = (Button) findViewById(R.id.button_IMEI);

        mButtonContacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentContacts = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intentContacts);
            }
        });

        mButtonGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentGPS = new Intent(MainActivity.this, GPSActivity.class);
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
}
