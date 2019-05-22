package com.example.pathfinderapp.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        //this.message = (String) review.get("message");
    }

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("author", author);
        data.put("createdAt", createdAt);
        return data;
    }

    public void getAuthorInfoFromDatabase(){
        final FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("user.uid", this.author )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = new User(document);
                                //DefValues.setDocumentReference(document.getReference());
                                //DefValues.setUserInContext(document);
                            }
                            setAuthorInfo(user);

                            /*fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
                            fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
                            fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
                            fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();*/

                        } else {
                            Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                        }
                    }
                });
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
