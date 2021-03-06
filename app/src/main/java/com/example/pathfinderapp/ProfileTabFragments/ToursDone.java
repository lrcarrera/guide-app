package com.example.pathfinderapp.ProfileTabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.pathfinderapp.Adapters.AdapterLanguageHorizontal;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ToursDone extends Fragment {

    private List<Post> posts;
    private CarouselView postsCarousselView;
    private ProgressBar rotateLoading;

    public  ToursDone(){}

    public ToursDone(List<Post> posts){ this.posts = posts; }

    public static ToursDone newInstance(List<Post> posts){
        ToursDone toursDone = new ToursDone();
        toursDone.posts = posts;
        return toursDone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_profile_secondtab, viewGroup, false);
        CirclePageIndicator indicator = rootView.findViewById(R.id.indicator);
        rotateLoading = rootView.findViewById(R.id.rotate_loading_tours);

        indicator.setFillColor(R.color.address);
        if(posts != null && posts.size() > 0){
            postsCarousselView = (CarouselView) rootView.findViewById(R.id.carouselView);
            postsCarousselView.setPageCount(posts.size());
            postsCarousselView.setSlideInterval(4000);
            postsCarousselView.setViewListener(viewListener);
        } else {
            postsCarousselView = (CarouselView) rootView.findViewById(R.id.carouselView);
            postsCarousselView.setVisibility(View.INVISIBLE);
            TextView textView = (TextView) rootView.findViewById(R.id.emptyTextViewMessage);
            textView.setVisibility(View.VISIBLE);
        }
        showProgress(true);

        return rootView;
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            Post post = posts.get(position);
            View customView = getLayoutInflater().inflate(R.layout.tour_summary_card, null);
            LinearLayout linearLayout = customView.findViewById(R.id.main_layout);
            linearLayout.setPadding(10, 20, 10, 10);
            FrameLayout layout = customView.findViewById(R.id.map);
            layout.setVisibility(View.INVISIBLE);

            LinearLayout zeroLayout = customView.findViewById(R.id.zeroLayout);
            TextView cityName = customView.findViewById(R.id.cityName);
            cityName.setText(post.getPlace().getName());
            zeroLayout.setVisibility(View.VISIBLE);

            RecyclerView recycler = customView.findViewById(R.id.languages);
            recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
            AdapterLanguageHorizontal adapterLanguages = new AdapterLanguageHorizontal(post.getLanguages());
            recycler.setAdapter(adapterLanguages);
            recycler.setItemAnimator(new DefaultItemAnimator());

            TextView dateContent = customView.findViewById(R.id.dateContent);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateContent.setText(sdf.format(post.getDueTo()));

            TextView fromHourNumber = customView.findViewById(R.id.fromHourNumber);
            fromHourNumber.setText(post.getStartHour());

            TextView toHourNumber = customView.findViewById(R.id.toHourNumber);
            toHourNumber.setText(post.getEndHour());


            TextView touristAllowed = customView.findViewById(R.id.touristAllowedNumber);
            touristAllowed.setText(String.valueOf(post.getTourists().size()));

            TextView priceNumber = customView.findViewById(R.id.priceNumber);
            priceNumber.setText(String.valueOf(post.getPrice()));

            showProgress(false);

            return customView;
        }
    };

    private void showProgress(final boolean show) {

        if (show) {
            rotateLoading.setVisibility(View.VISIBLE);
            postsCarousselView.setVisibility(View.INVISIBLE);
        } else {
            rotateLoading.setVisibility(View.GONE);
            postsCarousselView.setVisibility(View.VISIBLE);

        }
    }
}