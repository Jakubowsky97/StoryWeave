package com.example.storyweave.game;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.Map;

public class GameInformation {
    private FirebaseUser user;
    private String message;
    private boolean isReady;

    public GameInformation(String message) {
        this.message = message;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
