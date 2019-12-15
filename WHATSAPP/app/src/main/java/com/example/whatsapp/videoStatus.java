package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.VideoView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class videoStatus extends AppCompatActivity {

    DatabaseReference mainref;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_status);
        final VideoView mp4Video=findViewById(R.id.videoStatus1);
        Intent intent1=getIntent();
        String attach_path=intent1.getStringExtra("video");
        FirebaseApp.initializeApp(this);
        mp=MediaPlayer.create(this, Uri.parse(attach_path));
        mp4Video.setVideoURI(Uri.parse(attach_path));
        mp4Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp4Video.start();
            }
        });







    }


    }

