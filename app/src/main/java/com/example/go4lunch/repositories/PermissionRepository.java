package com.example.go4lunch.repositories;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionRepository implements PermissionInterface {

    // Crée un objet MutableLiveData<Boolean> pour stocker l'état des permissions
    final MutableLiveData<Boolean> hasPermissions = new MutableLiveData<>();

    @Inject
    public PermissionRepository() {
        // Constructeur de la classe PermissionRepository
    }

    @Override
    public boolean hasLocationPermissions(Context context) {
        // Vérifie si les permissions de localisation sont accordées
        return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
        );
    }

    @Override
    public LiveData<Boolean> liveDataHasLocationPermission(Context context) {
        // Vérifie les permissions de localisation et met à jour l'objet MutableLiveData
        hasPermissions.setValue(EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION));
        return hasPermissions; // Renvoie l'objet MutableLiveData pour observer les changements
    }
}
