package com.example.pathfinderapp.PublishPackage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IntegerRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
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
    private AdapterLanguage adapterLanguages;
    private FloatingActionButton continueButton;
    RecyclerView recycler;
    View view;
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
        view = inflater.inflate(R.layout.fragment_languages, container, false);
        containLayout = (LinearLayout) view.findViewById(R.id.switchLayout);

        recycler = view.findViewById(R.id.languages);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        //placesList = new ArrayList<Place>();

        adapterLanguages = new AdapterLanguage(parent.user.getLanguages());
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                int added = addLanguagesToPost();
                if (added > 0){
                    nextStep();
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) getActivity().findViewById(R.id.toastRoot));

                    ImageView image = (ImageView) layout.findViewById(R.id.imageId);
                    image.setImageResource(R.drawable.english_flag);
                    TextView text = (TextView) layout.findViewById(R.id.ItemTitle);
                    text.setText("English");
                    TextView text2 = (TextView) layout.findViewById(R.id.ItemInfo);
                    text2.setText("EN");
                    View aux = layout.findViewById(R.id.image);
                    aux.setBackgroundColor(Color.parseColor("#b6fcd5"));

                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.BOTTOM |Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }

            }
        });

        containLayout.addView(continueButton);
    }

    private int addLanguagesToPost()
    {
        ArrayList<Language> userLanguages = parent.user.getLanguages();
        ArrayList<Language> postLanguages = new ArrayList<>();
        for(Language language : userLanguages)
        {
            if(language.isAdded())
                postLanguages.add(language);
        }
        parent.post.setLanguages(postLanguages);
        return postLanguages.size();
    }

    private void nextStep(){
        parent.setCurrentPage();
    }

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
