package com.example.storyweave.game;

import static com.example.storyweave.game.StoryNode.createStoryNode;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Story {
    public String id; // Firestore document ID
    public String title;
    public String description;
    public boolean isPublic;

    public Story() {}

    public Story(String title, String description, boolean isPublic) {
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
    }

    public void createStory(String title, String description, boolean isPublic) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> storyData = new HashMap<>();
        storyData.put("title", title);
        storyData.put("description", description);
        storyData.put("isPublic", isPublic);

        db.collection("stories")
                .add(storyData)
                .addOnSuccessListener(documentReference -> {
                    String storyId = documentReference.getId();
                    // można od razu utworzyć pierwszy fragment
                    createStoryNode(storyId, "Początkowe zdanie", null);
                });
    }
}