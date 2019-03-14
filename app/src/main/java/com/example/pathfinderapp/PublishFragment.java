package com.example.pathfinderapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.PublishPackage.DurationFragment;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;

import java.lang.reflect.Array;
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
public class PublishFragment extends Fragment implements WhenFragment.OnFragmentInteractionListener, WhereFragment.OnFragmentInteractionListener,
        WhichTimeFragment.OnFragmentInteractionListener, DurationFragment.OnFragmentInteractionListener, PriceFragment.OnFragmentInteractionListener,
        RouteSelectionFragment.OnFragmentInteractionListener, TouristsAllowedFragment.OnFragmentInteractionListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public CustomPageAdapter pagerAdapter;
    public ViewPager pager;
    public SeekBar seekBar;
    public Post post;
    public User user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static int NUM_ITEMS = 2;

    public PublishFragment() {
        // Required empty public constructor
        //super(fragmenManager);
    }

    /*@Override
    public int getCount() {
        return NUM_ITEMS;
    }*/
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
    user = new User();
    user.setLanguages(new ArrayList<Language>(Arrays.asList(
            new Language("spanish_flag", "Spanish"),
            new Language("english_flag", "English"),
            new Language("french_flag", "French"),
            new Language("italian_flag", "Italian"),
            new Language("german_flag", "German")
            )));


    seekBar.setOnTouchListener(new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            return true;
        }

    });



    return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //ImageView imageView = (ImageView) getView().findViewById(R.id.foo);
        // or  (ImageView) view.findViewById(R.id.foo);
        List<Fragment> fragments = getFragments();
        pagerAdapter = new CustomPageAdapter(getFragmentManager(), fragments);
        pager = getView().findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        seekBar.setProgress(((pager.getCurrentItem() + 1) /getFragments().size() * 100));
        //Toast.makeText(getContext(), R.string.error_invalid_password, Toast.LENGTH_LONG);

    }

    public void setCurrentPage(){
        if(pager == null)
        {
            Toast.makeText(getContext(), "es un null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            setSeekBarStatus();
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    public void setSeekBarStatus(){
        //seekBar.setProgress((int) ((pager.getCurrentItem() + 1) /getFragments().size() * 100));
        seekBar.setProgress(seekBar.getProgress() + 10);
        seekBar.refreshDrawableState();
        //setCurrentPage();
    }

    /*/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static PublishFragment newInstance(String param1, String param2) {
       /* PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        List<Fragment> fragments = getFragments();

        pageAdapter = new CustomPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

    }*/

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(WhereFragment.newInstance(this));
        fList.add(WhenFragment.newInstance(this));
        fList.add(WhichTimeFragment.newInstance(this));
        fList.add(DurationFragment.newInstance(this));
        fList.add(new TouristsAllowedFragment());
        fList.add(LanguagesFragment.newInstance(this));
        fList.add(RouteSelectionFragment.newInstance(this));
        fList.add(new PriceFragment());


        //fList.add(MyFragment.newInstance("Fragment 3", R.drawable.image3));
        return fList;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

    private class CustomPageAdapter extends FragmentPagerAdapter {
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
