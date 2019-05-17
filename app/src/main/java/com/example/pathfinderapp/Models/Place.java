package com.example.pathfinderapp.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Place(HashMap<String, Object> placesInHashMap) {
        this.name = (String) placesInHashMap.get("name");
        this.country = (String) placesInHashMap.get("country");
        this.picture = (int) placesInHashMap.get("picture");
        this.coord = (LatLng) placesInHashMap.get("coord");

    }

    /*public Place(HashMap<String, String> placeHashMap) {
        this.name = placeHashMap.get("name");
        this.country = placeHashMap.get("country");
        this.picture = Integer.parseInt(placeHashMap.get("picture"));
        String[] aux = placeHashMap.get("coord").split(",");
        String[] lat = aux[0].split("\\[");
        String[] lon = aux[1].split("]");
        long latitude = Long.parseLong(lat[1]);
        long longitude = Long.parseLong(lon[0]);
        this.coord = new LatLng(latitude, longitude);
    }*/

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
