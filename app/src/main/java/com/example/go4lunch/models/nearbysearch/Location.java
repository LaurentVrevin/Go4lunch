package com.example.go4lunch.models.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// "Location" sera utilisée pour représenter les coordonnées de localisation dans la réponse de la recherche à proximité
public class Location {
    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
