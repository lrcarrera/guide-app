package com.example.pathfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangugesSelectionActivity extends AppCompatActivity implements LanguagesFragment.OnFragmentInteractionListener {

    private static final String PACKAGE_NAME = "com.example.pathfinderapp";
    private SharedPreferences prefs;
    ArrayList<Language> languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languges_selection);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        final LangugesSelectionActivity activity = this;

        db.collection("languages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

                            LanguagesFragment fragmentDemo = LanguagesFragment.newInstance(languages, activity);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, fragmentDemo);
                            fragmentTransaction.commit();
                            prefs = getApplicationContext().getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);

                        } else {
                            Log.w("TEST08", "Error getting documents.", task.getException());
                        }
                    }
                });
                /*(DemoFragment)
                getSupportFragmentManager().findFragmentById(R.id.frame_container);*/
        //above part is to determine which fragment is in your frame_container
        //setFragment(fragmentDemo);
    }

   /* private void storeFlagImageFromFirebaseStorage(int picture, final ArrayList<Language> languages) {



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(picture + ".png");

        try {
            final File localFile = File.createTempFile("images_flag", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    DefValues.addFlagFromFirebaseStorage(bitmap);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w("TEST10", "Error getting flag image .");

                }
            });
        } catch (IOException e ) {
            //System.out.println("Vergassso");
        }
    }*/

    /*
    private void processProfilePicture(Post current, final ViewHolderItem viewHolder){
        //Bitmap bitmap = null;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(current.getGuide().getImage() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    bitmap = CroppedImage.getCroppedBitmap(bitmap);
                    viewHolder.picture.setImageBitmap(bitmap);
                    viewHolder.topPicture.setImageBitmap(bitmap);
                    //mImageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            //System.out.println("Vergassso");
        }
        /*bitmap = BitmapFactory.decodeResource(context.getResources());
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user);
        }*/
/*}*/
    public void startMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction() {
    }
}
