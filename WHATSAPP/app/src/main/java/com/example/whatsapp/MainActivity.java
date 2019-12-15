package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;


public class MainActivity extends AppCompatActivity {
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemClock.sleep(100);
        Intent intent=new Intent(this,loginActivity.class);
        startActivity(intent);

        finish();



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null)
        {
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent realLogin =new Intent(MainActivity.this, com.example.whatsapp.realLogin.class);
        startActivity(realLogin);

    }
}
