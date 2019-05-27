package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pathfinderapp.Adapters.AdapterLanguageHorizontal;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private View view;
    private GoogleMap mMap;
    private PublishFragment parent;
    private TextView priceNumber;
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_summary, container, false);
        FloatingActionButton confirm = view.findViewById(R.id.continueButton);
        FloatingActionButton cancel = view.findViewById(R.id.cancelButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(false);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(true);
            }
        });
        getDataResume();
        return view;
    }

    private void buttonPressed(boolean isCancel){
        ArrayList<Language> aux = new ArrayList<Language>();
        for(Language language : parent.user.getLanguages()){
            language.setAdded(false);
            aux.add(language);
        }
        parent.user.setLanguages(aux);
        if(isCancel)
            cancelButtonPressed();
        else
            confirmButtonPressed();
    }

    private void confirmButtonPressed(){
        parent.confirmButtonPressed();
    }

    private void cancelButtonPressed(){
        parent.cancelButtonPressed();
    }

    private void getDataResume(){
        addTextViewsContent();
        addLanguages();
        addMapMarkers();
    }

    private void addTextViewsContent(){
        TextView dateContent = view.findViewById(R.id.dateContent);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateContent.setText(sdf.format(parent.post.getDueTo()));

        TextView fromHourNumber = view.findViewById(R.id.fromHourNumber);
        fromHourNumber.setText(parent.post.getStartHour());

        TextView toHourNumber = view.findViewById(R.id.toHourNumber);
        toHourNumber.setText(parent.post.getEndHour());


        TextView touristAllowed = view.findViewById(R.id.touristAllowedNumber);
        touristAllowed.setText(String.valueOf(parent.post.getNumTourists()));

        priceNumber = view.findViewById(R.id.priceNumber);
        priceNumber.setText(String.valueOf(parent.post.getPrice()));
    }

    private void addLanguages(){
        RecyclerView recycler = view.findViewById(R.id.languages);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        AdapterLanguageHorizontal adapterLanguages = new AdapterLanguageHorizontal(parent.post.getLanguages());
        recycler.setAdapter(adapterLanguages);
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void addMapMarkers(){
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if(parent.isNightMode())
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(parent.getContext(), R.raw.mapstyle_night));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parent.post.getPlace().getCoord(), 11));

                for (Marker marker : parent.post.getPlaces()){
                    mMap.addMarker(new MarkerOptions().position(marker.getPosition()));
                }
            }
        });
    }

    public void setPriceView(){
        priceNumber.setText(String.valueOf(parent.post.getPrice()));
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public void onButtonPressed() {
        if (mListener != null)
            mListener.onFragmentInteraction();
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
        void onFragmentInteraction();
    }
}
