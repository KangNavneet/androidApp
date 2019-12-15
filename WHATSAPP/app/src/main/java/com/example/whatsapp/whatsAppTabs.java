package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class whatsAppTabs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_tabs);
        Toolbar tb1;
        TabLayout tabLayout;
        ViewPager vp1;
        tb1 = findViewById(R.id.toolbar1);
        setSupportActionBar(tb1);

        tabLayout = findViewById(R.id.tab1);
        tabLayout.addTab(tabLayout.newTab().setText("Page 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Page 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Page 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Page 4"));
        vp1 = findViewById(R.id.vp1);
        FragmentManager fm = getSupportFragmentManager();

        myPagerAdapter pa = new myPagerAdapter(fm);
        vp1.setAdapter(pa);
        // Link Tabs with ViewPager
        tabLayout.setupWithViewPager(vp1);
    }

    class myPagerAdapter extends FragmentPagerAdapter {
        public myPagerAdapter(FragmentManager frm) {
            super(frm);
        }


        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return (new contactFragment());
            } else if (position == 1) {
                return (new chatsFragment());

            } else if(position==2){
                return (new statusFragment());

            }
            else
            {
                return (new profileFragment());

            }


        }


        public CharSequence getPageTitle(int i) {
            if (i == 0) {
                return "Contacts";
            } else if (i == 1) {
                return "Chats";
            } else if(i==2) {
                return "Status";
            }
            else
            {
                return "Profile";
            }
        }

    }
}

