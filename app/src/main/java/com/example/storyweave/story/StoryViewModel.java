package com.example.storyweave.story;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.StoryNode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StoryViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<StoryNode>> getStoryNodes(String storyId) {
        MutableLiveData<List<StoryNode>> data = new MutableLiveData<>();

        CollectionReference nodesRef = db.collection("stories")
                .document(storyId)
                .collection("nodes");

        nodesRef.orderBy("createdAt").addSnapshotListener((value, error) -> {
            if (error != null || value == null) {
                data.setValue(null);
                return;
            }

            List<StoryNode> nodes = new ArrayList<>();
            for (var doc : value.getDocuments()) {
                StoryNode node = doc.toObject(StoryNode.class);
                node.setId(doc.getId());
                nodes.add(node);
            }
            data.setValue(nodes);
        });

        return data;
    }
}
