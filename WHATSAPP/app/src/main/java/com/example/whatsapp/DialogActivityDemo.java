package com.example.whatsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogActivityDemo extends AppCompatActivity {
Button editOk,editCancel;
EditText editMsg;
DatabaseReference mainref;
    long msgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        editOk=findViewById(R.id.editOk);
        editCancel=findViewById(R.id.editCancel);
        editMsg=findViewById(R.id.editMsg);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        mainref = firebaseDatabase.getReference("Messages");
        Intent in=getIntent();
        msgId = in.getLongExtra("msgId",88888);
//       msgId =in.getStringExtra("msgId").toString();
//        Toast.makeText(DialogActivityDemo.this, "MSGID:"+msgId, Toast.LENGTH_SHORT).show();

        editOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  abc = editMsg.getText().toString();

                if(abc.equals(""))
                {
                    Toast.makeText(DialogActivityDemo.this, "ENTER MESSAGE", Toast.LENGTH_SHORT).show();

                }
                else
                {
//                  Toast.makeText(DialogActivityDemo.this, "D"+abc+msgId, Toast.LENGTH_SHORT).show();
                    mainref.child(msgId+"").child("msgText").setValue(abc);
                    Toast.makeText(DialogActivityDemo.this, "DATA UPDATED", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
