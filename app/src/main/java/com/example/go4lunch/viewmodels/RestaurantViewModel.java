package com.example.go4lunch.viewmodels;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.repositories.RestaurantInterface;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {
    private final RestaurantInterface restaurantInterface;
    private final MutableLiveData<List<Restaurant>> listRestaurantLiveData = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> selectedRestaurantLiveData = new MutableLiveData<>();

    private final MutableLiveData<Integer> radiusLiveData = new MutableLiveData<>();

    public int radius = 200;

    @Inject
    public RestaurantViewModel(RestaurantInterface restaurantInterface) {
        this.restaurantInterface = restaurantInterface;
    }

    public void getRestaurants(Location location) {
        restaurantInterface.getRestaurants(location, radius)
                .observeForever(restaurant -> {
                    if (restaurant != null) {
                        listRestaurantLiveData.setValue(restaurant);
                    }
                });
    }

    public void getRestaurantById(String placeId) {
        restaurantInterface.getRestaurantById(placeId)
                .observeForever(restaurant -> {
                    if (restaurant != null) {
                        selectedRestaurantLiveData.setValue(restaurant);
                    }
                });
    }

    public LiveData<List<Restaurant>> getListRestaurantLiveData() {
        return listRestaurantLiveData;
    }

    public LiveData<Restaurant> getSelectedRestaurantLiveData() {
        return selectedRestaurantLiveData;
    }

    // Méthode pour récupérer les détails du restaurant sélectionné par l'utilisateur
    public void setSelectedRestaurant(Restaurant restaurant) {
        selectedRestaurantLiveData.setValue(restaurant);
    }


    public void updateRadius(int selectedRadius) {
        restaurantInterface.updateRadius(selectedRadius);
        radiusLiveData.setValue(selectedRadius);
        radius = selectedRadius;
    }

    public LiveData<Integer> getRadiusLiveData() {

        return this.radiusLiveData;
    }

}