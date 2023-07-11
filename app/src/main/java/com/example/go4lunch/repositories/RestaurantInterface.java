package com.example.go4lunch.repositories;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.Restaurant;

import java.util.List;

public interface RestaurantInterface {

    MutableLiveData<List<Restaurant>> getRestaurants(Location location, int radius);

    MutableLiveData<Restaurant> getRestaurantById(String placeId);

}
