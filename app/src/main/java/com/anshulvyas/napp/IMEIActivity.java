package com.anshulvyas.napp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IMEIActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imei);
        Intent intent = getIntent();
        String IMEI = intent.getExtras().getString(Intent.EXTRA_TEXT);
        textView = (TextView) findViewById(R.id.tv_imei);
        textView.setText(IMEI);

    }
}
