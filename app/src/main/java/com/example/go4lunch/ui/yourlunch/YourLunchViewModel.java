package com.example.go4lunch.ui.yourlunch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class YourLunchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public YourLunchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is your lunch fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}