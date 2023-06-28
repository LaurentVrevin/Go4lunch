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

    public Restaurant(Result result) {
        this.id = result.getPlaceId();
        this.name = result.getName();
        this.address = result.getFormattedAddress();
        this.phone = result.getFormattedPhoneNumber();
        this.websiteUrl = result.getWebsite();
        this.photoUrl = result.getPhotos();
        this.latitude = result.getGeometry().getLocation().getLat();
        this.longitude = result.getGeometry().getLocation().getLng();
        if (result.getOpeningHours() != null) {
            this.openingHours = result.getOpeningHours().getOpeningHours(result);
        }
        if (result.getOpeningHours() != null) {
            this.closingHours = result.getOpeningHours().getClosingHours(result);
        }
        this.rating = result.getRating();
        this.distance=null;
    }


    public String getPlaceId() {
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
        //retourne 0.0 si la distance est null
        return distance !=null ? distance : 0.0 ;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
