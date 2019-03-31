package com.example.pathfinderapp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Post;

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

    private OnFragmentInteractionListener mListener;
    private ArrayList<Post> searchList;
    private RecyclerView recycler;
    private AdapterTour adapterSearch;
    public SwipeController swipeController;

    public ToursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ToursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToursFragment newInstance() {
        return new ToursFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_tours, container, false);
        searchList = DefValues.getMockYourToursList();
        adapterSearch = new AdapterTour(searchList, true, this);

        recycler = view.findViewById(R.id.recyclerid);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        recycler.setAdapter(adapterSearch);
        recycler.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public void recyclerListChanged(){
        searchList = DefValues.getMockYourToursList();
        adapterSearch.setToursList(searchList);
        adapterSearch.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resetController();
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

        Context context1 = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void resetController(){
        View view = getView();
        if(view == null)
            return;

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                adapterSearch.removeItem(position);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recycler);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
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
