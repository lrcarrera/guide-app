package com.example.pathfinderapp.Models;

import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Review {

    private String message;
    private String author;
    private User authorInfo;
    private Date createdAt;

    public Review(String review, String author, Date createdAt) {
        this.message = review;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Review(HashMap<String, Object> review){
        this.message =  (String) review.get("message");
        this.author = (String) review.get("author");
        Timestamp timestamp = (Timestamp) review.get("createdAt");
        this.createdAt = timestamp.toDate();
    }

    Map<String, Object> addToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("author", author);
        data.put("createdAt", createdAt);
        return data;
    }

    public void setAuthorInfo(User user){
        this.authorInfo = user;
    }

    public User getAuthorInfo(){
        return this.authorInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAutor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
