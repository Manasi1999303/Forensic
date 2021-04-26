package com.example.forensicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button callLogsBtn,smsBtn,contactsBtn,Mobile_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callLogsBtn = findViewById(R.id.call_logs_btn);
        callLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CallLogActivity.class);
                startActivity(intent);
            }
        });

        smsBtn = findViewById(R.id.sms_btn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SmsTrialActivity.class);
                startActivity(intent);
            }
        });
        contactsBtn = findViewById(R.id.contacts_btn);
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewContactsActivity.class);
                startActivity(intent);
            }
        });
        Mobile_details = findViewById(R.id.mobile_details);
        Mobile_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}