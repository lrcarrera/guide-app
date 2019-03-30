package com.example.pathfinderapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathfinderapp.Adapters.AdapterProfile;
import com.example.pathfinderapp.AsyncStuff.AsyncTaskLoadImage;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab1Fragment;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab2Fragment;
import com.example.pathfinderapp.ProfileTabFragments.ProfileTab3Fragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String NO_PHOTO = "no_photo";
    private static final String NO_NAME = "no_name";
    private static final String NO_EMAIL = "no_email";
    private static final String PACKAGE_NAME = "com.example.pathfinderapp";

    Dialog myDialog;
    ImageButton settings;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences prefs;
    ImageView profilePicture;
    TextView textViewName;
    //TextView textViewEmail;

    CheckBox checkboxNotifications;
    CheckBox checkboxFrench;
    CheckBox checkboxEnglish;
    CheckBox checkboxGerman;
    CheckBox checkboxItalian;
    CheckBox checkboxSpanish;

    RadioGroup radioGroupConnectivity;
    RadioButton radioWifi;
    RadioButton radioWifiAndMore;

    //private static final String TAG = "MainActivity";

    private AdapterProfile mSectionsPageAdapter;
    private ViewPager mViewPager;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        myDialog = new Dialog(getContext());
        settings = (ImageButton) rootView.findViewById(R.id.configure_button);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });


        profilePicture = rootView.findViewById(R.id.profilePicture);
        textViewName = rootView.findViewById(R.id.tv_name);
        //textViewEmail = rootView.findViewById(R.id.tv_email);

        //From facebook login
        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(
                PACKAGE_NAME, Context.MODE_PRIVATE);

        String facebookImageLink =  prefs.getString(getResources().getString(R.string.facebook_picture_link), NO_PHOTO);
        String name = prefs.getString(getResources().getString(R.string.facebook_name), NO_NAME);
        String email = prefs.getString(getResources().getString(R.string.facebook_email), NO_EMAIL);

        if(!Objects.equals(facebookImageLink, NO_PHOTO))
            new AsyncTaskLoadImage(profilePicture).execute(facebookImageLink);

        if(!Objects.equals(name, NO_NAME)) textViewName.setText(name);
       // if(!Objects.equals(email, NO_EMAIL)) textViewEmail.setText(email);

        //TODO: From normal login


        //Log.d(TAG, "onCreate: Starting.");

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.reviewsTitle));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.toursTitle));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scoresTitle));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager =(ViewPager) rootView.findViewById(R.id.view_pager);
        AdapterProfile tabsAdapter = new AdapterProfile(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

    public void openSettings(){

        myDialog.setContentView(R.layout.settings_popup);

        checkboxNotifications = (CheckBox) myDialog.findViewById(R.id.checkbox_notifications);
        checkboxFrench = (CheckBox) myDialog.findViewById(R.id.checkbox_french);
        checkboxEnglish = (CheckBox) myDialog.findViewById(R.id.checkbox_english);
        checkboxGerman = (CheckBox) myDialog.findViewById(R.id.checkbox_german);
        checkboxItalian = (CheckBox) myDialog.findViewById(R.id.checkbox_italian);
        checkboxSpanish = (CheckBox) myDialog.findViewById(R.id.checkbox_spanish);

        radioGroupConnectivity = (RadioGroup) myDialog.findViewById(R.id.radio_group_connectivity);
        radioWifi = (RadioButton) myDialog.findViewById(R.id.wifi);
        radioWifiAndMore = (RadioButton) myDialog.findViewById(R.id.wifiandmore);


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        handleActionButtons();
        setInitialValuesToTogglesItems();
    }

    private void handleActionButtons(){

        ImageButton btnClose;
        Button btnLogout;
        Button btnSave;

        btnClose = (ImageButton) myDialog.findViewById(R.id.btnclose);
        btnLogout = (Button) myDialog.findViewById(R.id.btnlogout);
        btnSave = (Button) myDialog.findViewById(R.id.btnSave);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                storePreferenceValues();
            }
        });
    }
    private void setInitialValuesToTogglesItems(){

        Boolean isNotificationsOn = prefs.getBoolean(getResources().getString(R.string.notification), false);
        Boolean isFullConnectivityOn = prefs.getBoolean(getResources().getString(R.string.full_connectivity), false);

        if(isNotificationsOn){
            checkboxNotifications.setChecked(true);
        }else{
            checkboxNotifications.setChecked(false);
        }
        if(isFullConnectivityOn){
            //radioGroupConnectivity.check(R.id.wifiandmore);
            radioWifi.setChecked(false);
            radioWifiAndMore.setChecked(true);
        }else{
           // radioGroupConnectivity.check(R.id.wifi);
            radioWifi.setChecked(true);
            radioWifiAndMore.setChecked(false);
        }

        setLanguagesStatuses();
    }

    private void setLanguagesStatuses(){

        Boolean isFrenchChecked = prefs.getBoolean(getResources().getString(R.string.french_key), false);
        Boolean isEnglishChecked = prefs.getBoolean(getResources().getString(R.string.english_key), false);
        Boolean isGermanChecked = prefs.getBoolean(getResources().getString(R.string.german_key), false);
        Boolean isItalianChecked = prefs.getBoolean(getResources().getString(R.string.italian_key), false);
        Boolean isSpanishChecked = prefs.getBoolean(getResources().getString(R.string.spanish_key), false);

        if(isFrenchChecked){
            checkboxFrench.setChecked(true);
        }else{
            checkboxFrench.setChecked(false);
        }
        if(isEnglishChecked){
            checkboxEnglish.setChecked(true);
        }else{
            checkboxEnglish.setChecked(false);
        }
        if(isGermanChecked){
            checkboxGerman.setChecked(true);
        }else{
            checkboxGerman.setChecked(false);
        }
        if(isItalianChecked){
            checkboxItalian.setChecked(true);
        }else{
            checkboxItalian.setChecked(false);
        }
        if(isSpanishChecked){
            checkboxSpanish.setChecked(true);
        }else{
            checkboxSpanish.setChecked(false);
        }
    }

    private void storePreferenceValues(){

        //Checkbox
        if(checkboxNotifications.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.notification), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.notification), false).apply();
        }

        if(radioWifi.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.full_connectivity), false).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.full_connectivity), true).apply();
        }

        if(checkboxFrench.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.french_key), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.french_key), false).apply();
        }
        if(checkboxEnglish.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.english_key), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.english_key), false).apply();
        }
        if(checkboxGerman.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.german_key), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.german_key), false).apply();
        }
        if(checkboxItalian.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.italian_key), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.italian_key), false).apply();
        }
        if(checkboxSpanish.isChecked()){
            prefs.edit().putBoolean(getResources().getString(R.string.spanish_key), true).apply();
        }else{
            prefs.edit().putBoolean(getResources().getString(R.string.spanish_key), false).apply();
        }

        myDialog.dismiss();
        Toast.makeText(getContext(), getResources().getString(R.string.configuration_stored_message), Toast.LENGTH_SHORT).show();
    }

    public void logOut(){

        myDialog.dismiss();

        if (AccessToken.getCurrentAccessToken() == null){// already logged out with fb
            getActivity().finish();//TODO: implement logout for credentials from Firebase
            Intent toLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(toLogin);
        }else{
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();
                    getActivity().finish();
                    Intent toLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(toLogin);

                }
            }).executeAsync();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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

    public void setFacebookProfilePicture(String imageUrl){
       // URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        //Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        //return bitmap;
        InputStream in = null;

        try
        {
            Log.i("URL", imageUrl);
            URL url = new URL(imageUrl);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();

            in = httpConn.getInputStream();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Bitmap bmpimg = BitmapFactory.decodeStream(in);
        profilePicture.setImageBitmap(bmpimg);
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
