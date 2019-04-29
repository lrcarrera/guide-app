package com.example.pathfinderapp.Adapters;

import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.ProfileTabFragments.ReviewsCaroussel;
import com.example.pathfinderapp.ProfileTabFragments.ToursDone;
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
    List<Post> posts;

    public AdapterProfile(FragmentManager fm, int NoofTabs, List<Review> reviews, List<Post> posts){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.reviews = reviews;
        this.posts = posts;
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
                return new ToursDone(posts);
            case 2:
                return new ProfileTab3Fragment();
            default:
                return null;
        }
    }
}