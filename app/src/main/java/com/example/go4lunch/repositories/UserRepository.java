package com.example.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.MyApplication;
import com.example.go4lunch.models.Restaurant;
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
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository implements UserInterface {
    private static final String TAG = "UserRepository";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CollectionReference userCollection;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<List<User>> userListLiveData;
    private List<Restaurant>restaurantList;

    @Inject
    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollection = firebaseFirestore.collection("users");
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();
    }
      @Override
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    @Override
    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
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

                    User newUser = new User(userId, displayName, email, photoUrl, new ArrayList<>(), null);

                    // Utiliser la méthode "set" avec l'option "merge" pour mettre à jour uniquement les champs spécifiques
                    userDocument.set(newUser, SetOptions.merge())
                            .addOnCompleteListener(createUserTask -> {
                                if (createUserTask.isSuccessful()) {
                                    Log.d(TAG, "User created in Firestore: " + userId);
                                } else {
                                    Log.e(TAG, "Failed to create user in Firestore: " + userId, createUserTask.getException());
                                }
                            });
                }
            });
        }
    }

    @Override
    public void getCurrentUserFromFirestore(String userId) {
        Log.d("TESTWORKMATEUPDATE", "currentuser");
        userCollection.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    user.setUserId(userId);
                    userLiveData.postValue(user);
                }
            }
        });
    }

    @Override
    public void getWorkmatesListFromFirestore(boolean forceUpdate) {

        if (userListLiveData.getValue() == null || userListLiveData.getValue().isEmpty() || forceUpdate)  {
            Log.d("TESTWORKMATEUPDATE", "coucou");
            userCollection.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<User> userList = new ArrayList<>();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);

                        /*if (currentUser != null && user.getUserId().equals(currentUser.getUid())) {
                            continue;
                        }*/
                        String selectedRestaurantId = document.getString("selectedRestaurantId");
                        user.setSelectedRestaurantId(selectedRestaurantId);
                        userList.add(user);
                    }

                    userListLiveData.postValue(userList);
                    Log.d(TAG, "Retrieved user list from Firestore. User count: " + userList.size());
                    // Log des lieux appréciés de tous les utilisateurs
                    for (User user : userList) {
                        Log.d(TAG, user.getName() + user.getLikedPlaces());
                    }
                    /*Log.d(TAG, "Retrieved user list from Firestore. User count: " +
                            userList.get(1).getUserId() + " " + userList.get(1).getName() + " " + userList.get(1).getSelectedRestaurantId());*/
                } else {
                    Log.e(TAG, "Failed to retrieve user list from Firestore", task.getException());
                }
            });
        }

    }
    @Override
    public void getAllUsersFromFirestore() {
        userCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> userList = new ArrayList<>();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    if (currentUser != null && user.getUserId().equals(currentUser.getUid())) {
                        continue;
                    }
                    userList.add(user);
                }

                updateRestaurantLikes(userList); // Appel de la méthode pour mettre à jour les likes

            } else {
                Log.e(TAG, "Failed to retrieve user list from Firestore", task.getException());
            }
        });
    }

    private void updateRestaurantLikes(List<User> userList) {
        List<Restaurant> restaurantList = new ArrayList<>(); // La liste de restaurants

        // Remplissez votre liste de restaurants ici (peut-être à partir de Firestore)

        for (User user : userList) {
            List<String> likedPlaces = user.getLikedPlaces();
            for (String likedPlace : likedPlaces) {
                for (Restaurant restaurant : restaurantList) {
                    if (restaurant.getPlaceId().equals(likedPlace)) {
                        restaurant.incrementlikesCount();


                        break;
                    }
                    Log.d(TAG, "le restaurant a :" + String.valueOf(restaurant.getLikesCount()));
                }
            }
        }

        //userListLiveData.postValue(restaurantList); // Mettez à jour la liste de LiveData
    }


    @Override
    public void updateUserInFirestore(String userId, User user) {
        if (userId != null && user != null) {
            userCollection.document(userId).set(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userLiveData.postValue(user);
                } else {
                    Log.e(TAG, "Failed to update user in Firestore: " + userId, task.getException());
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
                            Log.e(TAG, "Failed to update user selected restaurant in Firestore: " + userId, task.getException());
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
                            Log.d(TAG, "User liked places updated in Firestore: " + userId);
                        } else {
                            Log.e(TAG, "Failed to update user liked places in Firestore: " + userId, task.getException());
                        }
                    });
        }
    }

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
        if (firebaseUser != null) {
            AuthUI.getInstance().delete(context);
            userCollection.document(firebaseUser.getUid()).delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted from Firestore: " + firebaseUser.getUid());
                        } else {
                            Log.e(TAG, "Failed to delete user account from Firestore: " + firebaseUser.getUid(), task.getException());
                        }
                    });

            firebaseAuth.signOut();
        }
    }
    @Override
    public void setUserList(List<User> userList) {
        userListLiveData.postValue(userList);
    }



}
