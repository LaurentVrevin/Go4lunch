package com.example.go4lunch.ui.manager;

import android.content.Context;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import repositories.UserRepository;

public class UserManager {
    private static volatile UserManager instance;
    private UserRepository userRepository;

    public UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }
    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut (Context context){
        return userRepository.signOut(context);
    }

    public Task<Void> deleteUser (Context context){
        return userRepository.deleteUser(context);
    }
    // Ajouter ces trois méthodes pour récupérer les informations de l'utilisateur

    public String getFirstName() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.getDisplayName().split(" ")[0];
        }
        return null;
    }

    public String getLastName() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.getDisplayName().split(" ")[1];
        }
        return null;
    }

    public String getEmail() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

}