package com.example.pathfinderapp.Adapters;

import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.ProfileTabFragments.ReviewsCaroussel;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab2Fragment;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab3Fragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by User on 2/28/2017.
 */

public class AdapterProfile extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;
    List<Review> reviews;

    public AdapterProfile(FragmentManager fm, int NoofTabs, List<Review> reviews){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.reviews = reviews;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new ReviewsCaroussel(reviews);
            case 1:
                return new ProfileTab2Fragment();
            case 2:
                return new ProfileTab3Fragment();
            default:
                return null;
        }
    }
}