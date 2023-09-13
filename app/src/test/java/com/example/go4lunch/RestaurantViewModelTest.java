package com.example.go4lunch;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.repositories.RestaurantInterface;
import com.example.go4lunch.viewmodels.RestaurantViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class RestaurantViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantInterface restaurantInterface;

    private RestaurantViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new RestaurantViewModel(restaurantInterface);
    }

    @Test
    public void testGetRestaurants() {
        int radius = 200;
        Location mockLocation = Mockito.mock(Location.class);

        // Create a simulated list of restaurants
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());

        // Simulate the behavior of restaurantInterface
        MutableLiveData<List<Restaurant>> fakeLiveData = new MutableLiveData<>();
        fakeLiveData.setValue(restaurantList);
        when(restaurantInterface.getRestaurants(mockLocation, radius)).thenReturn(fakeLiveData);

        // Call the ViewModel method
        viewModel.getRestaurants(mockLocation);

        // Observe to monitor changes in LiveData
        LiveData<List<Restaurant>> observedLiveData = viewModel.getListRestaurantLiveData();

        assertNotNull(observedLiveData.getValue());
        assertEquals(2, observedLiveData.getValue().size());
    }

    @Test
    public void testGetRestaurantById() {
        // Create a simulated restaurant with a placeId
        String placeId = "123456";
        Restaurant restaurant = new Restaurant();
        restaurant.setPlaceId(placeId);

        // Simulate the behavior of restaurantInterface to return the restaurant by placeId
        MutableLiveData<Restaurant> fakeLiveData = new MutableLiveData<>();
        fakeLiveData.setValue(restaurant);
        when(restaurantInterface.getRestaurantById(placeId)).thenReturn(fakeLiveData);

        // Call the ViewModel method to get the restaurant by placeId
        viewModel.getRestaurantById(placeId);

        // Observe to monitor changes in LiveData
        LiveData<Restaurant> observedLiveData = viewModel.getSelectedRestaurantLiveData();

        // Assert that the initial value is not null
        assertNotNull(observedLiveData.getValue());

        // Assert that the initial value matches the simulated restaurant
        assertEquals(restaurant, observedLiveData.getValue());
    }
}
