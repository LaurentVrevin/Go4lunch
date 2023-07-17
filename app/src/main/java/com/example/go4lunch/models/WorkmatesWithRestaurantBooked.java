package com.example.go4lunch.models;

import androidx.lifecycle.LiveData;

public class WorkmatesWithRestaurantBooked {
    private User workmate;
    private Restaurant restaurant;

    public WorkmatesWithRestaurantBooked(User workmate, Restaurant restaurant) {
        this.workmate = workmate;
        this.restaurant = restaurant;
    }

    public WorkmatesWithRestaurantBooked(LiveData<User> userLiveData, Restaurant restaurant) {
        userLiveData.observeForever(user -> {
            if (user != null && user.getUserId().equals(workmate.getUserId())) {
                workmate = user;
            }
        });
        this.restaurant = restaurant;
    }

    public User getWorkmate() {
        return workmate;
    }

    public void setWorkmate(User workmate) {
        this.workmate = workmate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
