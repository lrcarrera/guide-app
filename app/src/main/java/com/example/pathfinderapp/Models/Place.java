package com.example.pathfinderapp.Models;

public class Place {

    private String name;
    private String country;
    private int picture;

    public Place(){}

    public Place(String name, String country, int picture) {
        this.name = name;
        this.country = country;
        this.picture = picture;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
