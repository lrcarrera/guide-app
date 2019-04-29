package com.example.pathfinderapp.MockValues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.sql.RowSetEvent;

public class DefValues {

    private static ArrayList<Post> POSTS;
    private static ArrayList<Post> YOUR_POSTS;
    private static ArrayList<Language> LANGUAGES;
    private static User DEF_USER;
    private static ArrayList<Place> PLACES;
    private static List<Review> REVIEWS;

    private static ArrayList<Language> defLanguages(){
        if(LANGUAGES == null){
            LANGUAGES = new ArrayList<Language>(Arrays.asList(
                    new Language("spanish_flag", "Spanish", "ES", R.drawable.spain_flag),
                    new Language("english_flag", "English", "EN",R.drawable.english_flag),
                    new Language("french_flag" , "French" , "FR",R.drawable.french_flag),
                    new Language("italian_flag", "Italian", "IT",R.drawable.italy_flag),
                    new Language("german_flag" , "German" , "DE",R.drawable.german_flag)
            ));
        }
        return LANGUAGES;
    }

    public static User defUser(){
        if(DEF_USER == null){
            DEF_USER = new User();
            DEF_USER.setScore(1.0f);
            DEF_USER.setName("Llui Spaimoc Rosales");
            DEF_USER.setImage(R.drawable.ic_user);
            DEF_USER.setLanguages(DefValues.defLanguages());
            //DEF_USER.setReviews(getMockReviews());
        }
        return DEF_USER;
    }

    public static ArrayList<Place> defPlaces(){
        if(PLACES == null){
            /*  NEW york new LatLng(40.750580, -73.993584),11)*/
            PLACES = new ArrayList<>();
            PLACES.add(new Place("Ubicación Actual", "", R.drawable.ic_action_mylocation, new LatLng(41.6082387, 0.6212267)));
            PLACES.add(new Place("Lleida", "Spain", R.drawable.ic_action_place, new LatLng(41.6082387, 0.6212267)));
            PLACES.add(new Place("Barcelona", "Spain", R.drawable.ic_action_place, new LatLng(41.3948975, 2.0785566)));
            PLACES.add(new Place("Paris", "France", R.drawable.ic_action_place, new LatLng(48.8589506, 2.2768488)));
            PLACES.add(new Place("London", "United Kingdom", R.drawable.ic_action_place, new LatLng(51.528308, -0.3817765)));
        }
        return PLACES;
    }

    public static ArrayList<Post> getMockPostList(){
        if(POSTS == null){
            POSTS = new ArrayList<Post>();
            ArrayList<User> users = new ArrayList<User>();
            users.add(new User());
            users.add(new User());
            users.add(new User());
            users.add(new User());
            users.add(new User());
            users.add(new User());
            final User user1 = new User();
            user1.setScore(5.0f);
            user1.setName("Maria Carrera");
            user1.setImage(R.drawable.stock_girl);



            Post post1 = new Post();
            post1.setGuide(user1);
            post1.setPrice(30.0f);
            post1.setDueTo(new Date());
            post1.setStartHour("19:00");
            post1.setEndHour("21:00");
            post1.setNumTourists(6);
            post1.setPrice(14.5f);
            post1.setPlaces(new ArrayList<Marker>());
            post1.setLanguages(DefValues.defLanguages());
            post1.setTourists(new ArrayList<User>());
            post1.setPlace(new Place("Lleida", "Spain", R.drawable.ic_action_place, new LatLng(41.6082387, 0.6212267)));

            User user2 = new User();
            user2.setScore(12.0f);
            user2.setName("Concha Mas");
            user2.setImage(R.drawable.stock_girl1);

            Post post2 = new Post();
            post2.setGuide(user2);
            post2.setPrice(30.0f);
            post2.setDueTo(new Date());
            post2.setStartHour("12:00");
            post2.setEndHour("14:00");
            post2.setNumTourists(8);
            post2.setPrice(14.5f);
            post2.setPlaces(new ArrayList<Marker>());
            post2.setLanguages(DefValues.defLanguages());
            post2.setTourists(users);
            post2.setPlace(new Place("Barcelona", "Spain", R.drawable.ic_action_place, new LatLng(41.3948975, 2.0785566)));

            User user3 = new User();
            user3.setScore(7.0f);
            user3.setName("Ramon Polanksi");
            user3.setImage(R.drawable.stock_man);

            Post post3 = new Post();
            post3.setGuide(user3);
            post3.setPrice(30.0f);
            post3.setDueTo(new Date());
            post3.setStartHour("21:00");
            post3.setEndHour("24:00");
            post3.setNumTourists(6);
            post3.setPrice(14.5f);
            post3.setTourists(users);
            post3.setPlaces(new ArrayList<Marker>());
            post3.setLanguages(DefValues.defLanguages());
            post3.setPlace(new Place("Paris", "France", R.drawable.ic_action_place, new LatLng(48.8589506, 2.2768488)));


            User user4 = new User();
            user4.setScore(1.0f);
            user4.setName("Andreu Ibañez");
            user4.setImage(R.drawable.stock_man);



            Post post4 = new Post();
            post4.setGuide(user4);
            post4.setPrice(30.0f);
            post4.setDueTo(new Date());
            post4.setStartHour("9:00");
            post4.setEndHour("13:00");
            post4.setNumTourists(6);
            post4.setPrice(14.5f);
            post4.setTourists(users);
            post4.setPlaces(new ArrayList<Marker>());
            post4.setLanguages(DefValues.defLanguages());
            post4.setPlace(new Place("New York", "EEUU", R.drawable.ic_action_place, new LatLng(40.750580, -73.993584)));

            POSTS.add(post1);
            POSTS.add(post2);
            POSTS.add(post3);
            POSTS.add(post4);
        }
        return POSTS;
    }

    public static void AddPostToToursList(Post post){
        if(YOUR_POSTS == null){
            getMockYourToursList();
        }
        YOUR_POSTS.add(post);
    }


    public static ArrayList<Post> getMockYourToursList(){
        if(YOUR_POSTS == null){
            ArrayList<User> users = new ArrayList<User>();
            users.add(new User());
            YOUR_POSTS = new ArrayList<Post>();
            User user1 = new User();
            user1.setScore(5.0f);
            user1.setName("Maria Carrera");
            user1.setImage(R.drawable.stock_girl);



            Post post1 = new Post();
            post1.setGuide(user1);
            post1.setPrice(30.0f);
            post1.setDueTo(new Date());
            post1.setStartHour("19:00");
            post1.setEndHour("21:00");
            post1.setNumTourists(6);
            post1.setPrice(14.5f);
            post1.setTourists(users);
            post1.setLanguages(DefValues.defLanguages());
            post1.setPlace(new Place("Lleida", "Spain", R.drawable.ic_action_place, new LatLng(41.6082387, 0.6212267)));
            post1.setPlaces(new ArrayList<Marker>());

            User user2 = new User();
            user2.setScore(12.0f);
            user2.setName("Ramona Mas");
            user2.setImage(R.drawable.stock_girl1);

            Post post2 = new Post();
            post2.setGuide(user2);
            post2.setPrice(30.0f);
            post2.setDueTo(new Date());
            post2.setStartHour("12:00");
            post2.setEndHour("14:00");
            post2.setNumTourists(6);
            post2.setPrice(14.5f);
            post2.setTourists(users);
            post2.setLanguages(DefValues.defLanguages());
            post2.setPlace(new Place("Barcelona", "Spain", R.drawable.ic_action_place, new LatLng(41.3948975, 2.0785566)));
            post2.setPlaces(new ArrayList<Marker>());

            User user3 = new User();
            user3.setScore(7.0f);
            user3.setName("Hodor Lannister");
            user3.setImage(R.drawable.stock_man);

            Post post3 = new Post();
            post3.setGuide(user3);
            post3.setPrice(30.0f);
            post3.setDueTo(new Date());
            post3.setStartHour("21:00");
            post3.setEndHour("24:00");
            post3.setNumTourists(6);
            post3.setPrice(14.5f);
            post3.setTourists(users);
            post3.setLanguages(DefValues.defLanguages());
            post3.setPlace(new Place("Paris", "France", R.drawable.ic_action_place, new LatLng(48.8589506, 2.2768488)));
            post3.setPlaces(new ArrayList<Marker>());


            User user4 = new User();
            user4.setScore(1.0f);
            user4.setName("Shreck Ibañez");
            user4.setImage(R.drawable.stock_man);



            Post post4 = new Post();
            post4.setGuide(user4);
            post4.setPrice(30.0f);
            post4.setDueTo(new Date());
            post4.setStartHour("9:00");
            post4.setEndHour("13:00");
            post4.setNumTourists(6);
            post4.setPrice(14.5f);
            post4.setTourists(users);
            post4.setLanguages(DefValues.defLanguages());
            post4.setPlace(new Place("New York", "EEUU", R.drawable.ic_action_place, new LatLng(40.750580, -73.993584)));
            post4.setPlaces(new ArrayList<Marker>());

            YOUR_POSTS.add(post1);
            YOUR_POSTS.add(post2);
            YOUR_POSTS.add(post3);
            YOUR_POSTS.add(post4);
        }
        return YOUR_POSTS;
    }

    public static List<Review> getMockReviews(){
        if(REVIEWS == null){
            User guide = getMockPostList().get(0).getGuide();
            Review aux = new Review("Muy buen tour, guía majisimo oye!", guide, new Date());
            Review aux2 = new Review("Muy buen tour1, guía majisimo oye!", guide, new Date());
            Review aux3 = new Review("Muy buen tour2, guía majisimo oye!", guide, new Date());
            REVIEWS = new ArrayList<Review>();
            REVIEWS.add(aux);
            REVIEWS.add(aux2);
            REVIEWS.add(aux3);

        }
        return REVIEWS;

    }
}
