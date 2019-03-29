package com.example.pathfinderapp.MockValues;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

    public static ArrayList<Post> createMockList(){
        ArrayList<Post> list = new ArrayList<Post>();

        User user1 = new User();
        user1.setScore(1.0f);
        user1.setName("Bonifacia la piedra");
        user1.setImage(R.drawable.stock_girl);



        Post post1 = new Post();
        post1.setGuide(user1);
        post1.setPrice(30.0f);
        post1.setDueTo(new Date());
        post1.setStartHour("19:00");
        post1.setEndHour("21:00");
        post1.setNumTourists(6);
        post1.setPrice(14.5f);
        post1.setLanguages(DefValues.DefLanguages());

        User user2 = new User();
        user2.setScore(5.0f);
        user2.setName("Concha Mas");
        user2.setImage(R.drawable.stock_man);

        Post post2 = new Post();
        post2.setGuide(user2);
        post2.setPrice(30.0f);
        post2.setDueTo(new Date());
        post2.setStartHour("12:00");
        post2.setEndHour("14:00");
        post2.setNumTourists(6);
        post2.setPrice(14.5f);
        post2.setLanguages(DefValues.DefLanguages());

        User user3 = new User();
        user3.setScore(5.0f);
        user3.setName("Cubru Tivisoaro");
        user3.setImage(R.drawable.stock_girl);

        Post post3 = new Post();
        post3.setGuide(user3);
        post3.setPrice(30.0f);
        post3.setDueTo(new Date());
        post3.setStartHour("21:00");
        post3.setEndHour("24:00");
        post3.setNumTourists(6);
        post3.setPrice(14.5f);
        post3.setLanguages(DefValues.DefLanguages());

        list.add(post1);
        list.add(post2);
        list.add(post3);
        return list;
    }
}
