package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhichTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhichTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhichTimeFragment extends Fragment {

    private PublishFragment parent;
    private boolean isDuration;
    private TextView whenTitle;
    private TimePicker timePicker;
    private OnFragmentInteractionListener mListener;

    public WhichTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment WhichTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static WhichTimeFragment newInstance(PublishFragment parent) {
        WhichTimeFragment fragment = new WhichTimeFragment();
        fragment.parent = parent;
        return fragment;
    }*/

    public static WhichTimeFragment newInstance(PublishFragment parent, boolean isDuration){
        WhichTimeFragment fragment = new WhichTimeFragment();
        fragment.parent = parent;
        fragment.isDuration = isDuration;
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
        View view = inflater.inflate(R.layout.fragment_which_time, container, false);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        whenTitle = (TextView) view.findViewById(R.id.whenTitle);

        checkHours();



        FloatingActionButton continueButton =  view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String aux = timePicker.getHour() + ":" + timePicker.getMinute();
                if(!isDuration)
                    parent.post.setStartHour(aux);
                if(isDuration)
                    parent.post.setEndHour(aux);
                nextStep();
            }
        });

        /*continueButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                    continueButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });*/

        return view;
    }

    private void checkHours(){
        String[] time = new String[]{"1", "2"};
        if(!isDuration){
            if(parent.post.getStartHour() != null){
                time = parent.post.getStartHour().split(":");
                timePicker.setHour(Integer.parseInt(time[0]));
                timePicker.setMinute(Integer.parseInt(time[1]));
            }
        } else {
            whenTitle.setText(R.string.durationTitle);
            if(parent.post.getEndHour() != null){
                time = parent.post.getEndHour().split(":");
                timePicker.setHour(Integer.parseInt(time[0]));
                timePicker.setMinute(Integer.parseInt(time[1]));
            }
        }


    }

    private void nextStep(){ parent.setCurrentPage(); }

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
