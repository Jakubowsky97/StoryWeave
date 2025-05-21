package com.example.storyweave.ui.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CreateViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}