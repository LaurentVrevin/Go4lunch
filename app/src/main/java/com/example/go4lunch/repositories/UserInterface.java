package com.example.go4lunch.repositories;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface UserInterface {

    LiveData<User> getUserLiveData();
    LiveData<List<User>> getUserListLiveData();
    void createUserInFirestore();

    void getCurrentUserFromFirestore(String userId);


    void setUserList(List<User> userList);

    void getWorkmatesListFromFirestore(boolean forceUpdate);
    void updateUserInFirestore(String userId, User user);
    void updateUserSelectedRestaurant(String userId, User user);
    void updateUserLikedPlace(String userId, List<String> likedPlaces);

    Task<DocumentSnapshot> getUserId();

    void logOut();

    void deleteAccount(Context context);






}
