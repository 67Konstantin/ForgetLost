package com.example.forgetlost;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageViewAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public PageViewAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GiftFragmentMyRecords homeFragment = new GiftFragmentMyRecords();
                return homeFragment;
            case 1:
                LostFragmentMyRecords settingsFragment = new LostFragmentMyRecords();
                return settingsFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
