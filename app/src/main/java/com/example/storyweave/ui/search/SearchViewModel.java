package com.example.storyweave.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.Story;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<Story> foundStory = new MutableLiveData<>();
    public LiveData<Story> foundStoryLive = foundStory;

    public LiveData<Story> getFoundStory() {
        return foundStoryLive;
    }

    public void searchByCode(String inviteCode) {
        FirebaseFirestore.getInstance()
                .collection("stories")
                .whereEqualTo("inviteCode", inviteCode)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        Story story = doc.toObject(Story.class);
                        if (story != null) {
                            story.setId(doc.getId());
                            foundStory.setValue(story);
                        } else {
                            foundStory.setValue(null);
                        }
                    } else {
                        foundStory.setValue(null);
                    }
                })
                .addOnFailureListener(e -> foundStory.setValue(null));
    }
}
