package com.example.go4lunch.viewmodels;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserInterface;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {

    private final UserInterface userInterface;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    @Inject
    public UserViewModel(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public LiveData<User> getUserLiveData() {
        return userInterface.getUserLiveData();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userInterface.getUserListLiveData();
    }

    public void getCurrentUserFromFirestore(String userId) {
        userInterface.getCurrentUserFromFirestore(userId);
    }



    public void getUserListFromFirestore() {
        userInterface.getUserListFromFirestore();
    }

    public void createUserInFirestore() {
        userInterface.createUserInFirestore();
    }

    public Task<DocumentSnapshot> getUserId() {
        return userInterface.getUserId();
    }

    public void logOut() {
        userInterface.logOut();
    }

    public void deleteAccount(Context context) {
        userInterface.deleteAccount(context);
    }

    public void updateUserInFirestore(String userId, User user) {
        userInterface.updateUserInFirestore(userId, user);
    }
    public void updateUserSelectedRestaurant(String userId, User user) {
        userInterface.updateUserSelectedRestaurant(userId, user);
    }

}

