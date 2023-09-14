package com.example.go4lunch.repositories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;


public class LocationRepository implements LocationInterface {

    private static final int LOCATION_REQUEST_PROVIDER_IN_MS = 60000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 50;
    private static final float USER_LATITUDE = 49.1828f;
    private static final float USER_LONGITUDE = -0.3700f;
    private static final String USER_POSITION = "Position";
    public final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Inject
    public LocationRepository() {
    }

    @Override
    public Location getCurrentLocation() {
        Location deviceLocation = new Location((USER_POSITION));
        deviceLocation.setLatitude(USER_LATITUDE);
        deviceLocation.setLongitude(USER_LONGITUDE);
        return deviceLocation;
    }

    // Returns the current location as a LiveData object
    public LiveData<Location> getLiveDataCurrentLocation() {
        return locationMutableLiveData;
    }

    @SuppressLint("MissingPermission")
    // Start location retrieval using Google Play location services
    public void startLocationRequest(Context context, Activity activity) {
        // Instantiate the FusedLocationProviderClient
        instantiateFusedLocationProviderClient(context);

        // Configure the location request
        setupLocationRequest();

        // Create a callback to receive location updates
        createLocationCallback();

        // Start location updates
        startLocationUpdates(activity);
    }

    // Stop location updates
    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    // Instantiate FusedLocationProviderClient
    private void instantiateFusedLocationProviderClient(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Configure the location request
    private void setupLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_PROVIDER_IN_MS);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Create a callback to receive location updates
    public void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }

                // Iterate through the received locations and update the value of locationMutableLiveData with the latest location
                for (Location location : locationResult.getLocations()) {
                    locationMutableLiveData.setValue(location);
                }
            }
        };
    }

    // Start location updates
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(Activity activity) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
}