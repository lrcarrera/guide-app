package com.example.pathfinderapp.MockValues;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DefValues {

    public static ArrayList<Language> DefLanguages(){
        return new ArrayList<Language>(Arrays.asList(
                new Language("spanish_flag", "Spanish", "ES", R.drawable.spain_flag),
                new Language("english_flag", "English", "EN",R.drawable.english_flag),
                new Language("french_flag" , "French" , "FR",R.drawable.french_flag),
                new Language("italian_flag", "Italian", "IT",R.drawable.italy_flag),
                new Language("german_flag" , "German" , "DE",R.drawable.german_flag)
        ));
    }
}
