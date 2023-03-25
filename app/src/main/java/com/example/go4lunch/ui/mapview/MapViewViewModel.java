package com.example.go4lunch.ui.mapview;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MapViewViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MapViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is your Mapview fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}