package com.example.pathfinderapp.ProfileTabFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Adapters.CroppedImage;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;

public class ReviewsCaroussel extends Fragment {


    CarouselView reviewsCarousselView;
    private List<Review> reviews;

    //int[] sampleImages = {R.drawable.review_example1, R.drawable.review_example2, R.drawable.review_example3, R.drawable.review_example4};

    public ReviewsCaroussel(List<Review> reviews){
        this.reviews = reviews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View rootView =  inflater.inflate(R.layout.fragment_profile_firsttab, viewGroup, false);
        CirclePageIndicator indicator = rootView.findViewById(R.id.indicator);
        indicator.setFillColor(R.color.address);
        if(reviews != null)
        {
            reviewsCarousselView = (CarouselView) rootView.findViewById(R.id.carouselView);
            reviewsCarousselView.setPageCount(reviews.size());
            reviewsCarousselView.setSlideInterval(4000);
            reviewsCarousselView.setViewListener(viewListener);
            //reviewsCarousselView.setImageListener(imageListener);

        }

        // To set custom views



        return rootView;

    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.reviews_caroussel, null);

            TextView messageTextView = (TextView) customView.findViewById(R.id.ReviewMessage);
            TextView authorTextView = (TextView) customView.findViewById(R.id.ReviewAuthor);
            ImageView profileImageView = (ImageView) customView.findViewById(R.id.ReviewAuthorImage);
            Review current = reviews.get(position);
            String aux = "\""+ current.getMessage() + "\"";
            messageTextView.setText(aux);

            Date date = current.getCreatedAt();
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun
            String year         = (String) DateFormat.format("yyyy", date); // 2013
            aux = current.getAutor().getName() + " - " + day + "/" + monthString + "/" + year;
            authorTextView.setText(aux);

            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(getResources(), current.getAutor().getImage());
            bitmap = CroppedImage.getCroppedBitmap(bitmap);
            profileImageView.setImageBitmap(bitmap);

            //fruitImageView.setImageResource(sampleImages[position]);
            //labelTextView.setText(sampleTitles[position]);

            reviewsCarousselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);

            return customView;
        }
    };

    /*ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };*/
}