package com.example.storyweave.game;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class GameValues {
    private String language;
    private String code;
    private String creatorUserId;

    public GameValues(String language, String code) {
        this.language = language;
        this.code = code;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.creatorUserId = user.getUid();
        }
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
