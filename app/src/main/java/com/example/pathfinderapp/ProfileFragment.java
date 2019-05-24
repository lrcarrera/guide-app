package com.example.pathfinderapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathfinderapp.Adapters.AdapterLanguageHorizontal;
import com.example.pathfinderapp.Adapters.AdapterProfile;
import com.example.pathfinderapp.Adapters.AdapterTour;
import com.example.pathfinderapp.Adapters.CroppedImage;
import com.example.pathfinderapp.AsyncStuff.AsyncTaskLoadImage;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final String NO_PSSWRD = "no_password";

    private static final String PACKAGE_NAME = "com.example.pathfinderapp";

    private Dialog myDialog;

    private SharedPreferences prefs;
    private ImageView profilePicture;
    //TextView textViewEmail;

    private OnFragmentInteractionListener mListener;

    private CheckBox checkboxNotifications;
    private CheckBox checkboxFrench;
    private CheckBox checkboxEnglish;
    private CheckBox checkboxGerman;
    private CheckBox checkboxItalian;
    private CheckBox checkboxSpanish;

    private RadioButton radioWifi;
    private RadioButton radioWifiAndMore;

    //private static final String TAG = "MainActivity";

    private AdapterProfile mSectionsPageAdapter;
    private ViewPager mViewPager;

    private User user;

    private ProgressBar rotateLoading;

    private FirebaseAuth mAuth;

    private AdapterLanguageHorizontal adapterLanguages;


    private FirebaseFirestore db;
    ArrayList<Language> languages;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean isInProfilePage;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(boolean isInProfilePage) {


        ProfileFragment fragment = new ProfileFragment();
        fragment.isInProfilePage = isInProfilePage;
        //fragment.user = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView textViewName = rootView.findViewById(R.id.tv_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (isInProfilePage) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            textViewName.setText(user.getEmail());
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            myDialog = new Dialog(getContext());
            ImageButton settings = (ImageButton) rootView.findViewById(R.id.configure_button);

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSettings();
                }
            });


            profilePicture = rootView.findViewById(R.id.profilePicture);
            setProfilePicture();

            //From facebook login
            prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(
                    PACKAGE_NAME, Context.MODE_PRIVATE);



            tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.reviewsTitle));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.toursTitle));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.scoresTitle));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
            setTabsSubPages(DefValues.getUserRelatedPosts());





        }/* else {
            textViewName.setText(user.getName());
        }*/


        return rootView;
    }

    private void setProfilePicture(){
        User current = DefValues.getUserInContext();
        if (current == null)
            return;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(current.getImage() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    bitmap = CroppedImage.getCroppedBitmap(bitmap);
                    profilePicture.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            System.out.println("Error in bitmap-profile_fragment-picture " + e.getMessage());
        }


    }

    /*private ArrayList<Review> getUserReviewsInfo(ArrayList<Review> reviews){
        final ArrayList<Review> newReviews = new ArrayList<>();
        if(reviews != null){
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

                                } else {
                                    Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        }

        return newReviews;
    }*/

    public void setTabsSubPages(User user){

    }

    void setTabsSubPages(final ArrayList<Post> posts){
        if (DefValues.getUserInContext() == null)
            return;

        ArrayList<Review> reviews = DefValues.getUserInContext().getReviews();

        final ArrayList<Review> newReviews = new ArrayList<>();
        if(reviews != null){
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
                                    AdapterProfile tabsAdapter = new AdapterProfile(getFragmentManager(), tabLayout.getTabCount(), newReviews, posts, true);
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

                                } else {
                                    Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        }

    }

    private void addLanguages(final Dialog dialog) {

        // ArrayList<Language> userLanguages = DefValues.getUserInContext().getLanguages();

        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // String userUid = user.getUid();

        showProgress(true);
        db.collection("languages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST08", document.getId() + " => " + document.getData());
                            }

                            languages = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //    public Language(long id, String flag, String name, String code, int picture) {

                                languages.add(new Language(
                                        document.getId(),
                                        document.getString("flag"),
                                        document.getString("name"),
                                        document.getString("code"),
                                        document.getLong("picture").intValue()));
                            }

                            RecyclerView recycler = dialog.findViewById(R.id.languages);
                            recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
                            adapterLanguages = new AdapterLanguageHorizontal(languages, true, setLanguagesStatuses(languages));
                            recycler.setAdapter(adapterLanguages);
                            recycler.setItemAnimator(new DefaultItemAnimator());

                            handleActionButtons();
                            setInitialValuesToTogglesItems();



                            showProgress(false);
                        } else {
                            showProgress(false);
                            Log.w("TEST08", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void openSettings() {

        myDialog.setContentView(R.layout.settings_popup);
        checkboxNotifications = (CheckBox) myDialog.findViewById(R.id.checkbox_notifications);
        radioWifi = (RadioButton) myDialog.findViewById(R.id.wifi);
        rotateLoading = myDialog.findViewById(R.id.rotate_loading_popup);
        radioWifiAndMore = (RadioButton) myDialog.findViewById(R.id.wifiandmore);
        addLanguages(myDialog);


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void handleActionButtons() {

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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storePreferenceValues(languages);
            }
        });
    }

    private void setInitialValuesToTogglesItems() {

        Boolean isNotificationsOn = prefs.getBoolean(getResources().getString(R.string.notification), false);
        Boolean isFullConnectivityOn = prefs.getBoolean(getResources().getString(R.string.full_connectivity), false);

        if (isNotificationsOn) {
            checkboxNotifications.setChecked(true);
        } else {
            checkboxNotifications.setChecked(false);
        }

        if (isFullConnectivityOn) {
            radioWifi.setChecked(false);
            radioWifiAndMore.setChecked(true);
        } else {
            radioWifi.setChecked(true);
            radioWifiAndMore.setChecked(false);
        }


    }

    private boolean[] setLanguagesStatuses(ArrayList<Language> rootLanguages) {
        boolean[] bools = new boolean[rootLanguages.size()];
        if (DefValues.getUserInContext() == null || DefValues.getUserInContext().getLanguages() == null)
            return bools;

        ArrayList<Language> list = DefValues.getUserInContext().getLanguages();

        for (int i = 0; i < rootLanguages.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                boolean isSelectedByUser = list.get(j).getCode().equals(rootLanguages.get(i).getCode());
                if (isSelectedByUser) {
                    bools[i] = true;
                }
            }
        }

        return bools;
    }

    private void storePreferenceValues(ArrayList<Language> languages) {

        //Checkbox
        prefs.edit().putBoolean(getResources().getString(R.string.notification), checkboxNotifications.isChecked()).apply();
        prefs.edit().putBoolean(getResources().getString(R.string.full_connectivity), radioWifi.isChecked()).apply();

        boolean[] listPrefs = adapterLanguages.getCheckBoxesStatus();
        ArrayList<Language> languagesResult = new ArrayList<>();
        for (int i = 0; i < languages.size(); i++)
            if (listPrefs[i])
                languagesResult.add(languages.get(i));

        if (DefValues.getUserInContext() == null)
            return;

        DefValues.getUserInContext().setLanguages(languagesResult);
        DefValues.getUserInContextDocument().update("user.languages", languagesResult);

        myDialog.dismiss();
        Toast.makeText(getContext(), getResources().getString(R.string.configuration_stored_message), Toast.LENGTH_SHORT).show();
    }

    private void showProgress(final boolean show) {

        if (show) {
            rotateLoading.setVisibility(View.VISIBLE);
        } else {
            rotateLoading.setVisibility(View.GONE);
        }
    }

    private void logOut() {

        myDialog.dismiss();

        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(toLogin);

       /* if (AccessToken.getCurrentAccessToken() == null){// already logged out with fb
            prefs.edit().putString(getResources().getString(R.string.email), NO_EMAIL).apply();
            prefs.edit().putString(getResources().getString(R.string.password), NO_PSSWRD).apply();

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
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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

    public void setFacebookProfilePicture(String imageUrl) {
        InputStream in = null;

        try {
            Log.i("URL", imageUrl);
            URL url = new URL(imageUrl);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();

            in = httpConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        void onFragmentInteraction();
    }
}
