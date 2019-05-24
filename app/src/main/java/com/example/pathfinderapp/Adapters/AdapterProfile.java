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

/**
 * Created by User on 2/28/2017.
 */

public class AdapterProfile extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;
    List<Review> reviews;
    private ArrayList<Post> posts;

    public AdapterProfile(FragmentManager fm, int NoofTabs, List<Review> reviews, List<Post> posts, boolean due){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.reviews = reviews;
        this.posts = new ArrayList<>();
        if(due){
            getOnlyPostsCreatedByTheCurrentUser(posts);
        } else {
            this.posts.addAll(posts);
        }
    }

    private void addPosts(List<Post> posts){
        this.posts.addAll(posts);
    }

    private void getOnlyPostsCreatedByTheCurrentUser(List<Post> posts){
        String userUid = DefValues.getUserInContext().getUid();
        if (userUid == null)
            return;

        for (int i=0; i< posts.size(); i++){
            Post current = posts.get(i);
            if(userUid.equals(current.getGuide().getUid())){
                this.posts.add(current);
            }
        }
    }

    public void notifiyDataChanged(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    /*@Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }*/

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