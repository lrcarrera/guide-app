package com.example.pathfinderapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Post> searchList;
    RecyclerView recycler;
    Context context;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);



        /*final FoldingCell fc = view.findViewById(R.id.folding_cell);
        fc.initialize(30,1000, Color.DKGRAY, 2);
        // attach click listener to folding cell
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fc.toggle(false);
            }
        });
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                GoogleMap mMap = googleMap;

            }
        });*/

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycler = getView().findViewById(R.id.recyclerid);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        searchList = createMockList();


        AdapterTour adapterSearch = new AdapterTour(searchList, getChildFragmentManager());
        /*adapterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "verga seleccionada: " +  searchList.get(recycler.getChildAdapterPosition(v)).getGuide().getName(), Toast.LENGTH_SHORT).show();
            }
        });*/

        recycler.setAdapter(adapterSearch);
        recycler.setItemAnimator(new DefaultItemAnimator());

    }

    public ArrayList<Post> createMockList(){
        ArrayList<Post> list = new ArrayList<Post>();

        User user1 = new User();
        user1.setScore(1.0f);
        user1.setName("Bonifacia la piedra");
        user1.setImage(R.drawable.verguser);



        Post post1 = new Post();
        post1.setGuide(user1);
        post1.setPrice(30.0f);
        post1.setDueTo(new Date());
        post1.setStartHour("19:00");
        post1.setEndHour("21:00");
        post1.setNumTourists(6);
        post1.setPrice(14.5f);
        post1.setLanguages(DefValues.DefLanguages());

        User user2 = new User();
        user2.setScore(5.0f);
        user2.setName("Concha Mas");
        user2.setImage(R.drawable.verguser);

        Post post2 = new Post();
        post2.setGuide(user2);
        post2.setPrice(30.0f);
        post2.setDueTo(new Date());
        post2.setStartHour("12:00");
        post2.setEndHour("14:00");
        post2.setNumTourists(6);
        post2.setPrice(14.5f);
        post2.setLanguages(DefValues.DefLanguages());

        User user3 = new User();
        user3.setScore(5.0f);
        user3.setName("Cubru Tivisoaro");
        user3.setImage(R.drawable.verguser);

        Post post3 = new Post();
        post3.setGuide(user3);
        post3.setPrice(30.0f);
        post3.setDueTo(new Date());
        post3.setStartHour("21:00");
        post3.setEndHour("24:00");
        post3.setNumTourists(6);
        post3.setPrice(14.5f);
        post3.setLanguages(DefValues.DefLanguages());

        list.add(post1);
        list.add(post2);
        list.add(post3);
        return list;
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
        this.context = context;
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
