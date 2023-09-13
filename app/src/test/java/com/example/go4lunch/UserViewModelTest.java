package com.example.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserInterface;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserInterface userInterface;

    private UserViewModel userViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userViewModel = new UserViewModel(userInterface);
    }

    @Test
    public void testGetUserLiveData() {
        // Simulate the LiveData that userInterface would return
        LiveData<User> mockLiveData = Mockito.mock(LiveData.class);
        when(userInterface.getUserLiveData()).thenReturn(mockLiveData);

        // Get the ViewModel's LiveData
        LiveData<User> viewModelLiveData = userViewModel.getUserLiveData();

        // Verify that the LiveData is not null
        assertNotNull(viewModelLiveData);

        // Verify that the ViewModel's LiveData is the same as userInterface's
        assertEquals(mockLiveData, viewModelLiveData);
    }

    @Test
    public void testGetUserListLiveData() {
        // Simulate the LiveData that userInterface would return
        LiveData<List<User>> mockLiveData = Mockito.mock(LiveData.class);
        when(userInterface.getUserListLiveData()).thenReturn(mockLiveData);

        // Get the ViewModel's LiveData
        LiveData<List<User>> viewModelLiveData = userViewModel.getUserListLiveData();

        // Verify that the LiveData is not null
        assertNotNull(viewModelLiveData);

        // Verify that the ViewModel's LiveData is the same as userInterface's
        assertEquals(mockLiveData, viewModelLiveData);
    }

    @Test
    public void testGetCurrentUserFromFirestore() {
        String userId = "yourUserId";

        // Call the ViewModel's method
        userViewModel.getCurrentUserFromFirestore(userId);

        // Verify that the corresponding method in UserInterface was called with the correct argument
        Mockito.verify(userInterface).getCurrentUserFromFirestore(userId);
    }

    @Test
    public void testCreateUserInFirestore() {
        // Call the ViewModel's method
        userViewModel.createUserInFirestore();

        // Verify that the corresponding method in UserInterface was called
        Mockito.verify(userInterface).createUserInFirestore();
    }

    @Test
    public void testGetUserId() {
        // Create a simulated instance of Task<DocumentSnapshot>
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);

        // Configure the mock to return this simulated instance
        Mockito.when(userInterface.getUserId()).thenReturn(mockTask);

        // Call the ViewModel's method
        Task<DocumentSnapshot> resultTask = userViewModel.getUserId();

        // Verify that the corresponding method in UserInterface was called
        Mockito.verify(userInterface).getUserId();

        // Verify that the returned task is the same as the one from UserInterface
        Assert.assertEquals(mockTask, resultTask);
    }

    @Test
    public void testUpdateUserSelectedRestaurant() {
        String userId = "123456789";
        User user = new User(); // Create an appropriate User object

        // Call the ViewModel's method
        userViewModel.updateUserSelectedRestaurant(userId, user);

        // Verify that the corresponding method in UserInterface was called with the correct arguments
        Mockito.verify(userInterface).updateUserSelectedRestaurant(userId, user);
    }

    @Test
    public void testUpdateUserLikedPlaces() {
        String userId = "123456789";
        List<String> likedPlaces = Arrays.asList("Place1", "Place2", "Place3"); // Replace with your list

        // Call the ViewModel's method
        userViewModel.updateUserLikedPlaces(userId, likedPlaces);

        // Verify that the corresponding method in UserInterface was called with the correct arguments
        Mockito.verify(userInterface).updateUserLikedPlace(userId, likedPlaces);
    }

    @Test
    public void testLogOut() {
        // Call the ViewModel's method
        userViewModel.logOut();

        // Verify that the corresponding method in UserInterface was called
        Mockito.verify(userInterface).logOut();
    }

    @Test
    public void testDeleteAccount() {
        Context context = Mockito.mock(Context.class); // Create a mock Context

        // Call the ViewModel's method
        userViewModel.deleteAccount(context);

        // Verify that the corresponding method in UserInterface was called with the correct context
        Mockito.verify(userInterface).deleteAccount(context);
    }
}
