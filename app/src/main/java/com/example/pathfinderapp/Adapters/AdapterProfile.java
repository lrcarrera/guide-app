package com.example.pathfinderapp.Adapters;

import com.example.pathfinderapp.ProfileTabFragments.ProfileTab1Fragment;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab2Fragment;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab3Fragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by User on 2/28/2017.
 */

public class AdapterProfile extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;
    public AdapterProfile(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new ProfileTab1Fragment();
            case 1:
                return new ProfileTab2Fragment();
            case 2:
                return new ProfileTab3Fragment();
            default:
                return null;
        }
    }
}