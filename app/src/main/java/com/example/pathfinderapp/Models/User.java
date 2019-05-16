package com.example.pathfinderapp.Models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private String uid;
    private String name;
    private List<Post> postList;
    private int toursCound;
    private String company;
    private float score;
    private ArrayList<Language> languages;
    private int image;
    private ArrayList<Review> reviews;


    public User(String name, List<Post> postList, int toursCound, String company, float score, ArrayList<Language> languages, int image) {
        this.name = name;
        this.postList = postList;
        this.toursCound = toursCound;
        this.company = company;
        this.score = score;
        this.languages = languages;
        this.image = image;
    }

    public User(String uid, String name, List<Post> postList, int toursCound, String company, float score, ArrayList<Language> languages, int image, ArrayList<Review> reviews) {
        this.uid = uid;
        this.name = name;
        this.postList = postList;
        this.toursCound = toursCound;
        this.company = company;
        this.score = score;
        this.languages = languages;
        this.image = image;
        this.reviews = reviews;
    }

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("uid", this.uid);
        data.put("name", this.name);
        data.put("postList", this.postList);
        data.put("toursCound", this.toursCound);
        data.put("company", this.company);
        data.put("score", this.score);
        data.put("languages", this.languages);
        data.put("image", this.image);
        data.put("reviews", this.reviews);
        return data;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public User() { }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Post> getPostList() {
        return postList;
    }

    public int getToursCound() {
        return toursCound;
    }

    public String getCompany() {
        return company;
    }

    public float getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public void setToursCound(int toursCound) {
        this.toursCound = toursCound;
    }

    public void setCompany(
            String company) {
        this.company = company;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
