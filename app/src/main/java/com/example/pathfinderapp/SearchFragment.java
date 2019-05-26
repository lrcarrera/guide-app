package com.example.pathfinderapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private AdapterTour adapterSearch;
    private ArrayList<Post> searchList;
    private RecyclerView recycler;
    private OnFragmentInteractionListener mListener;

    private ProgressBar rotateLoading;

    public SearchFragment() {
        // Required empty public constructor
    }

    private void showProgress(final boolean show) {

        if (show) {
            rotateLoading.setVisibility(View.VISIBLE);
        } else {
            rotateLoading.setVisibility(View.GONE);
        }
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //searchList = DefValues.getMockYourToursList();
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        rotateLoading = view.findViewById(R.id.rotate_loading_post_list);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        showProgress(true);
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DefValues.addAllPublishedPosts(document);
                            }
                            searchList = DefValues.getAllPublishedPosts();
                            FragmentManager fragmentManager = getFragmentManager();
                            adapterSearch = new AdapterTour((MainActivity) getActivity(),fragmentManager, searchList, false, null);

                            recycler = view.findViewById(R.id.recyclerid);
                            recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

                            recycler.setAdapter(adapterSearch);
                            recycler.setItemAnimator(new DefaultItemAnimator());

                            MainActivity activity = (MainActivity) getActivity();
                            activity.setProfileTours(searchList);
                            showProgress(false);



                        } else {
                            showProgress(false);
                            notConnectionToast();
                            Log.w("TEST08", "Error getting documents.", task.getException());
                        }
                    }
                });
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);
        return  view;
    }

    private void notConnectionToast(){
        Toast toast = Toast.makeText(getContext(), R.string.not_connection, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void recyclerListChanged(){
        searchList = DefValues.getAllPublishedPosts();
        adapterSearch.setToursList(searchList);
        adapterSearch.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view == null)
            return;


        /*db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DefValues.addAllPublishedPosts(document);
                            }


                        } else {
                            Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                        }
                    }
                });*/

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterSearch.filter(newText);
        return false;
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
}
