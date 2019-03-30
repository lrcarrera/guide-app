package com.example.pathfinderapp;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishFragment extends Fragment implements Serializable{

    private CustomPageAdapter pagerAdapter;
    private ViewPager pager;
    private SeekBar seekBar;
    public Post post;
    public User user;
    private int currentItem = 0;
    private OnFragmentInteractionListener mListener;


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
        user = DefValues.defUser();
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
        if(Integer.parseInt(time[0]) >= 21)
            return true;
        return false;
    }

    public void confirmButtonPressed(){
        DefValues.AddPostToToursList(post);
        MainActivity mainActivity = (MainActivity)  getActivity();
        mainActivity.moveToToursPage();
    }

    public void cancelButtonPressed(){
        seekBarInitialProgress();
        currentItem = 0;
        post = new Post();
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
                //int aux = pager.getCurrentItem();
                if(position < currentItem)
                {
                    pager.setCurrentItem(position);
                } else {
                    pager.setCurrentItem(currentItem);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //Toast.makeText(getContext(), R.string.error_invalid_password, Toast.LENGTH_LONG);

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

    /*@Override
    public void onFragmentInteraction(Uri uri) {

    }*/

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return WhenFragment.newInstance("Page1", "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return WhereFragment.newInstance("Page 2", "Page # 2");
            default:
                return null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class CustomPageAdapter extends FragmentPagerAdapter implements Serializable {
        private List<Fragment> fragments;
        private int[] mResources;

        public CustomPageAdapter(FragmentManager fm, List<Fragment> fragments ) {
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
