package viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import models.User;
import repositories.UserRepositoryImpl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class UserViewModel extends ViewModel {

    private final UserRepositoryImpl userRepository;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<List<User>> userListLiveData;

    public UserViewModel() {
        userRepository = new UserRepositoryImpl();
        userLiveData = (MutableLiveData<User>) userRepository.getUserLiveData();
        userListLiveData = (MutableLiveData<List<User>>) userRepository.getUserListLiveData();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public void getCurrentUserFromFirestore(String userId) {
        userRepository.getCurrentUserFromFirestore(userId);
    }

    public void getUserListFromFirestore() {
        userRepository.getUserListFromFirestore();
    }

    public void createUserInFirestore() {
        userRepository.createUserInFirestore();
    }

    public Task<DocumentSnapshot> getUserId() {
        return userRepository.getUserId();
    }

    public void logOut() {
        userRepository.logOut();
    }

    public void deleteAccount(Context context) {
        userRepository.deleteAccount(context);
    }

    public void updateUserInFirestore(String userId, User user) {
        userRepository.updateUserInFirestore(userId, user);
    }
}

