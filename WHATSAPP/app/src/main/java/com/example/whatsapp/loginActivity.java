package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;

    private EditText mobile, otpEntry;

    private Button sendOTP, verifyNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startPersmissionsLogic();
        mobile =       findViewById(R.id.mobile);
        otpEntry =     findViewById(R.id.otpEntry);
        sendOTP =      findViewById(R.id.sendOTP);
        verifyNumber = findViewById(R.id.verifyNumber);
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", null);
        String  email = sharedPreferences.getString("email", null);
        String  fullName = sharedPreferences.getString("fullName", null);


        if(mobile==null
        )
        {

        }
        else
        {
            Intent intent = new Intent(this, whatsAppTabs.class);
            startActivity(intent);
            finish();
        }





    }


    public void verifyMobileNum(View view) {
        Log.d("Mymessage", otpEntry.getText().toString());
        if (!mobile.getText().toString().equals("")) {
            if(mobile.getText().toString().length()==10)
            {
            if (otpEntry.getText().toString().equals("123456")) {
                Toast.makeText(this, "OTP MATCHED", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=getSharedPreferences("mypref",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("mobile","+91"+mobile.getText().toString());
                editor.commit();

                SystemClock.sleep(100);
                Intent intent = new Intent(this, signUp.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "OTP NOT  MATCHED", Toast.LENGTH_SHORT).show();
            }

        }
        else
            {
                Toast.makeText(this, "MOBILE NUMBER MUST BE OF 10 DIGITS", Toast.LENGTH_SHORT).show();

            }
        }

        else
        {
            Toast.makeText(this, "MOBILE NUMBER NOT FILLED", Toast.LENGTH_SHORT).show();
        }

    }


    /*PERMISSION LOGIC*/
    void startPersmissionsLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Check If Permissions are already granted, otherwise show Ask Permission Dialog

            if (checkPermission()) {

                Toast.makeText(this, "All Permissions Already Granted", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                requestPermission();
            }

        }
    }
    public boolean checkPermission() {

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;

        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean result4 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean result5 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        boolean result6 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2 && result3 && result4 && result5 && result6;

    }
    public void requestPermission() {

        //Show ASK FOR PERSMISSION DIALOG (passing array of permissions that u want to ask)

        ActivityCompat.requestPermissions(this,

                new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 1);

    }
    // After User Selects Desired Permissions, thid method is automatically called
    // It has request code, permissions array and corresponding grantresults array

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 1) {

            if (grantResults.length > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "All PERMISSON GRANTED", Toast.LENGTH_SHORT).show();
                }


                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
                }


                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[4] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Read Contacts Permission Granted", Toast.LENGTH_SHORT).show();

                }
                if (grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Read PHONE STATE Permission Granted", Toast.LENGTH_SHORT).show();

                }


                if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED && grantResults[2] == PackageManager.PERMISSION_DENIED && grantResults[3] == PackageManager.PERMISSION_DENIED && grantResults[4] == PackageManager.PERMISSION_DENIED && grantResults[5] == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(this, "All Permission Denied", Toast.LENGTH_SHORT).show();

                }

            }

        }


    }



    /*PERMISSION LOGIC ENDS HERE*/

}
