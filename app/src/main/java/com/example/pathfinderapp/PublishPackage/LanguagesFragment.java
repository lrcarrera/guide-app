package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IntegerRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pathfinderapp.Adapters.AdapterLanguage;
import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.PublishFragment;
import com.example.pathfinderapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LanguagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LanguagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PublishFragment parent;
    private LinearLayout containLayout;
    private List<Integer> toAdd;
    private FloatingActionButton continueButton;
    RecyclerView recycler;
    private OnFragmentInteractionListener mListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_languages, container, false);
        containLayout = (LinearLayout) view.findViewById(R.id.switchLayout);

        recycler = view.findViewById(R.id.languages);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        //placesList = new ArrayList<Place>();

        AdapterLanguage adapterLanguages = new AdapterLanguage(parent.user.getLanguages());
        adapterLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos =  recycler.getChildAdapterPosition(v);
                List<Language> aux = parent.user.getLanguages();
                Language language = aux.get(pos);
                language.setAdded(!language.isAdded());
                aux.set(pos, language);
            }
        });

        recycler.setAdapter(adapterLanguages);
        recycler.setItemAnimator(new DefaultItemAnimator());
        addContinueButton();
        return view;
    }

    private void addContinueButton()
    {
        continueButton = new FloatingActionButton(getContext());
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                addLanguagesToPost();
                nextStep();
            }
        });

        containLayout.addView(continueButton);
    }

    private void addLanguagesToPost()
    {
        ArrayList<Language> userLanguages = parent.user.getLanguages();
        ArrayList<Language> postLanguages = new ArrayList<>();
        for(Language language : userLanguages)
        {
            if(language.isAdded())
                postLanguages.add(language);
        }
        parent.post.setLanguages(postLanguages);
    }

    private void nextStep(){
        parent.setCurrentPage();
    }

    /*private void addSwitches(){
        int index = 0;
        switches = new ArrayList<>();
        for(Language currentLanguage : parent.user.getLanguages())
        {
            Switch sw = new Switch(getContext());
            sw.setId(index);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            sw.setLayoutParams(params);
            sw.setWidth(400);
            sw.setHeight(200);
            sw.setTextSize(18);
            sw.setGravity(Gravity.CENTER);
            //sw.setTrackDrawable(getResources().getDrawable(R.drawable.spain_flag));
            sw.setTrackTintMode(PorterDuff.Mode.DARKEN);
            changeSwitchImageAndText(sw, currentLanguage);
            switchesLayout.addView(sw);
            switches.add(sw);
            index++;

        }
    }

    private void changeSwitchImageAndText(Switch sw, Language language) {
        sw.setText(language.getName());
        switch (language.getName())
        {
            case "Spanish":
                sw.setThumbDrawable(getResources().getDrawable(R.drawable.spain_flag));
                break;
            case "French":
                sw.setThumbDrawable(getResources().getDrawable(R.drawable.french_flag));
                break;
            case "German":
                sw.setThumbDrawable(getResources().getDrawable(R.drawable.german_flag));
                break;
            case "Italian":
                sw.setThumbDrawable(getResources().getDrawable(R.drawable.italy_flag));
                break;
            case "English":
                sw.setThumbDrawable(getResources().getDrawable(R.drawable.english_flag));
                break;
        }
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
