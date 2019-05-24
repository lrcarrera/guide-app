package com.example.pathfinderapp.Adapters;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.ProfileTabFragments.ReviewsCaroussel;
import com.example.pathfinderapp.ProfileTabFragments.ToursDone;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab3Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterProfilePopUp extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;
    List<Review> reviews;
    private ArrayList<Post> posts;

    public AdapterProfilePopUp(FragmentManager fm, int NoofTabs, List<Review> reviews, List<Post> posts){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.reviews = reviews;
        this.posts = new ArrayList<>();
        this.posts.addAll(posts);
    }

    private void addPosts(List<Post> posts){
        this.posts.addAll(posts);
    }


    public void notifiyDataChanged(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return ReviewsCaroussel.newInstance(reviews);
            case 1:
                return ToursDone.newInstance(posts);
            case 2:
                return new ProfileTab3Fragment();
            default:
                return null;
        }
    }
}
