package com.example.go4lunch.viewmodels;



import android.app.Activity;
import android.content.Context;
import android.location.Location;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import com.example.go4lunch.repositories.LocationInterface;
import com.example.go4lunch.repositories.PermissionInterface;


@HiltViewModel
public class LocationPermissionViewModel extends ViewModel  {


    private final PermissionInterface permissionInterface;
    private final LocationInterface locationInterface;

    private final MutableLiveData<Boolean> hasPermissions = new MutableLiveData<>();

    @Inject
    public LocationPermissionViewModel(PermissionInterface permissionInterface, LocationInterface locationInterface) {
        this.permissionInterface = permissionInterface;
        this.locationInterface = locationInterface;
    }

    //LOCATION

    public LatLng getUserLatLng(){
        Location userLocation = locationInterface.getCurrentLocation();
        return new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
    }

    public Location getUserLocation() {
        return locationInterface.getCurrentLocation();
    }

    public void startUpdateLocation(Context context, Activity activity) {
        locationInterface.startLocationRequest(context, activity);
    }

    public void stopUpdateLocation() {
        locationInterface.stopLocationUpdates();
    }

    public LiveData<Location> getCurrentLocation() {
        return locationInterface.getLiveDataCurrentLocation();
    }

    //PERMISSION

    public Boolean hasPermission(Context context) {
        return permissionInterface.hasLocationPermissions(context);
    }

    public void permissionSet(boolean hasPermission) {
        hasPermissions.setValue(hasPermission);
    }

    public LiveData<Boolean> observePermission() {
        return hasPermissions;
    }

    public LiveData<Boolean> liveDataHasPermission(Context context) {
        return permissionInterface.liveDataHasLocationPermission(context);
    }

}
