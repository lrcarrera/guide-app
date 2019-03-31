package com.example.pathfinderapp.ProfileTabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pathfinderapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import androidx.fragment.app.Fragment;

public class ProfileTab1Fragment extends Fragment {
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.review_example1, R.drawable.review_example2, R.drawable.review_example3, R.drawable.review_example4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View rootView =  inflater.inflate(R.layout.fragment_profile_firsttab, viewGroup, false);

        carouselView = (CarouselView) rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        return rootView;

    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}