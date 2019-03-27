package com.example.pathfinderapp.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private Date createdAt;
    private Date dueTo;
    private String startHour;
    private String endHour;
    private User guide;
    private int numTourists;
    private Place place;
    private List<User> tourists;
    private ArrayList<Language> languages;
    private Float price;

    public Post(Date createdAt, Date dueTo, String startHour, String endHour, User guide, int numTourists, Place place, List<User> tourists, ArrayList<Language> languages, Float price) {
        this.createdAt = createdAt;
        this.dueTo = dueTo;
        this.startHour = startHour;
        this.endHour = endHour;
        this.guide = guide;
        this.numTourists = numTourists;
        this.place = place;
        this.tourists = tourists;
        this.languages = languages;
        this.price = price;
    }

    public Post() { this.price = 0f;}

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getNumTourists() {
        return numTourists;
    }

    public void setNumTourists(int numTourists) {
        this.numTourists = numTourists;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDueTo() {
        return dueTo;
    }

    public void setDueTo(Date dueTo) {
        this.dueTo = dueTo;
    }

    public User getGuide() {
        return guide;
    }

    public void setGuide(User guide) {
        this.guide = guide;
    }

    public List<User> getTourists() {
        return tourists;
    }

    public void setTourists(List<User> tourists) {
        this.tourists = tourists;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStartHour() { return this.startHour; }

    public void setStartHour(String startHour) { this.startHour = startHour; }

    public String getEndHour() { return  this.endHour; }

    public void setEndHour(String endHour) { this.endHour = endHour; }
}
