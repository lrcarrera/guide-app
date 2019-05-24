package com.example.pathfinderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";
    private static final String PACKAGE_NAME = "com.example.pathfinderapp";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Handler mainHandler = new Handler(getMainLooper());
        final String message = remoteMessage.getNotification().getBody();

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences prefs = Objects.requireNonNull(getApplicationContext()).getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        prefs.edit().putString(getResources().getString(R.string.message_token), token).apply();
        prefs.edit().putBoolean(getResources().getString(R.string.send_token), true).apply();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userUid = auth.getCurrentUser().getUid();
        User user = DefValues.getUserInContext();
        if (token != null && user != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(userUid).push().setValue(token);
            user.setMessageToken(token);
            DefValues.getUserInContextDocument().update("user", user.addToHashMap());
        }
    }
}
