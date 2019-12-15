package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabbedAdapter extends FragmentPagerAdapter
{


    public tabbedAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public tabbedAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                chatsFragment chat=new chatsFragment();
                return chat;
            case 1:
                contactFragment contact=new contactFragment();
                return contact;
            case 2:
                groupFragment group=new groupFragment();
                return group;
            default:
                 return null;

        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chat";
            case 1:
                return "Contact";
            case 2:
                return "Group";
            default:
                return null;

        }


    }
}
