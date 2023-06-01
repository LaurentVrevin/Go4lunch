package repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

import models.User;

public class UserRepositoryImpl implements UserInterface {

    private FirebaseFirestore firebaseFirestoreDB;
    private final FirebaseAuth firebaseAuth;
    private final CollectionReference userCollection;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<List<User>> userListLiveData;

    public UserRepositoryImpl() {
        instanceFirestore();
        firebaseAuth = FirebaseAuth.getInstance();
        userCollection = firebaseFirestoreDB.collection("users");
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();
    }

    @Override
    public void instanceFirestore() {
        //FirebaseFirestore.setLoggingEnabled(true); // Activation des logs Firestore
        firebaseFirestoreDB = FirebaseFirestore.getInstance();
    }
    @Override
    public void setUserList(List<User> userList) {
        userListLiveData.postValue(userList);
    }

    @Override
    public FirebaseUser getCurrentUserFromFirebase() {
        return firebaseAuth.getCurrentUser(); // Récupération de l'utilisateur actuel Firebase
    }

    @Override
    public CollectionReference getUserCollection() {
        return userCollection; // Renvoi de la collection "users" dans Firestore
    }

    @Override
    public LiveData<User> getUserLiveData() {
        return userLiveData; // Renvoi du LiveData pour un utilisateur
    }

    @Override
    public void getCurrentUserFromFirestore(String userId) {
        // Récupération des données utilisateur depuis Firestore pour un utilisateur spécifique
        userCollection.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Si la récupération des données est réussie et que les données ne sont pas nulles, on créé un objet User avec les données récupérées et on le poste dans le LiveData userLiveData
                User user = task.getResult().toObject(User.class);
                userLiveData.postValue(user);
            }
        });
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
                            if (user.getUserId().equals(getCurrentUserFromFirebase().getUid())){
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

    @Override
    public void createUserInFirestore() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Récupération du nom de famille et de l'e-mail depuis le profil de l'utilisateur Firebase
            String surname = firebaseUser.getDisplayName().split(" ")[1];
            String email = firebaseUser.getEmail();
            // Récupération de l'URL de la photo de profil
            String photoUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;
            // Création d'un objet User avec les informations de l'utilisateur actuellement connecté
            User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), photoUrl, null, null);
            userCollection.document(user.getUserId()).set(user);
        }
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
}

