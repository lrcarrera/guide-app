package com.example.pathfinderapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pathfinderapp.Adapters.AdapterProfile;
import com.example.pathfinderapp.Adapters.AdapterProfilePopUp;
import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.Adapters.CroppedImage;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_popup, container);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(this.guide.getImage() + "x2.png");

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
                    adapterTour.dismiss(view);
                }
            });
            final ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            try {
                final File localFile = File.createTempFile("images", "png");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        bitmap = CroppedImage.getCroppedBitmap(bitmap);
                        profilePicture.setImageBitmap(bitmap);
                        setProfileContent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {
                System.out.println("Error in profile dialog");
            }

        }
        return view;
    }

    private void setProfileContent(){

        final ArrayList<Post> profilePosts = new ArrayList<>();
        final ArrayList<Review> newReviews = new ArrayList<>();
        final ViewPager viewPager = view.findViewById(R.id.view_pager);

        final TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.reviewsTitle));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.toursTitle));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scoresTitle));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final FirebaseFirestore  db = FirebaseFirestore.getInstance();
        final ArrayList<Review> reviews = this.guide.getReviews();

        if(this.guide.getPostList() != null) {

            for (String post : this.guide.getPostList()) {
                db.collection("posts").whereEqualTo("uuid", post)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        profilePosts.add(new Post(document));
                                    }

                                    if(reviews != null && reviews.size() > 0){
                                        for(final Review review : reviews){
                                            db.collection("users").whereEqualTo("user.uid", review.getAutor() )
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                User user = null;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    user = new User(document);
                                                                    //DefValues.setDocumentReference(document.getReference());
                                                                    //DefValues.setUserInContext(document);
                                                                }
                                                                review.setAuthorInfo(user);
                                                                newReviews.add(review);
                                                                AdapterProfilePopUp tabsAdapter = new AdapterProfilePopUp(getChildFragmentManager(), tabLayout.getTabCount(), newReviews, profilePosts);
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
                                                                tabsAdapter.notifiyDataChanged();

                                                            } else {
                                                                Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        AdapterProfilePopUp tabsAdapter = new AdapterProfilePopUp(getChildFragmentManager(), tabLayout.getTabCount(), newReviews, profilePosts);
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
                                    }


                                } else {
                                    Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        } else {
            AdapterProfilePopUp tabsAdapter = new AdapterProfilePopUp(getChildFragmentManager(), tabLayout.getTabCount(), newReviews, profilePosts);
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
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


}
