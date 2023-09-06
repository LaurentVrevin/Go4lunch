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
        // Simuler la LiveData que votre userInterface retournerait
        LiveData<User> mockLiveData = Mockito.mock(LiveData.class);
        when(userInterface.getUserLiveData()).thenReturn(mockLiveData);

        // Obtenir la LiveData du ViewModel
        LiveData<User> viewModelLiveData = userViewModel.getUserLiveData();

        // Vérifier que la LiveData n'est pas nulle
        assertNotNull(viewModelLiveData);

        // Vérifier que la LiveData du ViewModel est la même que celle du userInterface
        assertEquals(mockLiveData, viewModelLiveData);
    }

    @Test
    public void testGetUserListLiveData() {
        // Simuler la LiveData que votre userInterface retournerait
        LiveData<List<User>> mockLiveData = Mockito.mock(LiveData.class);
        when(userInterface.getUserListLiveData()).thenReturn(mockLiveData);

        // Obtenir la LiveData du ViewModel
        LiveData<List<User>> viewModelLiveData = userViewModel.getUserListLiveData();

        // Vérifier que la LiveData n'est pas nulle
        assertNotNull(viewModelLiveData);

        // Vérifier que la LiveData du ViewModel est la même que celle du userInterface
        assertEquals(mockLiveData, viewModelLiveData);
    }

    @Test
    public void testGetCurrentUserFromFirestore() {
        String userId = "yourUserId";

        // Appel de la méthode du ViewModel
        userViewModel.getCurrentUserFromFirestore(userId);

        // Vérification que la méthode correspondante du UserInterface a été appelée avec le bon argument
        Mockito.verify(userInterface).getCurrentUserFromFirestore(userId);
    }

    @Test
    public void testCreateUserInFirestore() {
        // Appel de la méthode du ViewModel
        userViewModel.createUserInFirestore();

        // Vérification que la méthode correspondante du UserInterface a été appelée
        Mockito.verify(userInterface).createUserInFirestore();
    }

    @Test
    public void testGetUserId() {
        // Créez une instance simulée de Task<DocumentSnapshot>
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);

        // Configurez le mock pour qu'il renvoie cette instance simulée
        Mockito.when(userInterface.getUserId()).thenReturn(mockTask);

        // Appel de la méthode du ViewModel
        Task<DocumentSnapshot> resultTask = userViewModel.getUserId();

        // Vérification que la méthode correspondante du UserInterface a été appelée
        Mockito.verify(userInterface).getUserId();

        // Vérification que la tâche renvoyée est la même que celle du UserInterface
        Assert.assertEquals(mockTask, resultTask);
    }

    @Test
    public void testUpdateUserSelectedRestaurant() {
        String userId = "yourUserId";
        User user = new User(); // Créez un objet User approprié

        // Appel de la méthode du ViewModel
        userViewModel.updateUserSelectedRestaurant(userId, user);

        // Vérification que la méthode correspondante du UserInterface a été appelée avec les bons arguments
        Mockito.verify(userInterface).updateUserSelectedRestaurant(userId, user);
    }

    @Test
    public void testUpdateUserLikedPlaces() {
        String userId = "yourUserId";
        List<String> likedPlaces = Arrays.asList("Place1", "Place2", "Place3"); // Remplacez par votre liste

        // Appel de la méthode du ViewModel
        userViewModel.updateUserLikedPlaces(userId, likedPlaces);

        // Vérification que la méthode correspondante du UserInterface a été appelée avec les bons arguments
        Mockito.verify(userInterface).updateUserLikedPlace(userId, likedPlaces);
    }

    @Test
    public void testLogOut() {
        // Appel de la méthode du ViewModel
        userViewModel.logOut();

        // Vérification que la méthode correspondante du UserInterface a été appelée
        Mockito.verify(userInterface).logOut();
    }
    @Test
    public void testDeleteAccount() {
        Context context = Mockito.mock(Context.class); // Créez un mock de Context

        // Appel de la méthode du ViewModel
        userViewModel.deleteAccount(context);

        // Vérification que la méthode correspondante du UserInterface a été appelée avec le bon contexte
        Mockito.verify(userInterface).deleteAccount(context);
    }



}
