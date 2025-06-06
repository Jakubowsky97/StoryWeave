package com.example.storyweave.story;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.StoryNode;
import com.example.storyweave.model.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final MutableLiveData<Boolean> addNodeSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<StoryNode>> getStoryNodes(String storyId) {
        MutableLiveData<List<StoryNode>> data = new MutableLiveData<>();

        CollectionReference nodesRef = db.collection("stories")
                .document(storyId)
                .collection("nodes");

        nodesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        data.setValue(null);
                        return;
                    }

                    List<StoryNode> nodes = new ArrayList<>();
                    for (var doc : value.getDocuments()) {
                        StoryNode node = doc.toObject(StoryNode.class);
                        if (node != null) {
                            node.setId(doc.getId());
                            nodes.add(node);
                        }
                    }
                    data.setValue(nodes);
                });

        return data;
    }

    public void addStoryNode(String storyId, String parentId, String content) {
        if (mAuth.getCurrentUser() == null) {
            error.setValue("User not authenticated");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        StoryNode newNode = new StoryNode(null, parentId, storyId, content, userId, timestamp, 0);

        db.collection("stories")
                .document(storyId)
                .collection("nodes")
                .add(newNode)
                .addOnSuccessListener(documentReference -> {
                    addNodeSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    error.setValue("Failed to add story node: " + e.getMessage());
                    addNodeSuccess.setValue(false);
                });
    }

    public void voteForNode(String storyId, String nodeId) {
        if (mAuth.getCurrentUser() == null) {
            error.setValue("User not authenticated");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // Sprawdź, czy użytkownik już głosował na ten węzeł
        db.collection("votes")
                .whereEqualTo("userId", userId)
                .whereEqualTo("nodeId", nodeId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        error.setValue("Already voted for this node");
                        return;
                    }

                    // Dodaj głos
                    Vote vote = new Vote(userId, nodeId, storyId, System.currentTimeMillis());
                    db.collection("votes")
                            .add(vote)
                            .addOnSuccessListener(voteRef -> {
                                // Zwiększ liczbę głosów w węźle
                                DocumentReference nodeRef = db.collection("stories")
                                        .document(storyId)
                                        .collection("nodes")
                                        .document(nodeId);

                                db.runTransaction(transaction -> {
                                    StoryNode node = transaction.get(nodeRef).toObject(StoryNode.class);
                                    if (node != null) {
                                        node.setVotes(node.getVotes() + 1);
                                        transaction.set(nodeRef, node);
                                    }
                                    return null;
                                });
                            })
                            .addOnFailureListener(e -> error.setValue("Failed to vote: " + e.getMessage()));
                })
                .addOnFailureListener(e -> error.setValue("Error checking vote: " + e.getMessage()));
    }

    public LiveData<List<StoryNode>> getNodesByParent(String storyId, String parentId) {
        MutableLiveData<List<StoryNode>> data = new MutableLiveData<>();

        Query query = db.collection("stories")
                .document(storyId)
                .collection("nodes");

        if (parentId != null) {
            query = query.whereEqualTo("parentId", parentId);
        } else {
            query = query.whereEqualTo("parentId", null);
        }

        query.orderBy("votes", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        data.setValue(null);
                        return;
                    }

                    List<StoryNode> nodes = new ArrayList<>();
                    for (var doc : value.getDocuments()) {
                        StoryNode node = doc.toObject(StoryNode.class);
                        if (node != null) {
                            node.setId(doc.getId());
                            nodes.add(node);
                        }
                    }
                    data.setValue(nodes);
                });

        return data;
    }

    public LiveData<Boolean> getAddNodeSuccess() { return addNodeSuccess; }
    public LiveData<String> getError() { return error; }
}