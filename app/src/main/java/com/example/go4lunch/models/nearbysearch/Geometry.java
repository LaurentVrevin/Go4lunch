package com.example.go4lunch.models.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* "Geometry" sera utilisée pour représenter les informations de géométrie dans la réponse de la recherche à proximité */

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
