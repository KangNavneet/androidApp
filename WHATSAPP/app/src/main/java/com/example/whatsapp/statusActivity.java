package com.example.whatsapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class statusActivity extends AppCompatActivity {
    LinearLayout ll1;
    ImageView imageView1;
    VideoView videoView1;
    ProgressBar progressBar[];
    int i = 0;
    int j = 0;
    int f = 0;
    String photoid;
    int videoid;
    String videopath;
    TextView tvtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Intent incomingintent = getIntent();
        String uploadedby = incomingintent.getStringExtra("statusMob");
        Toast.makeText(this, uploadedby, Toast.LENGTH_SHORT).show();
        tvtext = findViewById(R.id.tvtext);

        final ArrayList<storyClass> finalstorieslist = new ArrayList<>();
             Log.d("Mymsg", "global story count" + GloblaClass.globalstorieslist.size());
        for (storyClass st : GloblaClass.globalstorieslist) {
            if (st.getPost_by().equals(uploadedby)) {
                finalstorieslist.add(st);
            }
        }


        final int progressbarcount = finalstorieslist.size();
        //    Log.d("MYMSG", "Progress " + progressbarcount);

        ll1 = (LinearLayout) (findViewById(R.id.ll1));
        imageView1 = (ImageView) (findViewById(R.id.imv1));
        videoView1 = (VideoView) (findViewById(R.id.vv1));

        ViewTreeObserver vto = ll1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                try {
                    if (f == 0) {
                        f = 1;
                        int width = ll1.getMeasuredWidth();
                        //        Log.d("MYMSG", "Width " + ll1.getMeasuredWidth());
                        width = width / progressbarcount;

                        progressBar = new ProgressBar[progressbarcount];
                        for (int i = 0; i < progressbarcount; i++) {
                            progressBar[i] = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    width, // Width in pixels
                                    LinearLayout.LayoutParams.WRAP_CONTENT // Height of progress bar
                            );
                            progressBar[i].setLayoutParams(lp);
                            progressBar[i].setPadding(1, 1, 1, 1);
                            ll1.addView(progressBar[i]);
                        }
                        new Thread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
                            @Override
                            public void run() {
                                for (i = 0; i < progressbarcount; i++) {

                                    if (finalstorieslist.get(i).getTypeOfFile().equals("Photo")) {
                                        photoid = finalstorieslist.get(i).getAttach_path();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                videoView1.setVisibility(View.GONE);
                                                tvtext.setVisibility(View.GONE);
                                                imageView1.setVisibility(View.VISIBLE);
                                                Picasso.get().load(photoid).into(imageView1);

                                            }
                                        });
                                        for (j = 1; j <= 100; j++) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar[i].setProgress(j);
                                                }
                                            });
                                            try {
                                                Thread.sleep(50);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //                      Log.d("xxx", "photo");
                                    }
                                    else if (finalstorieslist.get(i).getTypeOfFile().equals("Video")) {
                                        final String videopath = finalstorieslist.get(i).getAttach_path();

                                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                        if (Build.VERSION.SDK_INT >= 14)
                                            retriever.setDataSource(videopath, new HashMap<String, String>());
                                        else
                                            retriever.setDataSource(videopath);

                                        String mVideoDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                        long mTimeInMilliseconds = Long.parseLong(mVideoDuration);
                                        int t = (int) mTimeInMilliseconds / 100;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String path = videopath;
                                                videoView1.setVisibility(View.VISIBLE);
                                                imageView1.setVisibility(View.GONE);
                                                tvtext.setVisibility(View.GONE);

                                                videoView1.setVideoURI(Uri.parse(path));
                                                videoView1.requestFocus();
                                                videoView1.start();
                                            }
                                        });

                                        try {
                                            for (j = 1; j <= 100; j++) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressBar[i].setProgress(j);
                                                    }
                                                });
                                                try {
                                                    Thread.sleep(t);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //                    Log.d("xxx", "video " + t);
                                    }
                                    else if (finalstorieslist.get(i).getTypeOfFile().equals("Text")) {
                                        final String texttype = finalstorieslist.get(i).getCaption();
///       String mVideoDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                        long mTimeInMilliseconds = Long.parseLong("5000");
                                        int t = (int) mTimeInMilliseconds / 100;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                                String path = videopath;
                                                videoView1.setVisibility(View.GONE);
                                                imageView1.setVisibility(View.GONE);

                                                tvtext.setText(texttype);
                                                tvtext.setTextSize(30);

                                            }
                                        });

                                        try {
                                            for (j = 1; j <= 100; j++) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressBar[i].setProgress(j);
                                                    }
                                                });
                                                try {
                                                    Thread.sleep(t);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //                    Log.d("xxx", "video " + t);
                                    }




                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), whatsAppTabs.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("tab", 2);
                                        startActivity(intent);
                                        Toast.makeText(statusActivity.this, "Finish 1", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).start();


                    }
                } catch (Exception ex1) {

                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //    Log.d("abc","on start called");
//        ScopeofGlobalClass.databaseReference.child("Users").child(ScopeofGlobalClass.Usermobile).child("onlinestatus").setValue(true);
//
//    }

}
