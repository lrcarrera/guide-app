package com.example.pathfinderapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ToursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToursFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Post> searchList;
    RecyclerView recycler;
    Context context;

    public ToursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToursFragment newInstance(String param1, String param2) {
        ToursFragment fragment = new ToursFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_tours, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycler = getView().findViewById(R.id.recyclerid);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        searchList = createMockList();


        AdapterSearch adapterSearch = new AdapterSearch(searchList);
        adapterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "verga seleccionada: " +  searchList.get(recycler.getChildAdapterPosition(v)).getGuide().getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recycler.setAdapter(adapterSearch);
        recycler.setItemAnimator(new DefaultItemAnimator());

    }

    public ArrayList<Post> createMockList(){
        ArrayList<Post> list = new ArrayList<Post>();

        User user1 = new User();
        user1.setScore(1.0f);
        user1.setName("Bonifacia la piedra");
        user1.setImage(R.drawable.stock_girl);

        Post post1 = new Post();
        post1.setGuide(user1);
        post1.setPrice(30.0f);

        User user2 = new User();
        user2.setScore(5.0f);
        user2.setName("Concha Mas");
        user2.setImage(R.drawable.stock_girl1);

        Post post2 = new Post();
        post2.setGuide(user2);
        post2.setPrice(30.0f);

        User user3 = new User();
        user3.setScore(5.0f);
        user3.setName("Cubru Tivisoaro");
        user3.setImage(R.drawable.stock_man);

        Post post3 = new Post();
        post3.setGuide(user3);
        post3.setPrice(30.0f);

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
