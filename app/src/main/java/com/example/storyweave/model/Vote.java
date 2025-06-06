package com.example.storyweave.model;

public class Vote {
    private String id;
    private String userId;
    private String nodeId;
    private String storyId;
    private long timestamp;

    public Vote() {
        // Required for Firebase
    }

    public Vote(String userId, String nodeId, String storyId, long timestamp) {
        this.userId = userId;
        this.nodeId = nodeId;
        this.storyId = storyId;
        this.timestamp = timestamp;
    }

    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }

    public String getStoryId() { return storyId; }
    public void setStoryId(String storyId) { this.storyId = storyId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}