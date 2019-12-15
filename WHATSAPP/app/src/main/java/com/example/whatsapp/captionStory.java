package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class captionStory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption_story);

        TextView captionStatus=findViewById(R.id.captionStatus);

        Intent intent1=getIntent();
        String caption=intent1.getStringExtra("caption");
        captionStatus.setText(caption);




    }
}
