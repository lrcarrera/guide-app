package com.example.pathfinderapp.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Post implements Serializable {

    private Date createdAt;
    private Date dueTo;
    private String startHour;
    private String endHour;
    private User guide;
    private int numTourists;
    private Place place;
    private List<User> tourists;
    private ArrayList<Language> languages;
    private ArrayList<Marker> places;
    private Float price;
    private String uuid;


    public Post(Date createdAt, Date dueTo,
                String startHour, String endHour,
                User guide, int numTourists, Place place,
                List<User> tourists, ArrayList<Language> languages,
                ArrayList<Marker> places, Float price) {

        this.createdAt = createdAt;
        this.dueTo = dueTo;
        this.startHour = startHour;
        this.endHour = endHour;
        this.guide = guide;
        this.numTourists = numTourists;
        this.place = place;
        this.tourists = tourists;
        this.languages = languages;
        this.places = places;
        this.price = price;
        this.uuid = this.guide.getUid() + this.guide.getScore() + 1;
    }

    public Post (QueryDocumentSnapshot doc){
        this.createdAt = (Date) doc.get("createdAt");
        dueTo = doc.getTimestamp("dueTo").toDate();
        startHour = doc.getString("startHour");
        endHour = doc.getString("endHour");
        price = Float.parseFloat(doc.getLong("price").toString());
        numTourists = Integer.parseInt(doc.getLong("numTourists").toString());
        uuid = doc.getString("uuid");

        ArrayList<HashMap<String, Object>> languagesHash  = (ArrayList<HashMap<String, Object>>) doc.get("languages");
        languages = new ArrayList<>();
        for (HashMap<String, Object> languageHash : languagesHash) {
            languages.add(new Language(languageHash));
        }



        HashMap<String, Map<String, Long>> locations = (HashMap<String, Map<String, Long>>) doc.get("place.coord");
        Collection collection = locations.values();
        List<Double> list = new ArrayList<>(collection);
        Double lat = list.get(0);
        Double lon = list.get(1);
        place = new Place(doc.getString("place.name"),
                          doc.getString("place.country"),
                          Integer.parseInt(doc.getLong("place.picture").toString()),
                          new LatLng(lat, lon));


        places = (ArrayList<Marker>) doc.get("places");
        guide = new User((HashMap<String, Object>) doc.get("guide"));

        /**Falta por implementar se tienen que catcher bien*/
        this.tourists = new ArrayList<>();
        this.tourists.add(guide);


    }

    public Post() {
        this.price = 0f;
        this.numTourists= 1;
        this.places = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public ArrayList<Marker> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Marker> places) {
        this.places = places;
    }

    /*public void setPrice(Float price) {
        this.price = price;
    }*/

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("createdAt", this.createdAt);
        data.put("dueTo", this.dueTo);
        data.put("startHour", this.startHour);
        data.put("endHour", this.endHour);
        data.put("guide", this.guide);
        data.put("numTourists", this.numTourists);
        data.put("place", this.place);
        data.put("tourists", this.tourists);
        data.put("languages", this.languages);
        data.put("places", this.places);
        data.put("price", this.price);
        return data;
    }


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

    public void addTourist(User user){this.tourists.add(user);}

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
