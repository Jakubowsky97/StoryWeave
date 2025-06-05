package com.example.storyweave.model;

public class StoryNode {
    private String id;
    private String parentId;
    private String storyId;
    private String content;
    private String authorId;
    private long timestamp;
    private int votes;

    public StoryNode() {}

    public StoryNode(String id, String parentId, String storyId, String content, String authorId, long timestamp, int votes) {
        this.id = id;
        this.parentId = parentId;
        this.storyId = storyId;
        this.content = content;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.votes = votes;
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getStoryId() { return storyId; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public long getTimestamp() { return timestamp; }
    public int getVotes() { return votes; }

    public void setId(String id) { this.id = id; }
    public void setVotes(int votes) { this.votes = votes; }
}
