package com.example.storyweave.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.Story;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Story>> storyList = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<List<Story>> getStoryList() {
        return storyList;
    }

    public void fetchStories() {
        db.collection("stories")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    List<Story> stories = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Story story = doc.toObject(Story.class);
                        if (story != null) {
                            story.setId(doc.getId());
                            stories.add(story);
                        }
                    }
                    storyList.setValue(stories);
                });
    }

}
