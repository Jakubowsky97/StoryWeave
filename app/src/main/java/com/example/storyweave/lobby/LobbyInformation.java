package com.example.storyweave.lobby;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LobbyInformation {
    private String language;
    private String lobbycode;
    private String creatorUserId;

    public LobbyInformation(String language, String code) {
        this.language = language;
        this.lobbycode = code;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.creatorUserId = user.getUid();
        }
    }

    public String getLanguage() {
        return language;
    }

    public String getLobbycode() {
        return lobbycode;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
