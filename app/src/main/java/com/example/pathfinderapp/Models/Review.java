package com.example.pathfinderapp.Models;

import java.util.Date;

public class Review {

    private String message;
    private User author;
    private Date createdAt;

    public Review(String review, User author, Date createdAt) {
        this.message = review;
        this.author = author;
        this.createdAt = createdAt;
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
