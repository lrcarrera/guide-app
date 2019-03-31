package com.example.pathfinderapp.PublishPackage;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Looper;

import com.example.pathfinderapp.Adapters.AdapterPlace;
import com.example.pathfinderapp.MainActivity;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhereFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhereFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhereFragment extends Fragment implements INexStep{

    private static final String LOCATION_INADEQUATE = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
    private static final String LOCATION_NOT_SATISFIED = "Location settings are not satisfied. Attempting to upgrade ";
    private static final String LOCATION_SETTINGS = "location settings ";
    private static final String PENDING_INTENT_UNABLE = "PendingIntent unable to execute request.";
    private static String STOP_LOCATION_UPDATES = "stopLocationUpdates: updates never requested, no-op.";
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Code used in requesting runtime permissions.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Boolean mRequestingLocationUpdates;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private FusedLocationProviderClient mFusedLocationClient;
    private PublishFragment parent;
    private ArrayList<Place> placesList;
    private RecyclerView recycler;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_where, container, false);
        parent.setSeekBarStatus();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRequestingLocationUpdates = true;
        recycler = getView().findViewById(R.id.places);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        placesList = DefValues.defPlaces();
        AdapterPlace adapterPlace = new AdapterPlace(placesList);
        adapterPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos =  recycler.getChildAdapterPosition(v);
                onPlaceClicked(pos);
            }
        });

        recycler.setAdapter(adapterPlace);
        recycler.setItemAnimator(new DefaultItemAnimator());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
            }
        };
    }

    private void onPlaceClicked(int position){

        Place aux = placesList.get(position);
        if(position == 0) {
            if(mCurrentLocation == null){
                Toast.makeText(getContext(), R.string.location, Toast.LENGTH_SHORT).show();
            } else {
                aux.setCoord(new LatLng(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()));
                stopLocationUpdates();
                parent.post.setPlace(aux);
                nextStep();
            }
        } else {
            parent.post.setPlace(aux);
            nextStep();
        }

    }



    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(parent.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(),  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(parent.getActivity(), new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e)
                   {
                       int statusCode = ((ApiException) e).getStatusCode();
                       switch (statusCode) {
                           case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                               Log.i(TAG, LOCATION_NOT_SATISFIED + LOCATION_SETTINGS);
                               try {
                                   // Show the dialog by calling startResolutionForResult(), and check the
                                   // result in onActivityResult().
                                   ResolvableApiException rae = (ResolvableApiException) e;
                                   rae.startResolutionForResult(parent.getActivity(), REQUEST_CHECK_SETTINGS);
                               } catch (IntentSender.SendIntentException sie) {
                                   Log.i(TAG, PENDING_INTENT_UNABLE);
                               }
                               break;
                           case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                               String errorMessage = LOCATION_INADEQUATE;
                               Log.e(TAG, errorMessage);
                               Toast.makeText(parent.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                               mRequestingLocationUpdates = false;
                       }
                   }
                });
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, STOP_LOCATION_UPDATES);
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(parent.getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(parent.getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(parent.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        startLocationPermissionRequest();
    }

    public void nextStep(){
        parent.setCurrentPage();
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
