package com.example.pathfinderapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.SummaryFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;

public class MainActivity extends AppCompatActivity implements
        ToursFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        PublishFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        WhenFragment.OnFragmentInteractionListener, WhereFragment.OnFragmentInteractionListener,
        WhichTimeFragment.OnFragmentInteractionListener, PriceFragment.OnFragmentInteractionListener,
        RouteSelectionFragment.OnFragmentInteractionListener, TouristsAllowedFragment.OnFragmentInteractionListener,
        LanguagesFragment.OnFragmentInteractionListener, SummaryFragment.OnFragmentInteractionListener
{

    SharedPreferences prefs;

    private TextView mTextMessage;
    final Fragment fragment1 = new SearchFragment();
    final Fragment fragment2 = new ToursFragment();
    final Fragment fragment3 = new PublishFragment();
    final Fragment fragment4 = new ProfileFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    //mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_tours:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    //mTextMessage.setText(R.string.title_tours);
                    return true;
                case R.id.navigation_publish:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    //mTextMessage.setText(R.string.title_publish);
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    //mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void changeIcons(BottomNavigationView navigationView)
    {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_action_search);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_action_tours);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_action_publish);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_action_profile);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
