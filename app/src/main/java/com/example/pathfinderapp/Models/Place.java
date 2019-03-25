package com.example.pathfinderapp.Models;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    private String name;
    private String country;
    private int picture;
    private LatLng coord;


    public Place(){}

    public Place(String name, String country, int picture) {
        this.name = name;
        this.country = country;
        this.picture = picture;
    }

    public Place(String name, String country, int picture, LatLng coord) {
        this.name = name;
        this.country = country;
        this.picture = picture;
        this.coord = coord;
    }

    public LatLng getCoord() {
        return coord;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
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
