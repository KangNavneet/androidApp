package com.example.whatsapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.whatsapp.contactFragment.firebaseList;


/**
 * A simple {@link Fragment} subclass.
 */
public class statusFragment extends Fragment {
    DatabaseReference mainref;
    ValueEventListener vel;
    ListView storyList;
    ArrayList<storyClass> al= new ArrayList<>();
    ArrayList<contactClass> c1=new ArrayList<>();

    Boolean flag = false;
    myadap myad;

    Button myStatus;
    public statusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("qwerty","oncreate call");
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        myad = new myadap();

        storyList = v.findViewById(R.id.storyList);
        return v;
    }



    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("qwerty","yes");
            myad = new myadap();
//            storyList = getActivity().findViewById(R.id.storyList);
            storyList.setAdapter(myad);
            myad.notifyDataSetChanged();

            getDataFromFirebase();
            myStatus=getActivity().findViewById(R.id.myStatus);
            myStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),myStatus.class);
                    startActivity(intent);
                }
            });




        }
        else{
//
            Log.d("qwerty","NO");
        }
    }

    private void getDataFromFirebase() {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        for (int i = 0; i < globalDataContacts.al.size(); i++) {
            System.out.println("GLOBAL CONTACT STORY:"+globalDataContacts.al.get(i));
        }


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



        mainref= firebaseDatabase.getReference("Whatsapp");
        mainref.keepSynced(true);

        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
        String mobileNo = sharedPreferences.getString("mobile", null);

            for (contactClass con : globalDataContacts.al)
            {
                globalContactStory.contactStoryArray.clear();
                System.out.println("Contacts:"+con.getMobileNo());
               // System.out.println("globalData Story:"+globalDataContacts.al);
                Query q1 = firebaseDatabase.getReference("Story").orderByChild("post_by").equalTo(con.getMobileNo());



                GloblaClass.globalstorieslist.clear();
                    q1.addValueEventListener(new ValueEventListener() {
                        ArrayList<storyClass> contactsStory = new ArrayList<>();
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            contactsStory.clear();
                            Boolean flag=true;


                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                storyClass s1 = (storyClass) ds.getValue(storyClass.class);
                                GloblaClass.globalstorieslist.add(s1);
                                System.out.println("Story:"+s1.post_by);
                                contactsStory.clear();
                                Date date = new Date();
                                Long time = date.getTime();
                                System.out.println("CHECK THE GLOBAL CONTACT ARRAY");
                                System.out.println("Size of globalContactStory:"+globalContactStory.contactStoryArray.size() );

                                for (int i = 0; i <globalContactStory.contactStoryArray.size() ; i++) {
                                    System.out.println(globalContactStory.contactStoryArray.get(i));
                                    if(s1.post_by.equals(globalContactStory.contactStoryArray.get(i).post_by) && s1.date_of_expiry>time)
                                    {
                                        System.out.println("BEFORE REMOVAL:"+globalContactStory.contactStoryArray.size());
                                        System.out.println("Already Exists!!!");
                                        globalContactStory.contactStoryArray.remove(globalContactStory.contactStoryArray.get(i));
                                        System.out.println("AFTER REMOVAL:"+globalContactStory.contactStoryArray.size());
                                        contactsStory.add(s1);
                                        System.out.println("contactsStory:"+contactsStory.size());
                                        System.out.println("GLOBAL contactsStory:"+globalContactStory.contactStoryArray.size());
                                        globalContactStory.contactStoryArray.addAll(contactsStory);
                                        flag=false;
                                        break;
                                    }
                                }

                                if (s1.date_of_expiry > time && flag)
                                {
                                    contactsStory.add(s1);
                                    System.out.println("contactsStory");
                                    globalContactStory.contactStoryArray.addAll(contactsStory);


                                    System.out.println(contactsStory);


                                }

                            }



                            myad.notifyDataSetChanged();

                            System.out.println("globalContact Story:" + globalContactStory.contactStoryArray);

                            System.out.println("*************START**************");

                            for (int i = 0; i < globalContactStory.contactStoryArray.size(); i++) {
                                System.out.print(globalContactStory.contactStoryArray.get(i).attach_path + "::");
                                System.out.print(globalContactStory.contactStoryArray.get(i).caption + "::");
                                System.out.print(globalContactStory.contactStoryArray.get(i).post_by + "::");
                                System.out.print(globalContactStory.contactStoryArray.get(i).typeOfFile + "::");
                            }
                            System.out.println("************END***************");

                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });



                }
            }


    @Override
    public void onStart() {

        super.onStart();
        myad = new myadap();

        if (flag == false)   //Some logics are to be fired only once
        {

            storyList = getActivity().findViewById(R.id.storyList);

            storyList.setAdapter(myad);
            myad.notifyDataSetChanged();
            flag = true;
            Log.d("qwerty", "on start called");
        }
        else{
            Log.d("qwerty", "on start called NO");
        }

        myad.notifyDataSetChanged();

        storyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final storyClass story = globalContactStory.contactStoryArray.get(position);
                Intent intent=new Intent(getActivity(),statusActivity.class);
                intent.putExtra("statusMob",story.post_by);
                startActivity(intent);



            }
        });



    }







    // Inner Class
    class myadap extends BaseAdapter {

        @Override
        public int getCount() {
            return globalContactStory.contactStoryArray.size();
        }

        @Override
        public Object getItem(int i) {
            return globalContactStory.contactStoryArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            // Inflate Single Row
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
            convertView = layoutInflater.inflate(R.layout.status_story_list, parent, false);
            //LinearLayout layout=new LinearLayout(getActivity());


            LinearLayout linearLayout=convertView.findViewById(R.id.linearLayout);
            // Refer Views in Single Row
            TextView storyName = convertView.findViewById(R.id.storyName);
            TextView storyDate = convertView.findViewById(R.id.storyDate);
            ImageView storyPhoto = convertView.findViewById(R.id.storyPhoto);

            ProgressBar pbar;




            // Pick ith object from list
            storyClass story =globalContactStory.contactStoryArray.get(i);



           storyDate.setText(story.caption);


            /*DYNAMIC PROGRESSBAR*/
            //ProgressBar progressStories=getActivity().findViewById(R.id.progressStories);

            /*DYNAMIC PROGRESSBAR ENDS HERE*/


            int index=findIndex(globalDataContacts.al,story.post_by);
            storyName.setText(globalDataContacts.al.get(index).fullName);

            Picasso.get().load(globalDataContacts.al.get(index).photopath).into(storyPhoto);
            /*Set Progress BAr Programatically*/

            //ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleSmall);

            /*Set Progress Bar Programatically Ends Here*/


            return convertView;
        }
    }

    private int findIndex(ArrayList<contactClass> al, String post_by) {

        int index=0;

        for(int i=0;i<globalDataContacts.al.size();i++)
        {
            System.out.println("post_by");
            System.out.println(post_by);
            System.out.println("globalDataContacts.al.get(i)");
            System.out.println(globalDataContacts.al.get(i));


            if(globalDataContacts.al.get(i).mobileNo.equals(post_by))
            {
                index=i;
                break;
            }

        }
        return index;
    }


}



