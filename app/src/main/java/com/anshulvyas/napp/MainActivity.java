package com.anshulvyas.napp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

//import com.anshulvyas.napp.utils.NoSSLv3SocketFactory;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "~!@#$MainActivity";
    private Button mButtonContacts;
    private Button mButtonGPS;
    private Button mButtonIMEI;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private byte[] byteAddress = new byte[100];
    private byte[] imeiBytes = new byte[15];
    private byte[] contactsBytes = new byte[1000];
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

        //new SendRequest().execute("hello");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                String contacts = getIntent().getStringExtra(ContactsActivity.KEY_INTENT_DATA);
//                Log.i("~!@#$", getIntent().getStringExtra(ContactsActivity.KEY_INTENT_DATA));
                new SendRequest().execute(contacts);
            }
        }
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


//    public String getContactsList() {
//
//        ContactsActivity contactsActivity = new ContactsActivity();
//        String contacts = contactsActivity.fetchContacts();
//        return contacts;
//
//    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... args) {
            try{
                String data = args[0];
                Log.i(TAG, "doInBackground: " + data);
                URL url = new URL("http://anshul.aeonext.com/GetRequest.ashx");
                JSONObject postDataParams = new JSONObject();
                //
                JSONObject newPostDataParams = new JSONObject();
                JSONObject contactsPostDataParams = new JSONObject();
                //
                postDataParams.put("GPS", showCurrentLocation());
                //
                newPostDataParams.put("IMEI", getIMEI(MainActivity.this));
                //
                contactsPostDataParams.put("contacts", data);
                //
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

                imeiBytes = getPostDataString(newPostDataParams).getBytes("UTF-8");

                os.write(byteAddress);
                System.out.println(byteAddress);
                Log.i("BYTE_ARRAY", getPostDataString(postDataParams).getBytes().toString());

                os.write(imeiBytes);
                System.out.println(imeiBytes);
                Log.i("IMEI_BYTE_ARRAY", getPostDataString(newPostDataParams).getBytes().toString());

                String contactsDataFromServer = getPostDataString(contactsPostDataParams);
                Log.i("CONTACTS_BYTE_ARRAY", contactsDataFromServer);
                os.write(contactsDataFromServer.getBytes("UTF-8"));
//                Log.i("CONTACTS_BYTE_ARRAY", getPostDataString(newPostDataParams));
//                os.write(contactsBytes);


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
                    return new String("false : " + responseCode);
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

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
