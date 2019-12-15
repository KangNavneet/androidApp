package com.example.whatsapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class audioFile extends AppCompatActivity {
    Button startPlayer,pausePlayer,stopPlayer;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file);
        startPlayer=findViewById(R.id.startPlayer);
        pausePlayer=findViewById(R.id.pausePlayer);
        stopPlayer=findViewById(R.id.stopPlayer);
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String audioVideo = sharedPreferences.getString("audioVideo1", null);
         mp=new MediaPlayer();
        mp=MediaPlayer.create(this, Uri.parse(audioVideo));
        try{
//            mp.setDataSource("");//Write your location here

            mp.prepare();
        }
        catch(Exception e)
        {e.printStackTrace();

        }
        startPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
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
                mp.pause();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mp.stop();
    }
}
