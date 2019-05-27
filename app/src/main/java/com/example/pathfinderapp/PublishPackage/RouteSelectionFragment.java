package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RouteSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RouteSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteSelectionFragment extends Fragment implements INexStep {


    private ArrayList<Marker> mMarkerArrayList;
    private PublishFragment parent;
    private GoogleMap mMap;
    private OnFragmentInteractionListener mListener;


    public RouteSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment RouteSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteSelectionFragment newInstance(PublishFragment parent) {
        RouteSelectionFragment fragment = new RouteSelectionFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_selection, container, false);
        FloatingActionButton continueButton = (FloatingActionButton) view.findViewById(R.id.continueButton);
        setMap();
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueButtonPressed();
            }
        });

        return view;
    }

    private void continueButtonPressed(){
        if(mMarkerArrayList.size() > 0){
            parent.post.setPlaces(mMarkerArrayList);
            nextStep();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.selection_route_custom_toast,
                    (ViewGroup) getActivity().findViewById(R.id.toastRoot));

            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.CENTER |Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    public void nextStep(){
        parent.setCurrentPage();
    }

    private void setMap(){
        FragmentManager fm = getChildFragmentManager();
        mMarkerArrayList = new ArrayList<>();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.mapLayout, supportMapFragment).commit();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if(parent.post.getPlaces().size() != 0){
                    mMarkerArrayList = parent.post.getPlaces();
                    for (Marker marker : mMarkerArrayList){
                        mMap.addMarker(new MarkerOptions().position(marker.getPosition()));
                    }
                }

                if(parent.isNightMode())
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(parent.getContext(), R.raw.mapstyle_night));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Marker toRemove = null;
                        for (Marker current : mMarkerArrayList)
                        {
                            if (current.getTag() == marker.getTag())
                            {
                                toRemove = current;
                                break;
                            }

                        }

                        if(toRemove != null)
                            mMarkerArrayList.remove(toRemove);
                        marker.remove();
                        return true;
                    }
                });
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parent.post.getPlace().getCoord(), 11));
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions marker_onclick = new MarkerOptions().position(latLng);
                        Marker m = mMap.addMarker(marker_onclick);
                        m.setTag(mMarkerArrayList.size());
                        mMarkerArrayList.add(m);
                    }
                });
            }
        });
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
