package com.example.go4lunch.models;

import java.util.List;

import javax.annotation.Nullable;

import com.example.go4lunch.models.nearbysearch.Photo;
import com.example.go4lunch.models.nearbysearch.Result;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String websiteUrl;
    private List<Photo> photoUrl;
    private double latitude;
    private double longitude;
    private String openingHours;
    private String closingHours;
    private Double rating;
    @Nullable
    private Double distance;



    public Restaurant() {
        // constructeur sans argument requis pour Firestore
    }

    /*public Restaurant(String id, String name, String address, String phone, String websiteUrl,
                      List<Photo> photoUrl, double latitude, double longitude, String openingHours, String closingHours, Double rating, Double distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
        this.rating = rating;
        this.distance = distance;
    }*/

    public Restaurant(Result result) {
        this.id = result.getPlaceId();
        this.name = result.getName();
        this.address = result.getFormattedAddress();
        this.phone = result.getFormattedPhoneNumber();
        this.websiteUrl = result.getWebsite();
        this.photoUrl = result.getPhotos();
        this.latitude = result.getGeometry().getLocation().getLat();
        this.longitude = result.getGeometry().getLocation().getLng();
        this.openingHours = result.getOpeningHours().getOpeningHours(result);
        this.closingHours = result.getOpeningHours().getClosingHours(result);
        this.rating = result.getRating();
        this.distance=null;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public List<Photo> getPhotoUrl() {
        return photoUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getClosingHours() {
        return closingHours;
    }

    public Double getRating() {
        return rating;
    }

    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
