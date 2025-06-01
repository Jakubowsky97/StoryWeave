package com.example.storyweave.game;

import androidx.recyclerview.widget.SortedList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryNode {
    public String id; // Firestore document ID
    public String storyId;
    public String content;
    public String parentId; // null jeśli pierwszy
    public String creatorId;
    public int votesCount;

    public StoryNode() {
    }

    public StoryNode(String storyId, String content, String parentId, String creatorId, int votesCount) {
        this.storyId = storyId;
        this.content = content;
        this.parentId = parentId;
        this.creatorId = creatorId;
        this.votesCount = votesCount;
    }

    public static void createStoryNode(String storyId, String content, String parentId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> nodeData = new HashMap<>();
        nodeData.put("storyId", storyId);
        nodeData.put("content", content);
        nodeData.put("parentId", parentId);
        nodeData.put("creatorId", user.getUid());
        nodeData.put("votesCount", 0L);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("storyNodes")
                .add(nodeData)
                .addOnSuccessListener(documentReference -> {
                    String nodeId = documentReference.getId();
                    // ew. odśwież widok
                });
    }

    public void loadStoryNodes(String storyId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("storyNodes")
                .whereEqualTo("storyId", storyId)
                .get()
                .addOnSuccessListener(query -> {
                    List<StoryNode> nodes = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        StoryNode node = doc.toObject(StoryNode.class);
                        assert node != null;
                        node.id = doc.getId();
                        nodes.add(node);
                    }
                });
    }
}