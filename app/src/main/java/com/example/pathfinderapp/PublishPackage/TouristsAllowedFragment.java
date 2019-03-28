package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TouristsAllowedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TouristsAllowedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TouristsAllowedFragment extends Fragment {


    private TextView touristNumber;
    private PublishFragment parent;
    private final static int MAX_TOURIST = 16;
    private OnFragmentInteractionListener mListener;

    public TouristsAllowedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1
     * @return A new instance of fragment TouristsAllowedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TouristsAllowedFragment newInstance(PublishFragment parent) {
        TouristsAllowedFragment fragment = new TouristsAllowedFragment();
        fragment.parent = parent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tourists_allowed, container, false);
        //parent.setSeekBarStatus();
        touristNumber = (TextView) view.findViewById(R.id.touristNumber);
        touristNumber.setText(String.valueOf(parent.post.getNumTourists()));
        ImageView btnIncrease = (ImageView) view.findViewById(R.id.btnIncrease);
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTouristNumber(true);
            }
        });

        ImageView btnDecrease = (ImageView) view.findViewById(R.id.btnDecrease);
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTouristNumber(false);
            }
        });

        FloatingActionButton continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            parent.post.setNumTourists(Integer.parseInt(touristNumber.getText().toString()));
            nextStep();
            }
        });
        return view;
    }

    public void setTouristNumber(boolean increase)
    {
        int tourists = Integer.parseInt(touristNumber.getText().toString());
        int new_tourist = 0;
        if(increase)
            new_tourist = (tourists + 1) % MAX_TOURIST;


        if(!increase)
            new_tourist = (tourists - 1) % MAX_TOURIST;

        if(new_tourist == 0 && increase)
            new_tourist = 1;

        /*Cas 1 turist i s'apreta -1*/
        if(new_tourist == 0 && !increase)
            new_tourist = MAX_TOURIST - 1;

        touristNumber.setText(String.format(Locale.getDefault(), "%d", new_tourist));

    }

    private void nextStep(){ parent.setCurrentPage(); }

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
