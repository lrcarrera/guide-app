package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pathfinderapp.Adapters.AdapterPlace;
import com.example.pathfinderapp.AdapterSearch;
import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;
import com.example.pathfinderapp.SearchItem;
import com.example.pathfinderapp.ToursFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhereFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhereFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhereFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PublishFragment parent;
    private ArrayList<Place> placesList;
    RecyclerView recycler;
    Context context;
    private OnFragmentInteractionListener mListener;

    public WhereFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment WhereFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhereFragment newInstance(PublishFragment parent) {
        WhereFragment fragment = new WhereFragment();
        fragment.parent = parent;
        return fragment;
    }

    @Override
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
        View view = inflater.inflate(R.layout.fragment_where, container, false);
        parent.setSeekBarStatus();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycler = getView().findViewById(R.id.places);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        placesList = new ArrayList<Place>();
        placesList.add(new Place("Ubicación Actual", "", R.drawable.ic_action_mylocation));
        placesList.add(new Place("Lleida", "Spain", R.drawable.ic_action_place));
        placesList.add(new Place("Barcelona", "Spain", R.drawable.ic_action_place));
        placesList.add(new Place("London", "England", R.drawable.ic_action_place));
        AdapterPlace adapterPlace = new AdapterPlace(placesList);
        recycler.setAdapter(adapterPlace);
        recycler.setItemAnimator(new DefaultItemAnimator());

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
}
