package com.example.go4lunch.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.example.go4lunch.BuildConfig;
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
    private float rating;
    @Nullable
    private Double distance;
    private int workmatesCount;

    public Restaurant() {
        // constructeur sans argument requis pour Firestore
    }

    public Restaurant(Result result) {
        this.id = result.getPlaceId();
        this.name = result.getName();
        this.address = result.getVicinity();
        this.phone = result.getFormattedPhoneNumber();
        this.websiteUrl = result.getWebsite();
        this.photoUrl = result.getPhotos();
        this.latitude = result.getGeometry().getLocation().getLat();
        this.longitude = result.getGeometry().getLocation().getLng();
        if (result.getOpeningHours() != null) {
            this.openingHours = result.getOpeningHours().isOpenNow() ? "Ouvert" : "Fermé";
        }
        if (result.getOpeningHours() != null && result.getOpeningHours().getCloseTime() != null) {
            this.closingHours = result.getOpeningHours().getCloseTime();
        }
        this.rating = (float) result.getRating();
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

    public List<String> getPhotoUrls() {
        List<String> photoUrls = new ArrayList<>();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            for (Photo photo : photoUrl) {
                String photoReference = photo.getPhotoReference();
                String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
                photoUrls.add(photoUrl);
            }
        }
        return photoUrls;
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

    public float getRating() {
        return rating;
    }

    public Double getDistance() {
        //retourne 0.0 si la distance est null
        return distance !=null ? distance : 0.0 ;
    }
    public void setDistance(@Nullable Double distance) {
        this.distance = distance;
    }
    public int getWorkmatesCount() {
        return workmatesCount;
    }

    public void setWorkmatesCount(int workmatesCount) {
        this.workmatesCount = workmatesCount;
    }
}
