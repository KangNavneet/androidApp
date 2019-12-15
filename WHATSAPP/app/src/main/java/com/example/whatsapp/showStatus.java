package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class showStatus extends AppCompatActivity {
String mobile3;
    ImageView statusProfile;
    TextView statusName,statusPhone,statusStatus;
DatabaseReference mainref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_status);
//
        statusProfile=findViewById(R.id.statusProfile);
        statusName=findViewById(R.id.statusName);
//        statusPhone=findViewById(R.id.statusPhone);
//        statusStatus=findViewById(R.id.statusStatus);
//
//
//
//
        Intent intent=getIntent();
        mobile3=intent.getStringExtra("mobile3");
        Toast.makeText(this, mobile3, Toast.LENGTH_SHORT).show();
        statusName.setText(mobile3);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("Contacts");
//
//
        mainref.child(mobile3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("abc",dataSnapshot.toString());
                whatsappDataEntry w1=dataSnapshot.getValue(whatsappDataEntry.class);//Nested Key Value
                statusName.setText(w1.fullName+"");
//                statusPhone.setText(w1.emailId+"\n"+w1.mobileNo);
//                statusStatus.setText(w1.status+"");
                Picasso.get().load(w1.photopath).resize(250, 250).into(statusProfile);
                Log.d("DATASNAPSHOT",w1.emailId+"---------------"+w1.fullName+"--------"+w1.photopath+"-----------------"+w1.status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
