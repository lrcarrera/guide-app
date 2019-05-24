package com.example.pathfinderapp.Models;

import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.facebook.internal.CollectionMapper;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzac;
import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.internal.maps.zzw;
import com.google.android.gms.internal.maps.zzz;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.IProjectionDelegate;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.internal.zzab;
import com.google.android.gms.maps.internal.zzad;
import com.google.android.gms.maps.internal.zzaf;
import com.google.android.gms.maps.internal.zzaj;
import com.google.android.gms.maps.internal.zzal;
import com.google.android.gms.maps.internal.zzan;
import com.google.android.gms.maps.internal.zzap;
import com.google.android.gms.maps.internal.zzar;
import com.google.android.gms.maps.internal.zzat;
import com.google.android.gms.maps.internal.zzav;
import com.google.android.gms.maps.internal.zzax;
import com.google.android.gms.maps.internal.zzaz;
import com.google.android.gms.maps.internal.zzbb;
import com.google.android.gms.maps.internal.zzbd;
import com.google.android.gms.maps.internal.zzbf;
import com.google.android.gms.maps.internal.zzbs;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.internal.zzh;
import com.google.android.gms.maps.internal.zzl;
import com.google.android.gms.maps.internal.zzp;
import com.google.android.gms.maps.internal.zzr;
import com.google.android.gms.maps.internal.zzv;
import com.google.android.gms.maps.internal.zzx;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
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
    private ArrayList<MarkerOptions> markerOptions;
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


        //places = (ArrayList<Marker>) doc.get("places");
        if(markerOptions == null)
            markerOptions = new ArrayList<>();

        guide = new User((HashMap<String, Object>) doc.get("guide"));

        /**Falta por implementar se tienen que catcher bien*/
        this.tourists = new ArrayList<>();
        this.tourists.add(guide);

        ArrayList<Map<String, Object>> collection1 = ((ArrayList<Map<String, Object>>)doc.get("places"));

        if(collection1 == null)
            return;


        for(Object obj : collection1){
            HashMap <String, Object> aux = (HashMap<String, Object>)obj;
            if(aux == null)
                continue;
            HashMap<String, Map<String, Double>> position = (HashMap<String, Map<String, Double>>) aux.get("position");
            List<Double> collection2 = new ArrayList(position.values());

            Double lat2 = collection2.get(0);
            Double lon2 = collection2.get(1);
            markerOptions.add(new MarkerOptions().position(new LatLng(lat2, lon2)));
            //Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat2, lon2)));
            //Marker marker = new Marker(new MarkerOptions().position(new LatLng(lat2, lon2)));
            //marker.setPosition(new LatLng(lat2,lon2));
            //places.add(marker);
            System.out.println("GAAAAAAAAy");

            //Marker marker = (Marker) aux.values();
            //places.add(marker);
        }




    }

    public Post() {
        this.price = 0f;
        this.numTourists= 1;
        this.places = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    public ArrayList<MarkerOptions> getMarkerOptions(){
        return this.markerOptions;
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

    public Map<String, Object> addToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("createdAt", this.createdAt);
        data.put("dueTo", this.dueTo);
        data.put("startHour", this.startHour);
        data.put("endHour", this.endHour);
        data.put("guide", this.guide.addToHashMap());
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
