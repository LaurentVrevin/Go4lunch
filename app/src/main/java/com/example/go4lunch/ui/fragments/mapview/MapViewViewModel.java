package com.example.go4lunch.ui.fragments.mapview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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