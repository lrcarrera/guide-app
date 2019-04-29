package com.example.pathfinderapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.Adapters.CroppedImage;
import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.Models.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileDialog extends DialogFragment{


    private User guide;
    private FragmentManager fragmentManager;
    private AdapterTour.ViewHolderItem adapterTour;
    private static View view;
    private Place place;

    public ProfileDialog(){}

    public ProfileDialog(User user, FragmentManager fragmentManager, View view, AdapterTour.ViewHolderItem adapterTour, Place place)
    {
        this.guide = user;
        this.fragmentManager = fragmentManager;
        this.adapterTour = adapterTour;
        this.place = place;
        //this.view = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_popup, container);
        if(guide != null){
            TextView textViewName = view.findViewById(R.id.tv_name);
            TextView textViewAddress = view.findViewById(R.id.tv_address);
            textViewName.setText(guide.getName());
            String aux = place.getName() + ", " + place.getCountry();
            textViewAddress.setText(aux);
            ImageButton settings = (ImageButton) view.findViewById(R.id.configure_button);
            settings.setBackground(null);
            settings.setImageResource(R.drawable.ic_action_cancel);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dismiss();
                    adapterTour.dismiss(view);
                }
            });
            ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), guide.getImage());
            bitmap = CroppedImage.getCroppedBitmap(bitmap);
            profilePicture.setImageBitmap(bitmap);

        }

        //Fragment fragment = ProfileFragment.newInstance(guide);
        //LinearLayout rowLayout =view.findViewById(R.id.profile_popup_linear);
        //rowLayout.setId(whateveryouwantasid);
// add rowLayout to the root layout somewhere here

        //FragmentManager fragMan = getFragmentManager();
        //FragmentTransaction fragTransaction = fragMan.beginTransaction();

        //ragment myFrag = new ImageFragment();
        //fragTransaction.add(rowLayout.getId(), myFrag , "fragment" + fragCount);
        //fragTransaction.commit();
        /*fragmentManager
                .beginTransaction()
                .add(rowLayout.getId(), fragment)
                .commit();*/
        //Fragment fragmentToRemplace = view.findViewById(R.id.profile_popup_fragment);
        //mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        //getDialog().setTitle("Hello");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    /*public ProfileDialog(@NonNull Context context) {
        super(context);
    }*/

    //public ProfileDialog(){}
    /*public static ProfileDialog newInstance(User user, FragmentManager fragmentManager, Context context) {
        ProfileDialog fragment = new ProfileDialog(context);
        fragment.guide = user;
        fragment.fragmentManager = fragmentManager;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View v = inflater.inflate(R.layout.profile_popup, container, false);
        //v.setBackgroundColor(Color.TRANSPARENT);
    }

    public void aux(){
        Fragment fragment = ProfileFragment.newInstance(guide);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.profile_layout, fragment);
        fragmentTransaction.commit();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_popup, container, false);
        v.setBackgroundColor(Color.TRANSPARENT);
        Fragment fragment = ProfileFragment.newInstance(guide);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.profile_layout, fragment);
        // Do all the stuff to initialize your custom view

        return v;
    }*/


}
