package com.example.storyweave.game;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Vote {
    public String id; // Firestore document ID
    public String nodeId;
    public String userId;
    public boolean isUpvote;

    public Vote() {}
    public Vote(String nodeId, String userId, boolean isUpvote) {
        this.nodeId = nodeId;
        this.userId = userId;
        this.isUpvote = isUpvote;
    }
    public void voteOnNode(String nodeId, boolean isUpvote) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> voteData = new HashMap<>();
        voteData.put("nodeId", nodeId);
        voteData.put("userId", user.getUid());
        voteData.put("isUpvote", isUpvote);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // dodanie głosu
        db.collection("votes")
                .add(voteData)
                .addOnSuccessListener(documentReference -> {
                    // zmienia sumę głosów w StoryNode
                    updateVotesCount(nodeId);
                });
    }

    public void updateVotesCount(String nodeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("votes")
                .whereEqualTo("nodeId", nodeId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    long count = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Boolean isUp = doc.getBoolean("isUpvote");
                        if (isUp != null && isUp) count++;
                        else count--;
                    }

                    // Aktualizacja votesCount w node
                    db.collection("storyNodes").document(nodeId)
                            .update("votesCount", count);
                });
    }
}