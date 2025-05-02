package com.example.storyweave.ui.profile;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.sql.Date;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private Profile proftest = new Profile("John","Doe",
            Date.valueOf("14.05.2004"),"abcd@gmail.com");
    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}