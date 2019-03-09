package com.example.pathfinderapp.Models;

import java.util.List;

public class User {

    private String name;
    private List<Post> postList;
    private int toursCound;
    private String company;
    private float score;

    public User(String name, List<Post> postList, int toursCound, String company, float score) {
        this.name = name;
        this.postList = postList;
        this.toursCound = toursCound;
        this.company = company;
        this.score = score;
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
