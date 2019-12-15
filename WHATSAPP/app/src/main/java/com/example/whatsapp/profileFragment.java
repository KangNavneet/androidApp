package com.example.whatsapp;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    ImageView profileImage;
    EditText profileEmail,profileName,profileStatus;
    Button editProfile;
    Button editSelectImageCamera,editSelectImageGallery;
    String photoPath;
    FirebaseStorage firebaseStorage;
    StorageReference mainref;

    DatabaseReference mainrefStore;
    Uri fileuri;



    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this.getActivity());
        firebaseStorage = FirebaseStorage.getInstance();
        //MAIN REFERENCE TO DS BUCKET
        mainref = firebaseStorage.getReference("/");





        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//        mainref=firebaseDatabase.getReference("student/1");
        mainrefStore=firebaseDatabase.getReference("Contacts");
        //  mainref= firebaseStorage.getReference();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);



    }
    public void onStart() {
        /*STORAGE OF ITEMS*/
        super.onStart();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
       final String mobile = sharedPreferences.getString("mobile", null);


//
//        String email = sharedPreferences.getString("email", null);
//        String name = sharedPreferences.getString("fullName", null);
//        String status = sharedPreferences.getString("status", null);
//
//        Toast.makeText(getContext(), ""+email, Toast.LENGTH_SHORT).show();
       photoPath = sharedPreferences.getString("photoPath", null);
        profileEmail=this.getActivity().findViewById(R.id.profileEmail);
        profileName=this.getActivity().findViewById(R.id.profileName);
        profileStatus=this.getActivity().findViewById(R.id.profileStatus);
        profileImage=this.getActivity().findViewById(R.id.profileImage);
        editSelectImageGallery=this.getActivity().findViewById(R.id.editSelectImageGallery);
        editProfile=this.getActivity().findViewById(R.id.editProfile);

        mainrefStore.child(mobile).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                whatsappDataEntry w1=dataSnapshot.getValue(whatsappDataEntry.class);//Nested Key Value
                profileName.setText(w1.fullName+"");
                profileEmail.setText(w1.emailId+"");
                profileStatus.setText(w1.status+"");
                Picasso.get().load(w1.photopath).resize(250, 250).into(profileImage);
                Log.d("DATASNAPSHOT",w1.emailId+"---------------"+w1.fullName+"--------"+w1.photopath+"-----------------"+w1.status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize(mobile);
            }
        });

        editSelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FROM GALLERY
                Intent intent =  new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 90);
            }

            public String getPath(Uri uri) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) return null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String s = cursor.getString(column_index);
                cursor.close();
                return s;
            }



            public Uri getImageUri(Context inContext, Bitmap inImage) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
                return Uri.parse(path);
            }



        }

        );
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent backintent) {

        if (requestCode == 90 && resultCode == RESULT_OK)
        {

            fileuri = backintent.getData();
            profileImage.setImageURI(fileuri);
            Picasso.get().load(fileuri).resize(250, 250).into(profileImage);
            Log.d("gallery", getPath(fileuri));

            /*FILE UPLOAD*/
            // File f=new File("/mnt/sdcard/Download/19.jpg");
            File f=new File(getPath(fileuri));
            Log.d("FileDetail",f.toString());

            if(f.exists())
            {
                StorageReference sr3=mainref.child(f.getName());
                UploadTask uploadTask =sr3.putFile((Uri.fromFile(f)));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(),"FILE UPLOADED",Toast.LENGTH_LONG).show();

                    }
                });

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
            /*FILE UPLOAD ENDS HERE*/




            /*DOWNLOAD URL*/
            mainref.child(f.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    photoPath=uri.toString();
                    Picasso.get().load(uri).resize(250, 250).into(profileImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });


            /*DOWNLOAD URL ENDS HERE*/
        }


    }



    private void initialize(String mobile) {

        if(profileName.getText().toString().equals(""))
        {
            Toast.makeText(this.getActivity(),"FULL NAME FIELD EMPTY",Toast.LENGTH_SHORT).show();

        }
        else if(profileEmail.getText().toString().equals(""))
        {
            Toast.makeText(this.getActivity(),"EMAIL FIELD EMPTY",Toast.LENGTH_SHORT).show();

        }
        else if(profileStatus.getText().toString().equals(""))
        {
            Toast.makeText(this.getActivity(),"STATUS FIELD EMPTY",Toast.LENGTH_SHORT).show();

        }
        else
        {
            databaseEntry(mobile);

        }
    }

    public void databaseEntry(String mobile) {

        String name=profileName.getText().toString().trim();
        String email=profileEmail.getText().toString().trim();
        String status=profileStatus.getText().toString().trim();

        String profileImv =photoPath;
        whatsappDataEntry w1=new whatsappDataEntry(mobile,name,email,profileImv,status,profileImv);
        Log.d("MyMessage",w1.emailId+"-----"+w1.fullName+"-----"+w1.photopath+"-----"+w1.status+"------"+w1.mobileNo);
        mainrefStore.child(mobile).setValue(w1);
        Toast.makeText(this.getActivity(),"DATA ENTERED!!!",Toast.LENGTH_LONG).show();



    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
