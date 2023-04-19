package com.example.go4lunch.ui.fragments.listview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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