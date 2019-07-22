package com.example.sms7_2_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private Button mSendMessageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //   initView();
        Button mDialButton = (Button) findViewById(R.id.btn_dial);
        mSendMessageBtn = (Button) findViewById(R.id.btn_send_message);
        final EditText mPhoneNoEt = (EditText) findViewById(R.id.et_phone_no);
        final EditText messagetEt = (EditText) findViewById(R.id.et_message);

        mDialButton.setEnabled(true);  //кнопка активна

        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {         //call
                String phoneNo = mPhoneNoEt.getText().toString();
                if (!TextUtils.isEmpty(phoneNo)) {
                    if (checkPermission(Manifest.permission.CALL_PHONE)) {  //  разрешение получено

                        String dial = "tel:" + phoneNo; //совершаем звонок
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        Toast.makeText(MainActivity.this, "call-call", Toast.LENGTH_SHORT).show();

                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);    //получаем разрешение
                        if (checkPermission(Manifest.permission.CALL_PHONE)) {

                            String dial = "tel:" + phoneNo;    //звоним
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        } else {
                            Toast.makeText(MainActivity.this, "permission not received error", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSendMessageBtn.setEnabled(true);  // кнопка активна

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //sms
                String message = messagetEt.getText().toString();
                String phoneNo = mPhoneNoEt.getText().toString();
                if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(phoneNo) && !MainActivity.this.isFinishing()) {

                    if (checkPermission(Manifest.permission.SEND_SMS)) {    //разрешение получено
                        try {
                            SmsManager smsManager = SmsManager.getDefault();      //отправка смс
                            smsManager.sendTextMessage(phoneNo, null, message, null, null);
                            Toast.makeText(MainActivity.this, "sms sent to", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                                    Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},
                                SEND_SMS_PERMISSION_REQUEST_CODE);  //получаем разрешение
                        if (checkPermission(Manifest.permission.SEND_SMS)) {

                            SmsManager smsManager = SmsManager.getDefault();   //отправка смс
                            smsManager.sendTextMessage(phoneNo, null, message, null, null);

                        } else {
                            Toast.makeText(MainActivity.this, "permission not received error", Toast.LENGTH_SHORT).show();  //нет разреш
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter a phone number end enter a sms", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mSendMessageBtn.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, " you do not have permission", Toast.LENGTH_SHORT).show();

                }
                break;
            }

            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mSendMessageBtn.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, " you do not have permission", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }
    }
}