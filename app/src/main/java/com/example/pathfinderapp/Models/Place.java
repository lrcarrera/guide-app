package com.example.pathfinderapp.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("country", country);
        data.put("picture", picture);
        data.put("coord", coord);
        return data;
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
