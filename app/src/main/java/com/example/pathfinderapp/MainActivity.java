package com.example.pathfinderapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pathfinderapp.PublishPackage.LanguagesFragment;
import com.example.pathfinderapp.PublishPackage.PriceFragment;
import com.example.pathfinderapp.PublishPackage.RouteSelectionFragment;
import com.example.pathfinderapp.PublishPackage.SummaryFragment;
import com.example.pathfinderapp.PublishPackage.TouristsAllowedFragment;
import com.example.pathfinderapp.PublishPackage.WhenFragment;
import com.example.pathfinderapp.PublishPackage.WhereFragment;
import com.example.pathfinderapp.PublishPackage.WhichTimeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ToursFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        PublishFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        WhenFragment.OnFragmentInteractionListener, WhereFragment.OnFragmentInteractionListener,
        WhichTimeFragment.OnFragmentInteractionListener, PriceFragment.OnFragmentInteractionListener,
        RouteSelectionFragment.OnFragmentInteractionListener, TouristsAllowedFragment.OnFragmentInteractionListener,
        LanguagesFragment.OnFragmentInteractionListener, SummaryFragment.OnFragmentInteractionListener
{
    SharedPreferences prefs;

    private final Fragment fragment1 = new SearchFragment();
    private final Fragment fragment2 = new ToursFragment();
    private final Fragment fragment3 = new PublishFragment();
    private final Fragment fragment4 = new ProfileFragment();
    private BottomNavigationView navigation;

    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;

    public void moveToToursPage(){
        ToursFragment aux = (ToursFragment) fragment2;
        aux.recyclerListChanged();
        fm.beginTransaction().hide(active).show(fragment2).commit();
        navigation.getMenu().getItem(1).setChecked(true);
        active = fragment2;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_tours:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.navigation_publish:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
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

        TextView mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();

        /*DefValues userInContext = new DefValues();
        userInContext.setPlayerInContext(new User());*/

        db.collection("users").whereEqualTo("user.uid", userUid )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            /*for (QueryDocumentSnapshot document : task.getResult()) {
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
                            LanguagesFragment fragmentDemo = LanguagesFragment.newInstance(languages, activity);

                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, fragmentDemo);
                            fragmentTransaction.commit();

                            prefs = getApplicationContext().getSharedPreferences(
                                    PACKAGE_NAME, MODE_PRIVATE);
                            */

                             /**{user={toursCound=0, image=0, score=0.0, uid=fJaIJJ14pzdeQj75e3OMIgdU5oR2,
                             languages=[{code=ES, flag=spanish_flag, added=true, name=Spanish, id=null,
                             picture=2131165417}, {code=EN, flag=english_flag, added=true, name=English,
                             id=null, picture=2131165341}], reviews=[{createdAt=Timestamp(seconds=1557828000, nanoseconds=0),
                             message=Buen tour, buenas vistas y nuevos lugares descubiertos, user={nom =Andreu Ibañez,
                             email=andreuibañez@gmail.com}}], postList=null, name=raduspaimoc@gmail.com, company=null}}*/
                           /* for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TESTSEXUAL", document.getId() + " => " + );
                               // DefValues.setPlayerInContext(document.getData());
                                List<String> userInContext = (List<String>) document.getData().get("user");

                                DefValues.setUserInContext(userInContext);

                            }
*/
                        } else {
                            Log.w("TESTNOSEXUAL", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void changeIcons(BottomNavigationView navigationView)
    {
        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_action_search);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_action_tours);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_action_publish);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_action_profile);
    }

    @Override
    public void onFragmentInteraction() {

    }
}
