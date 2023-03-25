package com.example.go4lunch.ui.listview;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ListViewViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ListViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is your ListView fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}