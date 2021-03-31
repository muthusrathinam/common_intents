package com.example.btn_navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText getPhone,msgBox;
    Button call,sms,web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call = (Button) findViewById(R.id.call);
        sms = (Button) findViewById(R.id.message);
        web = (Button) findViewById(R.id.takeToWeb);
        getPhone = (EditText) findViewById(R.id.getPhone);
        msgBox = (EditText) findViewById(R.id.etMsg);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = getPhone.getText().toString();
                Toast.makeText(MainActivity.this, "Redirecting to phone call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Redirecting to web", Toast.LENGTH_SHORT).show();
                gotoUrl("https://www.mycaptain.in/");
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Redirecting to sms", Toast.LENGTH_SHORT).show();
                if(checkPermission(Manifest.permission.SEND_SMS)){
                    sendSMS();
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });

    }
    private void gotoUrl(String s){
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    public void sendSMS()
    {
        String number = getPhone.getText().toString();
        String msg = msgBox.getText().toString();

        if(number == null || number.length() != 10 || msg.length() == 0){
            return;
        }
        if(checkPermission(Manifest.permission.SEND_SMS)){
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(MainActivity.this, "Msg sent!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}