package com.example.storyweave.model;

public class Story {
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private long createdAt;
    private String inviteCode;

    public Story() {
        // Required for Firebase
    }

    public Story(String title, String ownerId, String description, long createdAt, String inviteCode) {
        this.title = title;
        this.ownerId = ownerId;
        this.description = description;
        this.createdAt = createdAt;
        this.inviteCode = inviteCode;
    }

    // Getters & setters (Firebase uses them for serialization)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerId() {
        return ownerId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getCreatedAt() { return createdAt; }
    public String getInviteCode() { return inviteCode; }
}
