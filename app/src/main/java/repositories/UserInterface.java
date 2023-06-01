package repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface UserInterface {
    void instanceFirestore();

    FirebaseUser getCurrentUserFromFirebase();

    CollectionReference getUserCollection();

    LiveData<User> getUserLiveData();

    void getCurrentUserFromFirestore(String userId);

    LiveData<List<User>> getUserListLiveData();
    void setUserList(List<User> userList);

    void getUserListFromFirestore();

    void createUserInFirestore();

    Task<DocumentSnapshot> getUserId();

    void logOut();

    void deleteAccount(Context context);

    void updateUserInFirestore(String userId, User user);
}
