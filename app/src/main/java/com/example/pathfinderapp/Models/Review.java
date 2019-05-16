package com.example.pathfinderapp.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Review {

    private String message;
    private User author;
    private Date createdAt;

    public Review(String review, User author, Date createdAt) {
        this.message = review;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("author", author);
        data.put("createdAt", createdAt);
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAutor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
