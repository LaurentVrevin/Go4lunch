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
    public int getRadius() {
        return 500;
    }

    //je lui passe en paramètre la position du device, pour le moment je la prédéfinis.
    // Je verrais ensuite pour récupérer directement celle du device
    @Override
    public Location getCurrentLocation() {
        Location deviceLocation = new Location((USER_POSITION));
        deviceLocation.setLatitude(USER_LATITUDE);
        deviceLocation.setLongitude(USER_LONGITUDE);
        return deviceLocation;
    }

    // Renvoie la localisation courante sous forme d'un objet LiveData
    public LiveData<Location> getLiveDataCurrentLocation() {
        return locationMutableLiveData;
    }

    @SuppressLint("MissingPermission")
    // Démarre la récupération de la localisation en utilisant les services de localisation de Google Play
    public void startLocationRequest(Context context, Activity activity) {
        // Instantiation du client FusedLocationProviderClient
        instantiateFusedLocationProviderClient(context);

        // Configuration de la demande de localisation
        setupLocationRequest();

        // Création du rappel pour recevoir les mises à jour de localisation
        createLocationCallback();

        // Démarre la mise à jour de la localisation
        startLocationUpdates(activity);
    }

    // Arrête la mise à jour de la localisation
    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    // Instantiation de FusedLocationProviderClient
    private void instantiateFusedLocationProviderClient(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Configuration de la demande de localisation
    private void setupLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_PROVIDER_IN_MS);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Création du rappel pour recevoir les mises à jour de localisation
    public void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }

                // Parcours les emplacements reçus et met à jour la valeur de locationMutableLiveData avec la dernière localisation
                for (Location location : locationResult.getLocations()) {
                    locationMutableLiveData.setValue(location);
                }
            }
        };
    }

    // Démarre la mise à jour de la localisation
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(Activity activity) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    //ANCIEN CODE
    /*
    private MutableLiveData<LatLng>userLocation = new MutableLiveData<>();
    @Override
    public void updateUserLocation(LatLng location) {
        userLocation.setValue(location);
    }

    @Override
    public MutableLiveData<LatLng> getUserLocation() {
        return userLocation;
    }*/
}