package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.content.Intent.ACTION_GET_CONTENT;

public class signUp extends AppCompatActivity {
    EditText fullName, emailId, status;
    Button selectImageCamera, selectImageGallery, signUp;
    ImageView setImage;

    Uri fileuri;
    Bitmap bmp;
    ValueEventListener vel;
    Button btnsignup;
    File f  = new File("/mnt/");
    Uri urinew;

    /*FIREBASE STORAGE*/
    FirebaseStorage firebaseStorage;
    StorageReference mainrefStore;
    DatabaseReference mainref;
    ProgressBar mProgressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setImage = findViewById(R.id.signSetImage);
        startPersmissionsLogic();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mainrefStore = firebaseStorage.getReference();
        mainref = firebaseDatabase.getReference("Contacts");
        btnsignup = findViewById(R.id.signUpBtn);
        mProgressBar= findViewById(R.id.mProgressBar);







        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileuri!=null)
                {
                    f = new File(getPath(fileuri));
                    store();

                }
                else
                {
                    initialize();
//                    databaseEntry();

                }





            }
        });







    }
    public void store()
    {
        {

            StorageReference sr3 = mainrefStore.child(f.getName());
            UploadTask uploadTask = sr3.putFile((Uri.fromFile(f)));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "FILE UPLOADED", Toast.LENGTH_LONG).show();


//             StorageReference sr4=mainrefStore.child("mygirldress.png");
//                    File f2=new File("/mnt/sdcard/whatsappfile/mygirldress.png");
//
//                    FileDownloadTask downloadTask1=sr4.getFile(f2);
//                    downloadTask1.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getApplicationContext(),"FILE DOWNLOADED",Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });

                    mainrefStore.child(f.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(getApplicationContext(), "Url Downloaded Successfully" + uri, Toast.LENGTH_SHORT).show();
                            urinew = uri;
                            Log.d("Mymessage", uri.toString());

                            initialize();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress=(100*taskSnapshot.getBytesTransferred())/(taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int)progress);
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });



        }
    }



    /*PERMISSION LOGIC*/
    void startPersmissionsLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Check If Permissions are already granted, otherwise show Ask Permission Dialog

            if (checkPermission()) {

                Toast.makeText(this, "All Permissions Already Granted", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                requestPermission();
            }

        }
    }


    public boolean checkPermission() {

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;

        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean result4 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean result5 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        boolean result6 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2 && result3 && result4 && result5 && result6;

    }


    public void requestPermission() {

        //Show ASK FOR PERSMISSION DIALOG (passing array of permissions that u want to ask)

        ActivityCompat.requestPermissions(this,

                new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 1);

    }


    // After User Selects Desired Permissions, thid method is automatically called

    // It has request code, permissions array and corresponding grantresults array

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 1) {

            if (grantResults.length > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "All PERMISSON GRANTED", Toast.LENGTH_SHORT).show();
                }


                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
                }


                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();

                }

                if (grantResults[4] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Read Contacts Permission Granted", Toast.LENGTH_SHORT).show();

                }
                if (grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Read PHONE STATE Permission Granted", Toast.LENGTH_SHORT).show();

                }


                if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED && grantResults[2] == PackageManager.PERMISSION_DENIED && grantResults[3] == PackageManager.PERMISSION_DENIED && grantResults[4] == PackageManager.PERMISSION_DENIED && grantResults[5] == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(this, "All Permission Denied", Toast.LENGTH_SHORT).show();

                }

            }

        }


    }



    /*PERMISSION LOGIC ENDS HERE*/


    private void initialize() {

        fullName = findViewById(R.id.signName);
        emailId = findViewById(R.id.signEmail);

        status = findViewById(R.id.signStatus);
        selectImageCamera = findViewById(R.id.signSelectImageCamera);
        selectImageGallery = findViewById(R.id.signSelectImageGallery);

        if (fullName.getText().toString().equals("")) {
            Toast.makeText(this, "FULL NAME FIELD EMPTY", Toast.LENGTH_SHORT).show();

        } else if (emailId.getText().toString().equals("")) {
            Toast.makeText(this, "EMAIL FIELD EMPTY", Toast.LENGTH_SHORT).show();

        } else if (status.getText().toString().equals("")) {
            Toast.makeText(this, "STATUS FIELD EMPTY", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(),"one",Toast.LENGTH_SHORT).show();
            databaseEntry();

        }
    }

    public void databaseEntry() {

        /*STORAGE OF ITEMS*/
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", null);

        String a = fullName.getText().toString().trim();
        String b = emailId.getText().toString().trim();
        String c = status.getText().toString().trim();
if(fileuri != null)
{

    Toast.makeText(getApplicationContext(),"one 1",Toast.LENGTH_SHORT).show();
    String d = f.getName();

    String abc = urinew.toString();
    Toast.makeText(this, "" + abc, Toast.LENGTH_SHORT).show();
    Log.d("MyMessage", a + "----" + b + "-------" + c + "-------" + d + "--------");
    whatsappDataEntry w1 = new whatsappDataEntry(mobile, a, b, d, c, abc);

    mainref.child(mobile).setValue(w1);
    Toast.makeText(this, "DATA ENTERED!!!", Toast.LENGTH_LONG).show();


    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("email", b);
    editor.putString("fullName", a);
    editor.putString("status", c);
    editor.putString("photoPath", abc);

    editor.commit();

    Intent intent = new Intent(this, whatsAppTabs.class);
    startActivity(intent);

}
else{

    Toast.makeText(getApplicationContext(),"two",Toast.LENGTH_SHORT).show();
    String pic = "https://firebasestorage.googleapis.com/v0/b/whatsapp-146bb.appspot.com/o/mobile-organizers.png?alt=media&token=ecad4329-7f2d-4ae3-b0b8-7309c6e56c89";
    whatsappDataEntry w1 = new whatsappDataEntry(mobile, a, b, "one.jpg", c, pic);
    mainref.child(mobile).setValue(w1);
    Toast.makeText(this, "DATA ENTERED!!!", Toast.LENGTH_LONG).show();
    Intent intent = new Intent(this, whatsAppTabs.class);
    startActivity(intent);
}
    }

    public void selectImageGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 90);

    }

    public void selectImageCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 80);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backintent) {
            //90:Gallery and 80:Camera
        super.onActivityResult(requestCode, resultCode, backintent);
     if (requestCode == 80 && resultCode == RESULT_OK) {

                bmp = (Bitmap) (backintent.getExtras().get("data"));
                setImage.setImageBitmap(bmp);
                 fileuri= getImageUri(getApplicationContext(), bmp);
                Log.d("camera", getPath(fileuri));

            }
         else if (requestCode == 90 && resultCode == RESULT_OK && backintent != null && backintent.getData() != null) {
            fileuri = backintent.getData();
            setImage.setImageURI(fileuri);
            Log.d("gallery", getPath(fileuri));

        }
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
