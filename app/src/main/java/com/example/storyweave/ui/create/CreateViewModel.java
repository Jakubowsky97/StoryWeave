package com.example.storyweave.ui.create;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class CreateViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private final MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<String> generatedInviteCode = new MutableLiveData<>();
    private final MutableLiveData<String> createdStoryId = new MutableLiveData<>();

    public LiveData<Boolean> getCreateSuccess() {
        return createSuccess;
    }

    public LiveData<String> getGeneratedInviteCode() {
        return generatedInviteCode;
    }

    public LiveData<String> getCreatedStoryId() {
        return createdStoryId;
    }

    public void createStory(String title, String description) {
        String inviteCode = generateInviteCode();
        generatedInviteCode.setValue(inviteCode);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        Story story = new Story(title, mAuth.getUid(), description, System.currentTimeMillis(), inviteCode);

        db.collection("stories")
                .add(story)
                .addOnSuccessListener(documentReference -> {
                    createSuccess.setValue(true);
                    createdStoryId.setValue(documentReference.getId());
                })
                .addOnFailureListener(e -> Log.e("CreateViewModel", "Failed to create story", e));
    }

    private String generateInviteCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    public void goToLobby() {

    }
}
