package com.example.whatsapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
public class chatsActivity extends AppCompatActivity {
    Toolbar toolbar1;
    ArrayList<Messages> al = new ArrayList<>();
    ListView chatList;
    EditText sendMsg;
    ImageView sendChat;
    ProgressDialog pd;
    String localpath;

    String mobile2, mobile;
        DatabaseReference mainref;
        StorageReference mainrefStore, stref2;
        FirebaseStorage firebaseStorage;
    Uri urinew;
    ProgressBar mProgressBar;
    String fileExtensionGuess = "Text";

    ImageView chatImage;
    Query q1;
    Query q2;
    ArrayList<Messages> listUsrOth = new ArrayList<>();
    ArrayList<Messages> listOthUsr = new ArrayList<>();
    ArrayList<Messages> listCommonOthUsr = new ArrayList<>();
    String usrOth, othUsr;
    myadapter myad = new myadapter();
    File f = new File("/mnt/");
    ImageView msgImageView;
    TextView tv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        mProgressBar = findViewById(R.id.chatProgress);

        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait");
        pd.setMessage("Sending Data To Cloud");
        pd.setIcon(R.drawable.ic_screen_share_black_24dp);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        FirebaseStorage st = FirebaseStorage.getInstance();

        stref2 = st.getReference();
        chatList = findViewById(R.id.chatList);
        sendChat = findViewById(R.id.sendChat);
        sendMsg = findViewById(R.id.sendMsg);
        chatImage = findViewById(R.id.chatImage);
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        mobile = sharedPreferences.getString("mobile", null);

        Intent intent=getIntent();
        mobile2 = intent.getStringExtra("mobile2");
        String name = intent.getStringExtra("fullName1");
        String photopath1 = intent.getStringExtra("photopath1");
        Toast.makeText(this, mobile2+"...."+name+"...", Toast.LENGTH_LONG).show();
        Picasso.get().load(photopath1).resize(250, 250).into(chatImage);

//        chatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//       chatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        chatList.setStackFromBottom(true);


        toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle(name + "\n" + mobile2);
        toolbar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(chatsActivity.this,"I am clicked",Toast.LENGTH_SHORT).show();
//               finish();
                Intent intent=new Intent(getApplicationContext(),showStatus.class);
                intent.putExtra("mobile3",mobile2);
                startActivity(intent);
            }
        });
        // Use Toolbar as ActionBar
        // Use Toolbar as ActionBar
        setSupportActionBar(toolbar1);


        getDataFromFirebase();


        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendMsg.getText().toString();
                if (msg.equals("")) {
                    Toast.makeText(getApplicationContext(), "ENTER MESSAGE", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Uri localuri = Uri.EMPTY;
                    String localmsg = "Text";
                    sendChatToFirebase(localuri, localmsg);

                    //chatListView();
                }
            }
        });


        chatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                if (listCommonOthUsr.get(position).getMsgFrom().equals(mobile2)) {
                    Log.d("hello", listCommonOthUsr.get(position).getMsgFrom().equals(mobile2) + "");
                    return false;
                }
                final Messages msgobj1 = listCommonOthUsr.get(position);
                PopupMenu popup = new PopupMenu(chatsActivity.this, view);
                if (msgobj1.getMsgType().equals("Text")) {

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_item:
                                    Log.d("item", " edit clicked");
                                    EditMessage(msgobj1, view);
                                    return true;
                                case R.id.delete_item:
                                    Log.d("item", " delete clicked");
                                    DeleteMessage(msgobj1, view);
                                    return true;
                                case R.id.undo_item:
                                    Log.d("item", " undo clicked");
                                    UndeleteMessage(msgobj1, view);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.inflate(R.menu.popup_menu);
                    popup.show();

                } else {
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_item1:
                                    DeleteMessage(msgobj1, view);


                                case R.id.undo_item1:
                                    UndeleteMessage(msgobj1, view);
                                    return true;
                            }
                            return false;
                        }
                    });
                    popup.inflate(R.menu.popupmenu1);
                    popup.show();
                }


                return false;
            }
        });

    }

    /*STORE DATA IN STORAGE FIREBASE */


    public void store(Uri localuri, final String localmsg) {
        Log.d("hello", localmsg + ".." + localuri);
        pd.show();
        FirebaseApp.initializeApp(this);
        firebaseStorage = FirebaseStorage.getInstance();
        mainrefStore = firebaseStorage.getReference();
        f = new File(getPath(localuri));


        Log.d("hello", f.getName());
        StorageReference sr3 = mainrefStore.child(f.getName());

        UploadTask uploadTask = sr3.putFile((Uri.fromFile(f)));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "FILE UPLOADED", Toast.LENGTH_LONG).show();
                pd.dismiss();
                Log.d("hello", taskSnapshot.toString());
                mainrefStore.child(f.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(), "Url Downloaded Successfully" + uri, Toast.LENGTH_SHORT).show();
                        urinew = uri;

                        Log.d("hello", uri.toString());
                        sendChatToFirebase(uri, localmsg);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(chatsActivity.this, "", Toast.LENGTH_SHORT).show();
                        Log.d("hello", e.getMessage());
                    }
                });
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100 * taskSnapshot.getBytesTransferred()) / (taskSnapshot.getTotalByteCount());
                pd.setProgress((int) progress);

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /*STORE DATA IN STORAGE ENDS HERE */


    /*MENU INFLATE*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return true;
    }

    // Logic when a menu item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.m1:
                Toast.makeText(this, "One Clicked", Toast.LENGTH_SHORT).show();
                getPhoto();
                break;

            case R.id.m2:
                Toast.makeText(this, "Two Clicked", Toast.LENGTH_SHORT).show();
                getVideo();
                break;

            case R.id.m3:
                Toast.makeText(this, "Three Clicked", Toast.LENGTH_SHORT).show();
                getAudio();
                break;

        }

        return true;
    }
    /*MENU INFLATE ENDS HERE*/

    /*MEDIA STORE*/
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backintent) {

        if (requestCode == 80)   // from camera
        {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) (backintent.getExtras().get("data"));
                //imv1.setImageBitmap(bmp);
            }
        } else if (requestCode == 90)    // from gallery
        {
            if (resultCode == RESULT_OK) {
                Uri fileuri = backintent.getData();
                Toast.makeText(this, "GALLERY URI TOAST:" + fileuri, Toast.LENGTH_LONG).show();
                String msgtypelocal = "Photo";
                store(fileuri, msgtypelocal);
                //imv1.setImageURI(fileuri);
            }
        } else if (requestCode == 100)    // from VIDEO
        {
            if (resultCode == RESULT_OK) {
                Uri fileuri = backintent.getData();
                Toast.makeText(this, "VIDEO URI TOAST:" + fileuri, Toast.LENGTH_LONG).show();
                String msgtypelocal = "Video";
                store(fileuri, msgtypelocal);
                //imv1.setImageURI(fileuri);
            }
        } else if (requestCode == 110)    // from AUDIO
        {
            if (resultCode == RESULT_OK) {
                Uri fileuri = backintent.getData();
                Toast.makeText(this, "AUDIO URI TOAST:" + fileuri, Toast.LENGTH_LONG).show();
                String msgtypelocal = "Audio";
                store(fileuri, msgtypelocal);
                //imv1.setImageURI(fileuri);
            }
        }

    }

    public void getPhoto() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        fileExtensionGuess = "Photo";

        startActivityForResult(intent, 90);

    }

    public void getVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        fileExtensionGuess = "Video";
        startActivityForResult(intent, 100);
    }

    public void getAudio() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("audio/*");
        fileExtensionGuess = "Audio";

        startActivityForResult(intent, 110);
    }

    /*MEDIA STORE ENDS HERE*/


    private void getDataFromFirebase() {
        /*INITIALIZE FIREBASE*/
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        /*Get DATA FROM FIREBASE*/
        usrOth = mobile + "---" + mobile2;
        othUsr = mobile2 + "---" + mobile;

        mainref = firebaseDatabase.getReference("Messages");
        mainref.keepSynced(true);
        q1 = firebaseDatabase.getReference("Messages").orderByChild("colForFilter").equalTo(usrOth);
        q2 = firebaseDatabase.getReference("Messages").orderByChild("colForFilter").equalTo(othUsr);

        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATASNAPSHOT VIEW ", dataSnapshot.toString());


                if (listOthUsr != null) {

                    listOthUsr.clear();

                } else {

                    listOthUsr = new ArrayList<>();
                }
                int i = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Messages chatMsg = ds.getValue(Messages.class);
                    System.out.println("Messages:" + chatMsg);
                    System.out.println("Messages---MSG TO" + chatMsg.getMsgTo() + " --MSGDATETIME:" + chatMsg.getMsgDateTime() + " ----MSG FROM::" + chatMsg.getMsgFrom());

                    if (!chatMsg.getMsgDel()) {
                        listOthUsr.add(chatMsg);
                        Log.d("Hello2", listOthUsr.get(i).getMsgId() + ".........." + listOthUsr.get(i).getMsgDel());


                    } else {

                        if (chatMsg.msgText == "") {
                            msgImageView.getLayoutParams().height = 0;
                            msgImageView.getLayoutParams().width = 0;
                            Log.d("MYMESSAGE", "VIDEO12");
                            chatMsg.setMsgText("MESSAGE DELETED!");
                            listOthUsr.add(chatMsg);
                        }
                        chatMsg.setMsgText("MESSAGE DELETED!");
                        listOthUsr.add(chatMsg);
                    }
                    i++;
                }

                Log.d("OTHUSR", listOthUsr.toString());

                q2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("DATASNAPSHOT VIEW 1", dataSnapshot.toString());


                        if (listUsrOth != null) {
                            listUsrOth.clear();
                        } else {
                            listUsrOth = new ArrayList<>();

                        }
                        int i = 0;

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Messages chatMsg = ds.getValue(Messages.class);
                            System.out.println("Messages:" + chatMsg);
                            System.out.println("Messages---MSG TO" + chatMsg.getMsgTo() + " --MSGDATETIME:" + chatMsg.getMsgDateTime() + " ----MSG FROM::" + chatMsg.getMsgFrom());
                            if (!chatMsg.getMsgDel()) {
                                listUsrOth.add(chatMsg);
                                Log.d("Hello3", listUsrOth.get(i).getMsgId() + ".........." + listUsrOth.get(i).getMsgDel());

                            } else {
                                if (chatMsg.msgText == "") {
                                    msgImageView.getLayoutParams().height = 0;
                                    msgImageView.getLayoutParams().width = 0;
                                    Log.d("MYMESSAGE", "Viddeo");
//                            Log.d("Hello4",listUsrOth.get(i).getMsgId()+".........."+listUsrOth.get(i).getMsgDel());
                                    chatMsg.setMsgText("MESSAGE DELETED!");
                                    listUsrOth.add(chatMsg);
                                }

//
                                chatMsg.setMsgText("MESSAGE DELETED!");
                                listUsrOth.add(chatMsg);
                            }
                            i++;
                        }
                        commonList();
                        chatListView();
                        Log.d("USROTH", listUsrOth.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


        /*GET DATA FROM FIREBASE ENDS HERE*/



    }

    public void commonList() {
        listCommonOthUsr.clear();
        Log.d("COMMONLIST", listCommonOthUsr.toString());
        Log.d("ListOTH", listOthUsr.toString());
        Log.d("ListUsr", listUsrOth.toString());
        listCommonOthUsr.addAll(listUsrOth);
        listCommonOthUsr.addAll(listOthUsr);


        Collections.sort(listCommonOthUsr, new Comparator<Messages>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(Messages o1, Messages o2) {
                return Long.compare(o1.msgId, o2.msgId);
            }
        });

        for (Messages msg : listCommonOthUsr) {
            //  Log.d("hello", msg.toString());
        }
        myad.notifyDataSetChanged();


    }


    public void sendChatToFirebase(Uri localuri, String localmsg) {
        Log.d("msg", localuri + "......" + localmsg);
        System.out.println("Send Chat To Firebase");
        /*DATA OF THE USER*/
        Date msgDateTime = new Date();
        String msgDateTime1 = msgDateTime.toString();
        String msgText = sendMsg.getText().toString();
        Long getMsgId = msgDateTime.getTime();

        String msgFrom = mobile;
        String msgTo = mobile2;
        String colForFilter = msgFrom + "---" + msgTo;
        String attachFile;
        if (localuri != null) {
            attachFile = localuri.toString();

        } else {
            attachFile = "EMPTY";

        }
        String offlinePath = "offlinePath";
        String offlinePathOth = "offlinePathOth";
        String msgType = localmsg;
        Boolean msgDel = false;
        /*DATA OF THE USER ENDS HERE*/

        /*NOW SEND DATA TO FIREBASE*/

        Messages m1 = new Messages(getMsgId, msgTo, msgFrom, colForFilter, msgText, attachFile, offlinePath, offlinePathOth, msgDateTime1, msgType, msgDel);
        System.out.println(m1);

        mainref.child(getMsgId.toString()).setValue(m1);

        Toast.makeText(this, "DATA ENTERED!!!", Toast.LENGTH_LONG).show();
        attachFile = "EMPTY";
        msgType = "";
        sendMsg.setText("");
        getDataFromFirebase();

    }

    public void chatListView() {
        /* SEND DATA TO FIREBASE*/
        /*SEND CHAT LIST*/
        chatList.setAdapter(myad);
    }

    // Inner Class
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listCommonOthUsr.size();
        }

        @Override
        public Object getItem(int i) {
            return listCommonOthUsr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            // Inflate Single Row
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            convertView = layoutInflater.inflate(R.layout.chatbubble, parent, false);

            // Refer Views in Single Row
//            TextView msgFrom = convertView.findViewById(R.id.msgFrom);
//            TextView msgTo = convertView.
//            findViewById(R.id.msgTo);
//            TextView msgText = convertView.findViewById(R.id.msgText);
//            TextView msgDateTime = convertView.findViewById(R.id.msgDateTime);
//
//            // Pick ith object from list
//            Messages msgClass = listCommonOthUsr.get(i);
//            msgFrom.setText(msgClass.getMsgTo());
//           // msgFrom.setText(msgClass.getMsgDateTime());
//            //msgFrom.setText(msgClass.getColForFilter());
//            //msgTo.setText(msgClass.getMsgTo());
//            msgText.setText(msgClass.getMsgText());
//            msgDateTime.setText(msgClass.getMsgDateTime());


            final Messages msgobj = listCommonOthUsr.get(i);
            tv_message = convertView.findViewById(R.id.tv_message);
            msgImageView = convertView.findViewById(R.id.message_photo);
            tv_message.setText(msgobj.getMsgText() + "");
            TextView tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout);
            LinearLayout parent_layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout_parent);

            Date dt = new Date(msgobj.getMsgDateTime());
            Date currentdate = new Date();
            String datetoshow = "";

            if (msgobj != null)
                if (dt.getDay() == currentdate.getDay() && dt.getMonth() == currentdate.getMonth()) {
                    datetoshow = dt.getHours() + ":" + dt.getMinutes();
                } else {
                    String df = DateFormat.getDateInstance().format(dt) + " " + DateFormat.getTimeInstance().format(dt);
                    datetoshow = df;
                }
            tv_datetime.setText(datetoshow);

            System.out.println("ENTRY BEFORE TEXT");
            // Toast.makeText(getApplicationContext(), ":"+msgobj.msgText, Toast.LENGTH_SHORT).show();
//
//            if(msgobj.getMsgType())

            switch (msgobj.getMsgType()) {


                case "Text":
                    System.out.println("ENTRY INTO TEXT");

                    msgImageView.getLayoutParams().height = 0;
                    msgImageView.getLayoutParams().width = 0;
                    Log.d("MYMESSAGE", "TEXT");

                    tv_message.setVisibility(View.VISIBLE);
                    break;

                case "Photo":
                    if (msgobj.getMsgDel()) {
                        msgImageView.getLayoutParams().height = 0;
                        msgImageView.getLayoutParams().width = 0;
                        msgImageView.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);

                        String delMsg = "Message Deleted";
                        tv_message.setText(delMsg);
                        tv_datetime.setText(delMsg);

                    } else {
                        msgImageView.getLayoutParams().height = 500;
                        msgImageView.getLayoutParams().width = 500;
                        Log.d("MYMESSAGE", "PHOTO:" + msgobj.attchPath);
                        //Toast.makeText(getApplicationContext(), "PHOTO ATTACH:"+msgobj.attchPath, Toast.LENGTH_SHORT).show();

                        Picasso.get().load(msgobj.attchPath).resize(100, 100).into(msgImageView);
                        msgImageView.setVisibility(View.VISIBLE);
                    }

                    msgImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String filename = getFileName(Uri.parse(msgobj.attchPath));

                            File fn = new File(Environment.getExternalStorageDirectory() + "/mywhatsapp/" + filename);

                            if (fn.exists()) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+ "/mywhatsapp/"+filename);
//                                intent.setDataAndType(uri, "audio/*");
//                                Log.d("filename",uri.toString());

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                mainref = firebaseDatabase.getReference("Messages");
                                if (!msgobj.getMsgFrom().equals(mobile)) {
                                    String offlinePathOth = fn.getPath();
                                    mainref.child(msgobj.msgId.toString()).child("offlinePathOth").setValue(offlinePathOth);
                                    Log.d("done", mobile);

                                    Log.d("done", msgobj.getMsgId().toString());

                                    SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
                                    Intent i1 = new Intent(chatsActivity.this, fullImage.class);
                                    i1.putExtra("fullImage", msgobj.offlinePathOth);
                                    startActivity(i1);


                                } else {
                                    Log.d("done", mobile);
                                    Log.d("done", msgobj.getMsgTo());
                                    String offlinePath = fn.getPath();
                                    mainref.child(msgobj.msgId.toString()).child("offlinePath").setValue(offlinePath);
                                    Log.d("done", msgobj.getMsgId().toString());
                                    Intent i1 = new Intent(chatsActivity.this, fullImage.class);
                                    i1.putExtra("fullImage",msgobj.offlinePath);
                                    startActivity(i1);



                                }

                            } else {

                                downloadFileFromCloud(msgobj);
                                Intent i1 = new Intent(chatsActivity.this, fullImage.class);
                                i1.putExtra("fullImage", msgobj.attchPath);
                                startActivity(i1);


                            }



                        }
                    });
                    tv_message.setVisibility(View.GONE);
//                    tv_message.setText(msgobj.getMsgText());


                    break;

                case "Video":

                    if (msgobj.getMsgDel()) {
                        msgImageView.getLayoutParams().height = 0;
                        msgImageView.getLayoutParams().width = 0;
                        msgImageView.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);
                        String delMsg = "Message Deleted";
                        tv_message.setText(delMsg);
                        tv_datetime.setText(delMsg);
                    } else {
                        msgImageView.getLayoutParams().height = 500;
                        msgImageView.getLayoutParams().width = 500;
                        Log.d("MYMESSAGE", "PHOTO:" + msgobj.attchPath);
                        //   Toast.makeText(getApplicationContext(), "PHOTO ATTACH:"+msgobj.attchPath, Toast.LENGTH_SHORT).show();
                        msgImageView.setImageResource(R.drawable.video);
                        msgImageView.setVisibility(View.VISIBLE);
                    }

//                    Picasso.get().load(R.drawable.ic_mic_black_24dp).into(msgImageView);
                    msgImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            downloadtask2(msgobj.attchPath);
//
//                            SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("audioVideo1", msgobj.attchPath);
//                            editor.commit();
//
//                            Intent videoFile = new Intent(chatsActivity.this, videoFile.class);
//                            //startActivity(videoFile);

                            String filename = getFileName(Uri.parse(msgobj.attchPath));

                            File fn = new File(Environment.getExternalStorageDirectory() + "/mywhatsapp/" + filename);
                            if (fn.exists()) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+ "/mywhatsapp/"+filename);
//                                intent.setDataAndType(uri, "audio/*");
//                                Log.d("filename",uri.toString());

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                mainref = firebaseDatabase.getReference("Messages");
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                if (!msgobj.getMsgFrom().equals(mobile)) {
                                    String offlinePathOth = fn.getPath();
                                    mainref.child(msgobj.msgId.toString()).child("offlinePathOth").setValue(offlinePathOth);
                                    Log.d("done", mobile);

                                    Log.d("done", msgobj.getMsgId().toString());


                                    Log.d("donevideo", fn.getPath());

                                    intent.setDataAndType(Uri.parse(msgobj.getOfflinePathOth()), "video/*");

                                } else {
                                    Log.d("done", mobile);
                                    Log.d("done", msgobj.getMsgTo());


                                    String offlinePath = fn.getPath();
                                    mainref.child(msgobj.msgId.toString()).child("offlinePath").setValue(offlinePath);
                                    Log.d("done", msgobj.getMsgId().toString());


                                    Log.d("donevideo", fn.getPath());

                                    intent.setDataAndType(Uri.parse(msgobj.getOfflinePath()), "video/*");

                                }

                                startActivity(Intent.createChooser(intent, null));


                            } else {
                                Log.d("donevideo", "Manual Download");
                                downloadFileFromCloud(msgobj);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(msgobj.attchPath), "video/*");
                                startActivity(intent);
                            }
                        }

                    });

                    tv_message.setVisibility(View.GONE);
//                    tv_message.setText(msgobj.getMsgText());

                    break;

                case "Audio":

                    if (msgobj.getMsgDel()) {
                        msgImageView.getLayoutParams().height = 0;
                        msgImageView.getLayoutParams().width = 0;
                        msgImageView.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);
                        String delMsg = "Message Deleted";
                        tv_message.setText(delMsg);
                        tv_datetime.setText(delMsg);

                    } else {
                        msgImageView.getLayoutParams().height = 500;
                        msgImageView.getLayoutParams().width = 500;
                        Log.d("MYMESSAGE", "PHOTO:" + msgobj.attchPath);
                        // Toast.makeText(getApplicationContext(), "PHOTO ATTACH:"+msgobj.attchPath, Toast.LENGTH_SHORT).show();
//                    Picasso.get().load(msgobj.attchPath).into(msgImageView);
                        msgImageView.setImageResource(R.drawable.ic_mic_black_24dp);
                        msgImageView.setVisibility(View.VISIBLE);
                    }

                    msgImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("audioVideo1", msgobj.attchPath);
//                            editor.commit();

//                            Intent audioFile = new Intent(chatsActivity.this, audioFile.class);
                            String filename = getFileName(Uri.parse(msgobj.attchPath));
                            File fn = new File(Environment.getExternalStorageDirectory() + "/mywhatsapp/" + filename);


                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                            mainref = firebaseDatabase.getReference("Messages");
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            if (!msgobj.getMsgFrom().equals(mobile)) {
                                String offlinePathOth = fn.getPath();
                                mainref.child(msgobj.msgId.toString()).child("offlinePathOth").setValue(offlinePathOth);
                                Log.d("done", mobile);

                                Log.d("done", msgobj.getMsgId().toString());


                                Log.d("donevideo", fn.getPath());

                                intent.setDataAndType(Uri.parse(msgobj.getOfflinePathOth()), "video/*");

                            } else {
                                Log.d("done", mobile);
                                Log.d("done", msgobj.getMsgTo());


                                String offlinePath = fn.getPath();
                                mainref.child(msgobj.msgId.toString()).child("offlinePath").setValue(offlinePath);
                                Log.d("done", msgobj.getMsgId().toString());


                                Log.d("donevideo", fn.getPath());

                                intent.setDataAndType(Uri.parse(msgobj.getOfflinePath()), "video/*");

                            }

                            startActivity(Intent.createChooser(intent, null));
                        }
                    });
                    tv_message.setVisibility(View.GONE);
//                    tv_message.setText(msgobj.getMsgText());

                    break;

            }


            if (!msgobj.getMsgTo().equals(mobile)) {
                layout.setBackgroundResource(R.drawable.bubble1);
                parent_layout.setGravity(Gravity.LEFT);

            } else // If not mine then align to left
            {
                layout.setBackgroundResource(R.drawable.bubble2);
                parent_layout.setGravity(Gravity.RIGHT);
            }

            return convertView;
        }
    }


    public void EditMessage(Messages msgobj, View view) {
        /*************************************ALERT DIALOG STARTS HERE*************************/

        Toast.makeText(this, "" + msgobj.getMsgId().toString(), Toast.LENGTH_SHORT).show();
        Intent in = new Intent(this, DialogActivityDemo.class);
        in.putExtra("msgId", msgobj.getMsgId());

        startActivity(in);

/*************************************ALERT DIALOG ENDS HERE*************************/

        getDataFromFirebase();

    }

    public void DeleteMessage(Messages msgobj, View convertView) {
        msgImageView.getLayoutParams().height = 0;
        msgImageView.getLayoutParams().width = 0;
        Log.d("MYMESSAGE", "TEXT");

        tv_message.setVisibility(View.VISIBLE);

        Log.d("Edit", "DELETE MESSAGE:ID" + msgobj.getMsgId() + ":DELETE:" + msgobj.getMsgDel());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Boolean msgDelMsg = true;
        mainref = firebaseDatabase.getReference("Messages");
        Long msgId = msgobj.getMsgId();
        mainref.child(msgId.toString()).child("msgDel").setValue(msgDelMsg);

        getDataFromFirebase();

    }

    public void UndeleteMessage(Messages msgobj, View convertView) {

        Log.d("Edit", "UNDO MESSAGE:ID" + msgobj.getMsgId() + ":DELETE:" + msgobj.getMsgDel());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("Messages");
        Long msgId = msgobj.getMsgId();

        Boolean msgDel = msgobj.getMsgDel();
        if (msgDel) {
            msgDel = false;
            mainref.child(msgId.toString()).child("msgDel").setValue(msgDel);
        }

        getDataFromFirebase();

    }

    private void downloadFileFromCloud(Messages msgObj) {
        new DownloadFileAsync().execute(msgObj.getAttchPath());

    }

    File f1;

    class DownloadFileAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //      showDialog(DIALOG_DOWNLOAD_PROGRESS);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(chatsActivity.this, "Download Started", Toast.LENGTH_SHORT).show();


                }
            });
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            Log.d("xxx", "filename : " + aurl[0]);
            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                Log.d("xxx", "filename : " + lenghtOfFile);
                String filename = getFileName(Uri.parse(aurl[0]));

                Log.d("xxx", "filename12 : " + filename);
                Log.d("xxx", "Lenght of file: " + lenghtOfFile);
                File fn = new File(Environment.getExternalStorageDirectory() + "/mywhatsapp");
                if (!fn.exists()) {
                    fn.mkdir();
                }
                //localpath = "/mnt/sdcard/" + filename;
                localpath = Environment.getExternalStorageDirectory() + "/mywhatsapp" + File.separator + filename;
                //    localpath = "/mnt/sdcard/mywhatsapp/" + File.separator + filename;
                Log.d("xxx", "local path " + localpath);
                f1 = new File(localpath);
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(f1);
                byte data[] = new byte[1024];
                long total = 0;
                while (true) {
                    count = input.read(data);
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                    if (total == lenghtOfFile)
                        break;
                }
                output.flush();
                output.close();
                input.close();
                Log.d("xxx", "local file : " + localpath);
                Log.d("xxx", "completed");
            } catch (Exception e) {
                Log.d("xxx", e.getMessage());
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(chatsActivity.this, "Download Completed!!", Toast.LENGTH_SHORT).show();
                    }
                });


//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//                File file = new File(localpath);
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                startActivity(Intent.createChooser(intent, null));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}