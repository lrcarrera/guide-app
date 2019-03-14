package com.example.pathfinderapp.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private List<Post> postList;
    private int toursCound;
    private String company;
    private float score;
    private ArrayList<Language> languages;

    public User(String name, List<Post> postList, int toursCound, String company, float score, ArrayList<Language> languages) {
        this.name = name;
        this.postList = postList;
        this.toursCound = toursCound;
        this.company = company;
        this.score = score;
        this.languages = languages;
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

    public List<Post> getPostList() {
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

    public void setCompany(String company) {
        this.company = company;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
