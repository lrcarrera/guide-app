package com.example.pathfinderapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.SummaryFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
        DefValues.AddPostToToursList(post);
        savePostInToDatabase();
        MainActivity mainActivity = (MainActivity)  getActivity();
        mainActivity.moveToToursPage();
        cancelButtonPressed();
    }

    private void savePostInToDatabase(){

        User user = DefValues.getUserInContext();
        user.addPost(post);
        /*ArrayList<Post> postList = user.getPostList();
        if(postList == null)
            postList = new ArrayList<Post>();

        postList.add(post);*/
        final Map<String, Object> newUser;
        /*final User realNigga = new User(user.getUid(), user.getName(), user.getPostList(), user.getToursCound(), user.getCompany(),
                user.getScore(), user.getLanguages(), user.getImage(), user.getReviews());*/
        newUser = user.AddToHashMap();

        /*newUser.put("user", new User(user.getUid(), user.getName(), postList, user.getToursCound(), user.getCompany(),
                user.getScore(), user.getLanguages(), user.getImage(), user.getReviews()));*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = DefValues.getUserInContextDocument();
        documentReference.update(newUser);
        db.collection("posts").add(post);
        //ApiFuture<String, Object>
        /*db.collection("users").whereEqualTo("user.uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {



                                //document.s
                                DocumentReference docu = document.getDocumentReference("users");
                                docu.set(newUser);
                                //docu.set(newUser);

                                //docu.set(realNigga);

                                //docu.update(newUser).;
                                //DefValues.setUserInContext(document);
                                //document.getDocumentReference('users');

                            }
                        } else {
                            Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                        }
                    }
                });

        //db.collection("users");
        db.collection("users")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST09", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST09", "Error adding document", e);
                    }
                });
        /*User(String uid, String name, List<Post> postList, int toursCound, String company, float score, ArrayList<
        Language > languages, int image, List<Review > reviews)*/
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();
        String name = user.getEmail();


        Map<String, User> newUser = new HashMap<>();

        newUser.put("user", new User(userUid, name, null, 0, null, 0, languages, 0, null));

        db.collection("users")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST09", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST09", "Error adding document", e);
                    }
                });

        act.changeFirstTimeStatus(); */
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
        post.setUuid(user.getUid() + (user.getScore() + 1));
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
        void onFragmentInteraction();
    }

    private class CustomPageAdapter extends FragmentPagerAdapter implements Serializable {
        private final List<Fragment> fragments;
        private int[] mResources;

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
