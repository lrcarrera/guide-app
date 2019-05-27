package com.example.pathfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class LangugesSelectionActivity extends AppCompatActivity implements LanguagesFragment.OnFragmentInteractionListener {

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

                        } else {
                            Log.w("DB", "Error getting documents.", task.getException());
                        }
                    }
                });
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
