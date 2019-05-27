package com.example.pathfinderapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.pathfinderapp.Models.User;

public class GuideProfileActivity extends AppCompatActivity {

    User guide;

    public GuideProfileActivity(User guide){
        //this.guide = guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_profile);
    }
}
