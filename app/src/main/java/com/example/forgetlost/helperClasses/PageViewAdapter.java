package com.example.forgetlost.helperClasses;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.forgetlost.fragments.GiftFragmentMyRecords;
import com.example.forgetlost.fragments.LostFragmentMyRecords;

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
                LostFragmentMyRecords homeFragment = new LostFragmentMyRecords();
                return homeFragment;
            case 1:
                GiftFragmentMyRecords settingsFragment = new GiftFragmentMyRecords();
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
