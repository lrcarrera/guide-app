package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import com.example.pathfinderapp.LangugesSelectionActivity;
import com.example.pathfinderapp.LoginActivity;
import com.example.pathfinderapp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pathfinderapp.Adapters.AdapterLanguage;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LanguagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LanguagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguagesFragment extends Fragment implements INexStep {

    private final String ENGLISH = "English";
    private final String EN = "EN";
    private final String DEFAULT_COLOR = "#b6fcd5";
    private PublishFragment parent;
    private LinearLayout containLayout;
    private RecyclerView recycler;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Language> languages;
    private LangugesSelectionActivity act;
    private AdapterLanguage adapterLanguages;

    public LanguagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent Parameter 1.
     * @return A new instance of fragment LanguagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LanguagesFragment newInstance(PublishFragment parent) {
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.parent = parent;
        return fragment;
    }

    public static LanguagesFragment newInstance(ArrayList<Language> languages, LangugesSelectionActivity act) {
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.languages = languages;
        fragment.act = act;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_languages, container, false);
        containLayout = (LinearLayout) view.findViewById(R.id.switchLayout);


        recycler = view.findViewById(R.id.languages);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        if (languages == null) {
            adapterLanguages = new AdapterLanguage(parent.user.getLanguages());
        } else {
            adapterLanguages = new AdapterLanguage(languages);
            TextView textView = view.findViewById(R.id.whenTitle);
            textView.setText(R.string.speaking_languages);
        }
        adapterLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recycler.getChildAdapterPosition(v);
                ArrayList<Language> aux = languages == null ?  parent.user.getLanguages() : languages;

                Language language = aux.get(pos);
                language.setAdded(!language.isAdded());
                aux.set(pos, language);

                if (languages != null)
                    languages = aux;
            }
        });

        recycler.setAdapter(adapterLanguages);
        recycler.setItemAnimator(new DefaultItemAnimator());
        addContinueButton();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void addContinueButton() {
        FloatingActionButton continueButton = new FloatingActionButton(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.bottomMargin = 20;
        params.rightMargin = 20;
        continueButton.setLayoutParams(params);
        continueButton.setForegroundGravity(Gravity.END);
        continueButton.setImageResource(R.drawable.ic_action_next);
        continueButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
        continueButton.setClickable(true);
        continueButton.setFocusable(true);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languages == null) {
                    int added = addLanguagesToPost();
                    if (added > 0) {
                        nextStep();
                    } else {
                        showErrorMessage();
                    }
                } else {
                    int added = addLanguagesToPost();
                    if (added > 0) {
                        act.startMainPage();
                    } else {
                        showErrorMessage();
                    }
                }
            }
        });

        containLayout.addView(continueButton);
    }

    private void showErrorMessage() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) getActivity().findViewById(R.id.toastRoot));

        ImageView image = (ImageView) layout.findViewById(R.id.imageId);
        image.setImageResource(R.drawable.english_flag);
        TextView text = (TextView) layout.findViewById(R.id.ItemTitle);
        text.setText(ENGLISH);
        TextView text2 = (TextView) layout.findViewById(R.id.ItemInfo);
        text2.setText(EN);
        View aux = layout.findViewById(R.id.image);
        aux.setBackgroundColor(Color.parseColor(DEFAULT_COLOR));

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    private int addLanguagesToPost() {
        ArrayList<Language> userLanguages = languages == null ?  parent.user.getLanguages() : languages;;

        ArrayList<Language> postLanguages = new ArrayList<>();
        for (Language language : userLanguages) {
            if (language.isAdded())
                postLanguages.add(language);
        }
        if (languages == null) {
            parent.post.setLanguages(postLanguages);
        } else {
            saveLanguagesInDataBase(postLanguages);
        }
        return postLanguages.size();
    }

    private void saveLanguagesInDataBase(ArrayList<Language> languages) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();
        String name = user.getEmail();

        Map<String, User> newUser = new HashMap<>();
        newUser.put("user", new User(userUid, name, null, 0, null, 0, languages, 0, null));

        db.collection("users")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB", "Error adding document", e);
                        Toast.makeText(getActivity(),
                                "Server not found (Error: 400)",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                });
    }

    public void nextStep() {
        parent.setCurrentPage();
    }

    public void onButtonPressed() {
        if (mListener != null)
            mListener.onFragmentInteraction();
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
