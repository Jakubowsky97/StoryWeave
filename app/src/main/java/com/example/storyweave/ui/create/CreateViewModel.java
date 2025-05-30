package com.example.storyweave.ui.create;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyweave.model.Story;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class CreateViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<String> generatedInviteCode = new MutableLiveData<>();

    public LiveData<Boolean> getCreateSuccess() {
        return createSuccess;
    }

    public LiveData<String> getGeneratedInviteCode() {
        return generatedInviteCode;
    }

    public void createStory(String title, String description) {
        String inviteCode = generateInviteCode();
        generatedInviteCode.setValue(inviteCode);

        Story story = new Story(title, description, System.currentTimeMillis(), inviteCode);

        db.collection("stories")
                .add(story)
                .addOnSuccessListener(documentReference -> createSuccess.setValue(true))
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
}
