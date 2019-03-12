package com.example.pathfinderapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;

public class MainActivity extends AppCompatActivity implements
        ToursFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        PublishFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        WhenFragment.OnFragmentInteractionListener, WhereFragment.OnFragmentInteractionListener
{

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



        /*Boolean mobile_register_flag = sharedpref.getBoolean("mobile_register_flag", false);

        if (!mobile_register_flag) {
            Intent intent = new Intent(FlashView.this,
                    RegisterActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(FlashView.this,
                    ActivityTwo.class);
            startActivity(intent);
        }*/


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
