package com.example.whatsapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.util.Config.LOGD;
import static androidx.constraintlayout.widget.Constraints.TAG;
/**
 * A simple {@link Fragment} subclass.
 */
public class contactFragment extends Fragment{
    DatabaseReference mainref;
    ValueEventListener vel;
    static ArrayList<contactClass> firebaseList = new ArrayList<>();
    ListView flv1;
    myadapter myad;
    String phoneNo;
    ListView chatListFragment;
    String mobile;


   static ArrayList<String> phoneList=new ArrayList<>();


    public contactFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment


        View v= inflater.inflate(R.layout.fragment_contact, container, false);
        firebaseList.clear();
        phoneList.clear();



        globalDataContacts.al.clear();
        final ContentResolver cr = getActivity().getContentResolver();
        final Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                        for(int i=0;i<phoneNo.length();i++)
                        {
                            phoneNo=phoneNo.replace(" ","");
                            phoneNo=phoneNo.replace("  ","");
                            phoneNo= phoneNo.replace("-","");
                            phoneNo= phoneNo.replace("(","");
                            phoneNo= phoneNo.replace(")","");

                        }

                    }
                    phoneList.add(phoneNo);
                    System.out.println("*****************PHONELIST********************");
                    System.out.println(phoneList);

                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }


        return v;
    }



    public void onStart() {
        myad  =new myadapter();
        super.onStart();

        flv1=this.getActivity().findViewById(R.id.flv1);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
        mobile = sharedPreferences.getString("mobile", null);


        FirebaseApp.initializeApp(this.getActivity());
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        /*GET DATA FROM FIREBASE DATA SNAPSHOT*/
//      mainref=firebaseDatabase.getReference("student/1");
        mainref=firebaseDatabase.getReference("Contacts");
        mainref.keepSynced(true);

        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("DATASNAPSHOT VIEW",dataSnapshot.toString());


                if(firebaseList != null) {
                    firebaseList.clear();
                    globalDataContacts.al.clear();
                } else {
                    firebaseList= new ArrayList<>();
                }



                for(DataSnapshot ds:dataSnapshot.getChildren()) {

                    contactClass contacts = ds.getValue(contactClass.class);
                    System.out.println("Contacts"+contacts);
                    System.out.println("Contacts.getMobileNo()"+contacts.getMobileNo());
                    for (int i = 0; i < phoneList.size(); i++) {
                        if ((contacts.getMobileNo()).equals(phoneList.get(i)) && !contacts.getMobileNo().equals(mobile)) {

                            firebaseList.add(contacts);
                             globalDataContacts.al.add(contacts);


                        }
                    }
                    myad.notifyDataSetChanged();


                }
//                Log.d("MymessageSnap",listFromFirebase.toString());



            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };


        mainref.addValueEventListener(vel);
        flv1.setAdapter(myad);


        /*GET DATA FROM FIREBASE DATA SNAPSHOT ENDS HERE*/


        flv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mobile2=firebaseList.get(position).mobileNo;
                String fullName=firebaseList.get(position).fullName;
                String photoPath=firebaseList.get(position).photopath;
                Toast.makeText(getActivity(), mobile2 +" ---- "+fullName+" selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), chatsActivity.class);
                intent.putExtra("mobile2",mobile2);
                intent.putExtra("fullName1",fullName);
                intent.putExtra("photopath1",photoPath);
                startActivity(intent);

            }
        });

    }




    @Override
    public void onPause() {


        super.onPause();


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }



    class myadapter extends BaseAdapter
    {


        @Override
        public int getCount() {
            return firebaseList.size();
        }

        @Override
        public Object getItem(int i) {
            return firebaseList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){


            LayoutInflater layoutInflater =LayoutInflater.from(contactFragment.this.getActivity());
            view =layoutInflater.inflate(R.layout.contacts_list_item, viewGroup, false);
            TextView ftv1 = view.findViewById(R.id.tv111);
            TextView ftv2 = view.findViewById(R.id.tv222);
            ImageView imv1 = view.findViewById(R.id.imv111);
            //PICK ith Object from List
            contactClass cl = firebaseList.get(i);
            ftv1.setText(""+cl.fullName);
            ftv2.setText("" + cl.getMobileNo());
            Picasso.get().load(cl.photopath).resize(250, 250).into(imv1);
            return view;
            //Refer Views in Single Row

        }


    }



}
