package com.example.go4lunch.repositories;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.User;
import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class UserRepository implements UserInterface {

    private FirebaseFirestore firebaseFirestoreDB;
    private final FirebaseAuth firebaseAuth;
    private final CollectionReference userCollection;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<List<User>> userListLiveData;
    private User user;


    @Inject
    public UserRepository() {
        instanceFirestore();
        firebaseAuth = FirebaseAuth.getInstance();
        userCollection = firebaseFirestoreDB.collection("users");
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();
    }

    @Override
    public void instanceFirestore() {
        firebaseFirestoreDB = FirebaseFirestore.getInstance();
    }

    @Override
    public void createUserInFirestore() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DocumentReference userDocument = userCollection.document(userId);

            userDocument.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    // L'utilisateur existe déjà dans Firestore, ne rien faire
                } else {
                    // L'utilisateur n'existe pas encore dans Firestore, créer un nouveau document
                    String displayName = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    String photoUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;

                    List<String> likedPlaces = new ArrayList<>();

                    User newUser = new User(userId, displayName, email, photoUrl, likedPlaces, null);

                    // Utiliser la méthode "set" avec l'option "merge" pour mettre à jour uniquement les champs spécifiques
                    userDocument.set(newUser, SetOptions.merge());
                    Log.d("DATA", "Les données sont : " + userId + " " + displayName + " " + email);
                }
            });
        }
    }


    @Override
    public FirebaseUser getCurrentUserFromFirebase() {
        return firebaseAuth.getCurrentUser(); // Récupération de l'utilisateur actuel Firebase
    }

    @Override
    public void getCurrentUserFromFirestore(String userId) {
        // Récupération des données utilisateur depuis Firestore pour un utilisateur spécifique
        userCollection.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Si la récupération des données est réussie et que les données ne sont pas nulles, on créé un objet User avec les données récupérées et on le poste dans le LiveData userLiveData
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    user.setUserId(userId); // Définition de l'ID de l'utilisateur de l'objet User via firestore
                    userLiveData.postValue(user);
                }
            }
        });
    }


    @Override
    public LiveData<User> getUserLiveData() {
        return userLiveData; // Renvoi du LiveData pour un utilisateur
    }

    @Override
    public CollectionReference getUserCollection() {
        return userCollection; // Renvoi de la collection "users" dans Firestore
    }

    @Override
    public void setUserList(List<User> userList) {
        userListLiveData.postValue(userList);
    }


    @Override
    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData; // Renvoi du LiveData pour une liste d'utilisateurs
    }

    @Override
    public void getUserListFromFirestore() {
        //instanceFirestore();
        // Récupération de la liste des utilisateurs depuis Firestore
        Log.d("UserRepositoryImpl", "getUserListFromFirestore() called");
        userCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    // Si l'utilisateur actuellement traité est l'utilisateur connecté,
                    // on ignore cet utilisateur et on passe à l'itération suivante de la boucle
                    if (user.getUserId().equals(getCurrentUserFromFirebase().getUid())) {
                        continue;
                    }
                    userList.add(user);
                }
                userListLiveData.postValue(userList);
                Log.d("UserRepositoryImpl", "getUserListFromFirestore() success - userList size: " + userList.size());
            } else {
                Log.e("UserRepositoryImpl", "getUserListFromFirestore() error: " + task.getException());
            }
        });
    }


    //Task<DocumentSnapshot> représente la requête de récupération de l'utilisateur dans Firestore
    @Override
    public Task<DocumentSnapshot> getUserId() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return userCollection.document(firebaseUser.getUid()).get();
        } else {
            return null;
        }
    }


    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }

    @Override
    public void deleteAccount(Context context) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthUI.getInstance().delete(context);
        userCollection.document(firebaseUser.getUid()).delete();
        //Je m'assure que celui-ci soit bien déconnecté :
        firebaseAuth.signOut();
    }


    @Override
    public void updateUserInFirestore(String userId, User user) {
        if (userId != null && user != null) {
            userCollection.document(userId).set(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userLiveData.postValue(user);
                } else {
                    // Erreur lors de la mise à jour des données utilisateur dans Firestore
                }
            });
        }
    }

    @Override
    public void updateUserSelectedRestaurant(String userId, User user) {
        if (userId != null && user != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("selectedRestaurantId", user.getSelectedRestaurantId());

            userCollection.document(userId).update(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(user);
                        } else {
                            // Erreur lors de la mise à jour des données utilisateur dans Firestore
                        }
                    });
        }
    }
    @Override
    public void updateUserLikedPlace(String userId, List<String> likedPlaces) {
        if (userId != null) {
            DocumentReference userDocument = userCollection.document(userId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("likedPlaces", likedPlaces);

            userDocument.update(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Mise à jour réussie
                        } else {
                            // Erreur lors de la mise à jour
                        }
                    });
        }
    }
}

