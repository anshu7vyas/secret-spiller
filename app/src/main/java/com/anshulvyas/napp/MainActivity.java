package com.anshulvyas.napp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.anshulvyas.napp.utils.NoSSLv3SocketFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


public class MainActivity extends AppCompatActivity {
    private Button mButtonContacts;
    private Button mButtonGPS;
    private Button mButtonIMEI;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private byte[] byteAddress = new byte[100];
    private TextView byteAdd;


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

        new SendRequest().execute();
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
                    "%1$s %2$s",
                    location.getLongitude(), location.getLatitude()
            );
//            Toast.makeText(MainActivity.this, message,
//                    Toast.LENGTH_LONG).show();
        }
        return message;
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("http://anshul.aeonext.com/GetRequest.ashx");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("GPS", showCurrentLocation());
                postDataParams.put("IMEI", getIMEI(MainActivity.this));

                Log.e("httpGPS",postDataParams.toString());

//                SSLContext sslcontext = SSLContext.getInstance("TLSv1");
//
//                sslcontext.init(null,
//                        null,
//                        null);
//                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
//
//                HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                Log.i("CLASSNAME", conn.getOutputStream().getClass().toString());
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
                byteAddress = getPostDataString(postDataParams).getBytes("UTF-8");
                os.write(byteAddress);
                System.out.println(byteAddress);
                Log.i("BYTE_ARRAY", getPostDataString(postDataParams).getBytes().toString());


                os.flush();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
                //public StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();

            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
