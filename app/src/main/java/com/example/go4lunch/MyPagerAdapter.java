package com.example.go4lunch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;

import com.example.go4lunch.ui.listview.ListViewFragment;
import com.example.go4lunch.ui.mapview.MapViewFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 3;


    public MyPagerAdapter(FragmentManager fm) {
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
