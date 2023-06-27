package com.example.go4lunch.repositories;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

public interface LocationInterface {

    LiveData<Location> getLiveDataCurrentLocation();

    void startLocationRequest(Context context, Activity activity);

    int getRadius();

    Location getCurrentLocation();

    void stopLocationUpdates();

}