package com.example.pathfinderapp.Models;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class User implements Serializable {

    private String uid;
    private String name;
    private ArrayList<String> postList;
    private int toursCound;
    private String company;
    private float score;
    private ArrayList<Language> languages;
    private int image;
    private ArrayList<Review> reviews;
    private String messageToken;


    public User(String name, ArrayList<String> postList, int toursCound, String company, float score, ArrayList<Language> languages, int image) {
        this.name = name;
        this.postList = postList;
        this.toursCound = toursCound;
        this.company = company;
        this.score = score;
        this.languages = languages;
        this.image = image;
    }

    public User(String uid, String name, ArrayList<String> postList, int toursCound, String company, float score, ArrayList<Language> languages, int image, ArrayList<Review> reviews) {
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

    public User (QueryDocumentSnapshot doc){
        this.toursCound = doc.getLong("user.toursCound").intValue();
        ArrayList<HashMap<String, Object>> languagesHash  = (ArrayList<HashMap<String, Object>>) doc.get("user.languages");
        languages = new ArrayList<>();
        if(languagesHash != null){
            for (HashMap<String, Object> languageHash : languagesHash) {
                languages.add(new Language(languageHash));
            }
        }

        // ArrayList<Language> languages = (ArrayList<Language>) doc.get("user.languages");
        //reviews = (ArrayList<Review>) doc.get("user.reviews");
        postList = (ArrayList<String>) doc.get("user.postList");
        name = doc.getString("user.name");
        company = doc.getString("user.company");
        uid = doc.getString("user.uid");
        score = doc.getLong("user.score");
        image = doc.getLong("user.image").intValue();
        ArrayList<HashMap<String, Object>> reviews = (ArrayList<HashMap<String, Object>>) doc.get("user.reviews");
        if(this.reviews == null)
            this.reviews = new ArrayList<>();

        if(reviews != null){

            for (HashMap<String, Object> review : reviews){
                this.reviews.add(new Review(review));
            }
            System.out.println(reviews);
        }
    }

    public User(HashMap<String, Object> userInHashMap) {
        this.uid = (String) userInHashMap.get("uid");
        this.name = (String) userInHashMap.get("name");
        this.image = Integer.parseInt(userInHashMap.get("image").toString());
        this.postList = (ArrayList<String>) userInHashMap.get("postList");
        this.toursCound = Integer.parseInt(userInHashMap.get("toursCound").toString());
        this.company = (String) userInHashMap.get("company");
        this.score = Float.parseFloat(userInHashMap.get("score").toString());
        ArrayList<HashMap<String, Object>> reviews = (ArrayList<HashMap<String, Object>>) userInHashMap.get("reviews");
        if(this.reviews == null)
            this.reviews = new ArrayList<>();

        if(reviews != null)
            for (HashMap<String, Object> review : reviews)
                this.reviews.add(new Review(review));
    }

    public Map<String, Object> addToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("uid", this.uid);
        data.put("name", this.name);
        data.put("postList", this.postList);
        data.put("toursCound", this.toursCound);
        data.put("company", this.company);
        data.put("score", this.score);
        data.put("languages", this.languages);
        data.put("image", this.image);
        data.put("reviews", addReviewsToHashMap());
        data.put("messageToken", this.messageToken);
        return data;
    }

    private ArrayList<Map<String, Object>> addReviewsToHashMap(){
        ArrayList<Map<String, Object>> hashMaps = new ArrayList<>();
        for (int i=0; i < reviews.size(); i++)
            hashMaps.add(reviews.get(i).addToHashMap());

        return  hashMaps;
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

    public ArrayList<String> getPostList() {
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

    public void setPostList(ArrayList<String> postList) {
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

    public void addReview(Review review){
        if(this.reviews == null)
            reviews = new ArrayList<>();
        reviews.add(review);
    }

    public void addPost(Post post){
        if(postList == null)
            postList = new ArrayList<String>();

        post.setUuid(this.uid + this.toursCound + 1);
        postList.add(post.getUuid());
    }

    public void addPost(String postUuid){
        if(postList == null)
            postList = new ArrayList<String>();
        postList.add(postUuid);
    }

    public void setMessageToken(String messageToken) { this.messageToken = messageToken; }
    public String getMessageToken() { return this.messageToken; }
}
