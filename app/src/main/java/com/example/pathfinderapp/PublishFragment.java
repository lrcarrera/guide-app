package com.example.pathfinderapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.SummaryFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PublishFragment extends Fragment implements Serializable{

    private CustomPageAdapter pagerAdapter;
    private ViewPager pager;
    private SeekBar seekBar;
    public Post post;
    public User user;
    private int currentItem = 0;

    public PublishFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        seekBar = (SeekBar) view.findViewById(R.id.publishSeekBar);


        post = new Post();
        post.setTourists(new ArrayList<User>());
        user = DefValues.getUserInContext();
        post.setGuide(user);

        seekBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return true;
            }

        });

        return view;
    }

    public boolean isNightMode(){
        String[] time;
        time = post.getStartHour().split(":");
        if(Integer.parseInt(time[0]) >= 21)
            return true;

        time = post.getEndHour().split(":");
        return Integer.parseInt(time[0]) >= 21;
    }

    public void confirmButtonPressed(){
        makeSuccessToast();

        user.setToursCound(user.getToursCound() + 1);
        post.setUuid(user.getUid() + user.getScore());

        savePostInToDatabase();

        DefValues.addUserRelatedPost(post);
        DefValues.addAllPublishedPosts(post);

        MainActivity mainActivity = (MainActivity)  getActivity();
        mainActivity.moveToToursPage();

        cancelButtonPressed();
    }

    private void savePostInToDatabase(){

        User user = DefValues.getUserInContext();
        if (user == null)
            return;
        
        user.addPost(post);

        final Map<String, Object> newUser = user.addToHashMap();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DefValues.getUserInContextDocument().update("user", newUser);
        db.collection("posts").add(post);
    }

    private void makeSuccessToast(){
        Toast toast = Toast.makeText(getContext(), R.string.creationSuccessful, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void cancelButtonPressed(){
        seekBarInitialProgress();
        currentItem = 0;
        post = new Post();
        post.setTourists(new ArrayList<User>());
        post.setGuide(user);
        pager.setCurrentItem(0);
    }

    private void seekBarInitialProgress(){
        seekBar.setProgress(5);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pager = getView().findViewById(R.id.pager);

        List<Fragment> fragments = getFragments();
        pagerAdapter = new CustomPageAdapter(getFragmentManager(), fragments);
        seekBarInitialProgress();

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                pager.setCurrentItem(position < currentItem ? position : currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void summaryFragmentChange(){
        SummaryFragment sm = (SummaryFragment) pagerAdapter.getItem(pagerAdapter.getCount()-1);
        sm.setPriceView();
        setCurrentPage();
    }

    public void setCurrentPage(){
        setSeekBarStatus();
        currentItem++;
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    public void setSeekBarStatus(){
        seekBar.setProgress(seekBar.getProgress() + 10);
        seekBar.refreshDrawableState();
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(WhereFragment.newInstance(this));
        fList.add(WhenFragment.newInstance(this));
        fList.add(WhichTimeFragment.newInstance(this, false));
        fList.add(WhichTimeFragment.newInstance(this, true));
        fList.add(TouristsAllowedFragment.newInstance(this));
        fList.add(LanguagesFragment.newInstance(this));
        fList.add(RouteSelectionFragment.newInstance(this));
        fList.add(PriceFragment.newInstance(this));
        fList.add(SummaryFragment.newInstance(this));
        return fList;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    private class CustomPageAdapter extends FragmentPagerAdapter implements Serializable {
        private final List<Fragment> fragments;

        CustomPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position)
        {
            return this.fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return this.fragments.size();
        }
    }
}
