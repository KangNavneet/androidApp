package com.example.whatsapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatsFragment extends Fragment {


    ArrayList<Messages> usrOth=new ArrayList<Messages>();
    ArrayList<Messages> othUr=new ArrayList<Messages>();
    ArrayList<Messages> commonUsrOth=new ArrayList<Messages>();
    ArrayList<chatList> chat1=new ArrayList<chatList>();
    boolean flag =false;
    ListView chatlistview;
    DatabaseReference mainref;
    myadapter myad;
    String mobileNo;


    public chatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d("getContact1:","onStart");

        /*STORAGE OF ITEMS*/
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", null);

        if(flag==false)   //Some logics are to be fired only once
        {
            Log.d("getContact1:","onStart:MEMORY TO ADAPTER AND LISTVIEW");

            chatlistview = (ListView) (getActivity().findViewById(R.id.chatListFragment));
            myad = new myadapter();
            chatlistview.setAdapter(myad);
            myad.notifyDataSetChanged();
            flag=true;
            Log.d("xxx", "on start called");
        }
        chatlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                Log.d("getContact1:","onStart:onItemClickListener Called!GOT FULLNAME,PHOTOPATH,MOBILE2");

                String mobile2=chat1.get(position).getMobilename();
                String fullName=chat1.get(position).getDisplayname();
                String photoPath=chat1.get(position).getPhoto();
                Intent intent = new Intent(getActivity(), chatsActivity.class);
                intent.putExtra("mobile2",mobile2+"");
                intent.putExtra("fullName1",fullName+"");
                intent.putExtra("photopath1",photoPath+"");
                startActivity(intent);
            }
        });
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            Log.d("getContact1:","onSetUserVisibleHint:isVisibleToUser");

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            mainref = firebaseDatabase.getReference();
            mainref.keepSynced(true);

            SharedPreferences sharedPreferences =getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
            mobileNo = sharedPreferences.getString("mobile", null);

            Log.d("getContact1:","onSetUserVisibleHint:isVisibleToUser:othUsr.clear()");

            Log.d("getContact1:","GET CONTACTCLASS DATASNAPSHOT:globalDataContacts.a1:Starts.....");
            Log.d("getContact1:","GET QUERY DATASNAPSHOT:MESSAGES(othUsr.add(msg):Starts.....");
            Log.d("getContact1:","GET MESSAGES DATASNAPSHOT:globalDataContacts.a1:Starts.....");


            othUr.clear();
            //other to user list
            for (contactClass con : globalDataContacts.al) {
                Toast.makeText(getContext(), con.getMobileNo() + "---" + mobileNo, Toast.LENGTH_SHORT).show();
                Query query1 = mainref.child("Messages").orderByChild("colForFilter").equalTo(con.getMobileNo()+ "---" + mobileNo);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            Messages msg = (Messages) ds.getValue(Messages.class);
                            othUr.add(msg);
                        }
                        Log.d("abc",othUr.size()+"");
                        UpdateMessageList();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
            }
            Log.d("getContact1:","GET MESSAGES DATASNAPSHOT:MESSAGES:...ENDS HERE");
            Log.d("getContact1:","GET QUERY DATASNAPSHOT:MESSAGES(othUsr.add(msg):ENDS HERE....");
            Log.d("getContact1:","GET CONTACTCLASS DATASNAPSHOT:globalDataContacts.a1...ENDS HERE");

            usrOth.clear();


            Log.d("getContact1:","QUERY 2:GET CONTACTCLASS DATASNAPSHOT:globalDataContacts.a1:Starts.....");
            Log.d("getContact1:","QUERY 2:GET QUERY DATASNAPSHOT:MESSAGES(othUsr.add(msg):Starts.....");
            Log.d("getContact1:","Query 2:GET MESSAGES DATASNAPSHOT:globalDataContacts.a1:Starts.....");
            //user to other list
            for (contactClass con : globalDataContacts.al)
            {
                Query query2 = mainref.child("Messages").orderByChild("colForFilter").equalTo(mobileNo + "---" + con.getMobileNo());
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singlesnapshot : dataSnapshot.getChildren()) {
                            Messages msg = (Messages) singlesnapshot.getValue(Messages.class);
                            usrOth.add(msg);
                            Log.d("abc2",usrOth.size()+"");
                        }
                        UpdateMessageList();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
            }
            Log.d("getContact1:","QUERY 2 :GET MESSAGES DATASNAPSHOT:MESSAGES:...ENDS HERE");
            Log.d("getContact1:","QUERY 2 :GET QUERY DATASNAPSHOT:MESSAGES(usrOth.add(msg):ENDS HERE....");
            Log.d("getContact1:","QUERY 2 :GET CONTACTCLASS DATASNAPSHOT:globalDataContacts.a1...ENDS HERE");
        }
    }


    private void UpdateMessageList()
    {
        Log.d("getContact1", "UPDATED MESSAGE:COMMON LIST CALL");
        commonUsrOth.clear();
        commonUsrOth.addAll(usrOth);
        commonUsrOth.addAll(othUr);
        Collections.sort(commonUsrOth, new Comparator<Messages>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(Messages o1, Messages o2) {
                return Long.compare(o2.msgId, o1.msgId);
            }
        });
        chat1.clear();
        Log.d("getContact1", "CLEAR CHAT ARRAYLIST OF CHAT CLASS:WHICH ONE SEES ON CHAT FRAGMENT");
        Log.d("getContact1", "***********METHOD TO GET CHAT LIST ON CHAT FRAGMENT***************");
        Log.d("getContact1", "USE globalDataContacts---ARRAYLIST");
        Log.d("getContact1", "ON GETTING FIRST CONTACT");
        Log.d("getContact1", "GET MESSAGES DATASNAPNOT");
        Log.d("getContact1", "AFTER GETTING MESSAGES DATA SNAPSHOT MATCH ::: msgFrom and get getails of the same from contact and getMobileNo ");
        Log.d("getContact1", "AFTER GETTING MESSAGES DATA SNAPSHOT MATCH ::: msgFrom and get getails of the same from contact and getMobileNo ");



        /*GET TWO DATASNAPSHOT SIMULTANEOUSLY*/
        for (contactClass contact : globalDataContacts.al)
        {
            boolean alreadyadded = false;
            for (Messages msg : commonUsrOth)
            {
                if ((msg.msgFrom.equals(contact.getMobileNo()) || msg.msgTo.equals(contact.getMobileNo())) && alreadyadded==false) {
                    chat1.add(new chatList(contact.getMobileNo(), contact.getFullName(),contact.getPhotopath(), msg.getMsgText(),msg.getMsgId(),msg.getMsgType()));
                    alreadyadded = true;
                    break;
                }
                System.out.println("chat1:"+contact.getMobileNo()+"---"+contact.getFullName()+"---"+msg.getMsgText()+"---"+msg.getMsgType());

            }
            System.out.println("*******************************");
        }
        /*GET TWO DATASNAPSHOT SIMULTANEOUSLY ENDS HERE*/

        Collections.sort(chat1, new Comparator<chatList>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(chatList o1, chatList o2) {
                return Long.compare(o2.datetime,o1.datetime);
            }
        });
        myad.notifyDataSetChanged();
    }


    private class myadapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return chat1.size();
        }

        @Override
        public Object getItem(int position) {
            return chat1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position*10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                // Inflate Single Row
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
            convertView = layoutInflater.inflate(R.layout.chat_layout_fragment, parent, false);
            chatList conlist = chat1.get(position);
            ImageView photoOth;
            TextView nameOth, msgTextOth;
            ImageView msgPhotoOth;
            VideoView msgVideoOth;
            TextView msgdate;

            photoOth = convertView.findViewById(R.id.photoOth);
            msgPhotoOth = convertView.findViewById(R.id.msgPhotoOth);
            msgTextOth = convertView.findViewById(R.id.msgTextOth);
            msgVideoOth = convertView.findViewById(R.id.msgVideoOth);
            nameOth = convertView.findViewById(R.id.nameOth);
            msgdate = convertView.findViewById(R.id.msgdate);
            nameOth.setText(conlist.getDisplayname());
            msgdate.setText(conlist.getDatetime()+"");
            msgTextOth.setText(conlist.getLastmessage()+"");
            System.out.println(conlist.msgType);
            if(conlist.getLastmessage().equals(""))
            {
                msgPhotoOth.setImageResource(R.drawable.media);
                msgPhotoOth.setVisibility(View.VISIBLE);
                msgTextOth.setVisibility(View.GONE);
            }
            else
            {
                msgTextOth.setText(conlist.getLastmessage()+"");

            }

            Picasso.get().load(Uri.parse(conlist.getPhoto())).resize(250, 250).into(photoOth);
//            Log.d("photo", chatObj.r1.getPhotoPath());

            System.out.println("ENTRY BEFORE TEXT");
            // Toast.makeText(getApplicationContext(), ":"+msgobj.msgText, Toast.LENGTH_SHORT).show();
//
//            if(msgobj.getMsgType())
//
//            switch (conlist.getMsgType()) {
//
//
//                case "Text":
//                    System.out.println("ENTRY INTO TEXT");
//                    msgTextOth.setText(chatObj.r1.getMsgText()+"");
//                    break;
//
//                case "Photo":
//
//                    Picasso.get().load(Uri.parse(chatObj.r1.getMsgAttach())).resize(250, 250).into(msgPhotoOth);
//
//                    break;
//
//                case "Video":
//                    System.out.println("Video");
//                    break;
//                case "Audio":
//                    System.out.println("Audio");
//                    break;
//
//            }
            return convertView;


        }
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("my","on stop called");
        flag=false;

    }
}
