package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pathfinderapp.Adapters.AdapterLanguage;
import com.example.pathfinderapp.Adapters.AdapterLanguageHorizontal;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    RecyclerView recycler;
    PublishFragment parent;
    public TextView priceNumber;
    private OnFragmentInteractionListener mListener;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(PublishFragment parent) {
        SummaryFragment fragment = new SummaryFragment();
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

    /*public void setPrice(){

        priceNumber.setText(Integer.parseInt(String.valueOf(parent.post.getPrice())));
        //onCreateView(getLayoutInflater(), g)
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_summary, container, false);
        TextView dateContent = view.findViewById(R.id.dateContent);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateContent.setText(sdf.format(parent.post.getDueTo()));

        TextView fromHourNumber = view.findViewById(R.id.fromHourNumber);
        fromHourNumber.setText(parent.post.getStartHour());

        TextView toHourNumber = view.findViewById(R.id.toHourNumber);
        toHourNumber.setText(parent.post.getEndHour());


        int tourist = parent.post.getNumTourists();
        TextView touristAllowed = view.findViewById(R.id.touristAllowedNumber);
        touristAllowed.setText(String.valueOf(parent.post.getNumTourists()));

        //float aux = 15;
        //if(parent.post.getPrice() == 0.0f)
        priceNumber = view.findViewById(R.id.priceNumber);


        recycler = view.findViewById(R.id.languages);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        AdapterLanguageHorizontal adapterLanguages = new AdapterLanguageHorizontal(parent.post.getLanguages());
        recycler.setAdapter(adapterLanguages);
        recycler.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
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
