package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class myStatus extends AppCompatActivity {

    Button postPhoto,postVideo,postStory;
    EditText postCaption;
    Uri postImageUri,postVideoUri ;
    DatabaseReference mainref;

    StorageReference mainrefStore;
    FirebaseStorage firebaseStorage;
    ProgressBar progressStatus;
    File f;
    Uri postUri,uriAttach;
    String caption="";
    Integer count=0;
    String typeOfFile="Text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);
        postPhoto=findViewById(R.id.postPhoto);
        postVideo=findViewById(R.id.postVideo);
        postStory=findViewById(R.id.postStory);
        postCaption=findViewById(R.id.postCaption);
        progressStatus=findViewById(R.id.progressStatus);


        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count==0)
                {
                    Intent intent =  new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 90);


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"HEY,POST JUST ONE STORY :))",Toast.LENGTH_SHORT).show();
                }



            }
    });


        postVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");

                    startActivityForResult(intent, 100);


                }
                else

                {
                    Toast.makeText(getApplicationContext(),"HEY,POST JUST ONE STORY :))",Toast.LENGTH_SHORT).show();
                }


            }

        });

        postStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    caption=postCaption.getText().toString();
                    sendChatToFirebase();


                //sendChatToFirebase();




            }
        });









    }



    private void reset()
    {
typeOfFile="Text";
uriAttach=Uri.parse("");
caption="";
ProgressBar progressStatus;

    }

    private void sendChatToFirebase() {

        System.out.println("Send Chat To Firebase");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("Story");
        mainref.keepSynced(true);


        /*DATA OF THE USER*/
        Date storyDateTime = new Date();
        String storyDateTime1 = storyDateTime.toString();
        Long storyId= storyDateTime.getTime();

        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", null);
        String msgFrom = mobile;

        Long date_of_post = storyDateTime.getTime();
        Long date_of_expiry = date_of_post+86400000;

        String Caption=caption;
        String fileType=typeOfFile;
        String attachPath;
        if (uriAttach!= null) {
            attachPath=uriAttach.toString();

        } else {
            attachPath="";
        }
        /*DATA OF THE USER ENDS HERE*/

        /*NOW SEND DATA TO FIREBASE*/

        storyClass s1 = new storyClass(storyId, msgFrom, date_of_post, date_of_expiry, attachPath, caption, fileType);
        mainref.child(storyId.toString()).setValue(s1);

        Toast.makeText(this, "DATA ENTERED!!!", Toast.LENGTH_LONG).show();

    }

    private void databaseEntry(final String identity) {

        FirebaseApp.initializeApp(this);
        firebaseStorage = FirebaseStorage.getInstance();
        mainrefStore = firebaseStorage.getReference();
        f = new File(getPath(postUri));
        Log.d("hello", f.getName());
        StorageReference sr3 = mainrefStore.child(f.getName());

        UploadTask uploadTask = sr3.putFile((Uri.fromFile(f)));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "FILE UPLOADED", Toast.LENGTH_LONG).show();
                mainrefStore.child(f.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(), "Url Downloaded Successfully" + uri, Toast.LENGTH_SHORT).show();

                        if(identity=="Photo")
                        {
                            uriAttach=uri;
                            typeOfFile=identity;
                        }
                        else if(identity=="Video")
                        {
                            uriAttach=uri;
                            typeOfFile=identity;

                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(), "NOTHING UPLOADED" + uri, Toast.LENGTH_SHORT).show();

                        }





                        Log.d("hello", uri.toString());


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication(), "", Toast.LENGTH_SHORT).show();
                        Log.d("hello", e.getMessage());
                    }
                });
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100 * taskSnapshot.getBytesTransferred()) / (taskSnapshot.getTotalByteCount());
                progressStatus.setProgress((int) progress);

            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("hello", e.getMessage());
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backintent) {

        super.onActivityResult(requestCode, resultCode, backintent);
        if (requestCode == 80)   // from camera
        {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) (backintent.getExtras().get("data"));
                //imv1.setImageBitmap(bmp);
            }
        } else if (requestCode == 90)    // from gallery
        {
            if (resultCode == RESULT_OK) {
                postImageUri= backintent.getData();
                postUri=postImageUri;
                count++;
                databaseEntry("Photo");

                //imv1.setImageURI(fileuri);
            }
        } else if (requestCode == 100)    // from VIDEO
        {
            if (resultCode == RESULT_OK) {
                postVideoUri = backintent.getData();
                postUri=postVideoUri;
                count++;
                databaseEntry("Video");


            }
        } else if (requestCode == 110)    // from AUDIO
        {
//            if (resultCode == RESULT_OK) {
//                postAudioUri = backintent.getData();
//
//            }
        }

    }
}
