package com.example.pathfinderapp.ProfileTabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pathfinderapp.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class ProfileTab2Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_profile_secondtab, viewGroup, false);
    }
}