package com.example.pathfinderapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.pathfinderapp.AsyncStuff.ConnectivityReceiver;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.SummaryFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ToursFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        PublishFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        WhenFragment.OnFragmentInteractionListener, WhereFragment.OnFragmentInteractionListener,
        WhichTimeFragment.OnFragmentInteractionListener, PriceFragment.OnFragmentInteractionListener,
        RouteSelectionFragment.OnFragmentInteractionListener, TouristsAllowedFragment.OnFragmentInteractionListener,
        LanguagesFragment.OnFragmentInteractionListener, SummaryFragment.OnFragmentInteractionListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    SharedPreferences prefs;
    private static final String PACKAGE_NAME = "com.example.pathfinderapp";
    private ImageView notConnectionDetectedImage;

    Boolean isFullConnectivityOn;

    private final Fragment fragment1 = new SearchFragment();
    private final Fragment fragment2 = new ToursFragment();
    private final Fragment fragment3 = new PublishFragment();
    private final Fragment fragment4 = new ProfileFragment().newInstance(true);
    private BottomNavigationView navigation;

    private BroadcastReceiver connectivityReceiver = null;

    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;

    public void moveToToursPage(){
        SearchFragment searchFragment = (SearchFragment) fragment1;
        searchFragment.recyclerListChanged();
        ToursFragment aux = (ToursFragment) fragment2;
        aux.recyclerListChanged();
        setProfileTours(DefValues.getUserRelatedPosts());
        fm.beginTransaction().hide(active).show(fragment2).commit();
        navigation.getMenu().getItem(1).setChecked(true);
        active = fragment2;
    }
    
    public void moveToProfilePage(){
        fm.beginTransaction().hide(active).show(fragment4).commit();
        navigation.getMenu().getItem(3).setChecked(true);
        active = fragment4;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_tours:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.navigation_publish:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };

    public void setProfileTours(ArrayList< Post > posts){
        ProfileFragment aux = (ProfileFragment) fragment4;
        aux.setTabsSubPages(posts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);

        notConnectionDetectedImage = findViewById(R.id.dinosaur_connectivity);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        connectivityReceiver = new ConnectivityReceiver();


        String userUid = user.getUid();

        db.collection("users").whereEqualTo("user.uid", userUid )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DefValues.setDocumentReference(document.getReference());
                                DefValues.setUserInContext(document);

                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                String userUid = auth.getCurrentUser().getUid();
                                String token = prefs.getString(getResources().getString(R.string.message_token), null);
                                User user = DefValues.getUserInContext();

                                if (token != null && user != null) {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("users").child(userUid).push().setValue(token);
                                    user.setMessageToken(token);
                                    DefValues.getUserInContextDocument().update("user", user.addToHashMap());
                                }
                            }

                            fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
                            fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
                            fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
                            fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

                        } else {
                            notConnectionToast();
                            Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                        }
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager mgr = getSystemService(NotificationManager.class);
            mgr.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }

        TextView mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);

    }

    private void notConnectionToast(){
        Toast toast = Toast.makeText(this, R.string.not_connection, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        googleAPI.makeGooglePlayServicesAvailable(this);

        ConnectivityReceiver.connectivityReceiverListener = this;
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void changeIcons(BottomNavigationView navigationView)
    {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_action_search);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_action_tours);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_action_publish);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_action_profile);
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onNetworkConnectionChanged(String status) {
        isFullConnectivityOn = prefs.getBoolean(getResources().getString(R.string.full_connectivity), false);

        if (status.equals(getResources().getString(R.string.wifi_ok))) {
            notConnectionDetectedImage.setVisibility(View.GONE);
        }else if (status.equals(getResources().getString(R.string.mobile_ok)) && isFullConnectivityOn){
            notConnectionDetectedImage.setVisibility(View.VISIBLE);
            notConnectionDetectedImage.bringToFront();
        } else {
            notConnectionDetectedImage.setVisibility(View.VISIBLE);
            notConnectionDetectedImage.bringToFront();
        }
    }

}
