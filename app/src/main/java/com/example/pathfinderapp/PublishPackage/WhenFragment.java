package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhenFragment extends Fragment implements INexStep {

    private CalendarView calendarView;
    private PublishFragment parent;
    private OnFragmentInteractionListener mListener;
    private static String DATE_PATTERN = "dd/MM/yyyy";
    private static String CONTENT_PATTERN = "/";


    public WhenFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment WhenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhenFragment newInstance(PublishFragment parent) {

        WhenFragment fragment = new WhenFragment();
        fragment.parent = parent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_when, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                parent.post.setDueTo(new Date(calendarView.getDate()));
                nextStep();
            }
        });
        setCalendarMinDate();
        setFocus();
        parent.setSeekBarStatus();
        return view;
    }

    public void nextStep(){
        parent.setCurrentPage();
    }

    private void setFocus(){
        if(parent.post.getDueTo() != null){
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            String date = sdf.format(parent.post.getDueTo());
            String parts[] = date.split(CONTENT_PATTERN);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(parts[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
            calendarView.setDate(calendar.getTimeInMillis());
        }
    }

    private void  setCalendarMinDate() {
        long millis = System.currentTimeMillis();
        calendarView.setMinDate(millis);
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
