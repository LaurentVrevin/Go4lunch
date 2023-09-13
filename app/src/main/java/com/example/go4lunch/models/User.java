package com.example.go4lunch.models;


import androidx.annotation.Nullable;

import java.util.List;

public class User {

    private String userId;
    private String name;
    private String email;
    @Nullable
    private String pictureUrl;
    private List<String> likedPlaces;
    @Nullable
    private String selectedRestaurantId;

    //CONSTRUCTOR
    public User(String userId, String name, String email, @Nullable String pictureUrl, List<String> likedPlaces, @Nullable String selectedRestaurantId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.likedPlaces = likedPlaces;
        this.selectedRestaurantId = selectedRestaurantId;
    }
    public User() {

    }

    //GETTERS & SETTERS
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<String> getLikedPlaces() {
        return likedPlaces;
    }

    public void setLikedPlaces(List<String> likedPlaces) {
        this.likedPlaces = likedPlaces;
    }

    @Nullable
    public String getSelectedRestaurantId() {
        return selectedRestaurantId;
    }

    public void setSelectedRestaurantId(@Nullable String selectedRestaurantId) {
        this.selectedRestaurantId = selectedRestaurantId;
    }
}
