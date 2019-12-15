package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class photoStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_status);
        ImageView photoStatus=findViewById(R.id.imageStatus1);
        Intent intent1=getIntent();
        String attach_path=intent1.getStringExtra("photo");
        Picasso.get().load(attach_path).resize(100, 100).into(photoStatus);
    }
}
