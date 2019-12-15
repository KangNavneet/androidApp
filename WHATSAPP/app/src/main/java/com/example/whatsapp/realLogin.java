package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class realLogin extends AppCompatActivity {
private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_login);
    }


    protected void onStart() {
        super.onStart();
        if(currentUser!=null)
        {
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity() {

        Intent realLogin =new Intent(this, MainActivity.class);
        startActivity(realLogin);

    }
}
