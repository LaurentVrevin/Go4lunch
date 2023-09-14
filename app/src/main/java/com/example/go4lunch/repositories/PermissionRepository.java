package com.example.go4lunch.repositories;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionRepository implements PermissionInterface {

    // Create a MutableLiveData<Boolean> object to store the state of permissions
    final MutableLiveData<Boolean> hasPermissions = new MutableLiveData<>();

    @Inject
    public PermissionRepository() {

    }

    @Override
    public boolean hasLocationPermissions(Context context) {
        // Check if location permissions are granted
        return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
        );
    }

    @Override
    public LiveData<Boolean> liveDataHasLocationPermission(Context context) {
        // Check location permissions and update the MutableLiveData object
        hasPermissions.setValue(EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION));
        return hasPermissions; // Return the MutableLiveData object for observing changes
    }
}
