package com.example.pathfinderapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_tours:
                    mTextMessage.setText(R.string.title_tours);
                    return true;
                case R.id.navigation_publish:
                    mTextMessage.setText(R.string.title_publish);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
