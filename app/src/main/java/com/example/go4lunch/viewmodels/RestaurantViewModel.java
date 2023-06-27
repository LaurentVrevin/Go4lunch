package com.example.go4lunch.viewmodels;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.repositories.RestaurantRepository;

public class RestaurantViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> restaurantsLiveData;
    private MutableLiveData<Restaurant> restaurantLiveData;
    private RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>> getRestaurants(Location location, int radius, String type) {
        if (restaurantsLiveData == null) {
            restaurantsLiveData = restaurantRepository.getRestaurants(location, radius);
        }
        return restaurantsLiveData;
    }

    public LiveData<Restaurant> getRestaurantById(String placeId) {
        if (restaurantLiveData == null) {
            restaurantLiveData = restaurantRepository.getRestaurantById(placeId);
        }
        return restaurantLiveData;
    }
}
