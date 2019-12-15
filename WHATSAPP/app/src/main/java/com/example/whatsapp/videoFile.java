package com.example.whatsapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;


public class videoFile extends AppCompatActivity {
    Button startPlayer,pausePlayer,stopPlayer;
    VideoView mp4Video;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_file);
        startPlayer=findViewById(R.id.startPlayerVideo);
        pausePlayer=findViewById(R.id.pausePlayerVideo);
        stopPlayer=findViewById(R.id.stopPlayerVideo);
        mp4Video=findViewById(R.id.mp4Video);

        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String audioVideo = sharedPreferences.getString("audioVideo1", null);
        mp=MediaPlayer.create(this, Uri.parse(audioVideo));
        mp4Video.setVideoURI(Uri.parse(audioVideo));
        mp4Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp4Video.start();
            }
        });

        startPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp4Video.start();
            }
        });
        stopPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });
        pausePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4Video.pause();
            }
        });

    }

    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
    }
}
