package com.example.storyweave.lobby;

public class LobbyInformation {
    private String storyId;
    private String title;
    private boolean isPrivate;
    private String inviteCode;
    private String creatorId;

    public LobbyInformation() {
        // Required for Firestore
    }

    public LobbyInformation(String storyId, String title, boolean isPrivate, String inviteCode, String creatorId) {
        this.storyId = storyId;
        this.title = title;
        this.isPrivate = isPrivate;
        this.inviteCode = inviteCode;
        this.creatorId = creatorId;
    }

    public String getStoryId() { return storyId; }
    public String getTitle() { return title; }
    public boolean isPrivate() { return isPrivate; }
    public String getInviteCode() { return inviteCode; }
    public String getCreatorId() { return creatorId; }
}
