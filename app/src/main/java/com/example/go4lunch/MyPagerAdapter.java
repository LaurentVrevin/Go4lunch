package com.example.go4lunch;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.go4lunch.ui.listview.ListViewFragment;
import com.example.go4lunch.ui.mapview.MapViewFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;


    public MyPagerAdapter(FragmentManager fm, int behavior) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {switch (position) {
        case 0:
            return new MapViewFragment();
        case 1:
            return new ListViewFragment();
        case 2:
            return new WorkmatesFragment();
        default:
            throw new IllegalArgumentException("Invalid page index");
    }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }



}
