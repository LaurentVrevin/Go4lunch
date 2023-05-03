package models;

import java.util.List;

import javax.annotation.Nullable;

public class User {

    private String userId;
    private String name;

    private String email;
    @Nullable
    private String pictureUrl;
    private List<String> likedPlaces;

    //CONSTRUCTOR
    public User(String userId, String name, String email, @Nullable String pictureUrl, List<String> likedPlaces) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.likedPlaces = likedPlaces;
    }
    public User() {
        // constructeur sans argument requis pour Firestore
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
}
