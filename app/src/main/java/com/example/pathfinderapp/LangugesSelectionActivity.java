package com.example.pathfinderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;

import java.util.ArrayList;

public class LangugesSelectionActivity extends AppCompatActivity implements LanguagesFragment.OnFragmentInteractionListener {

    private static final String PACKAGE_NAME = "com.example.pathfinderapp";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languges_selection);

        LanguagesFragment fragmentDemo = LanguagesFragment.newInstance(DefValues.defLanguages(), this);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, fragmentDemo);
        fragmentTransaction.commit();
        prefs = this.getSharedPreferences(
                PACKAGE_NAME, MODE_PRIVATE);
                /*(DemoFragment)
                getSupportFragmentManager().findFragmentById(R.id.frame_container);*/
        //above part is to determine which fragment is in your frame_container
        //setFragment(fragmentDemo);
    }

    public void changeFirstTimeStatus(){
        prefs.edit().putBoolean(getResources().getString(R.string.is_first_time), false).apply();
    }

    public void startMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction() {
    }
}
