package com.example.go4lunch.repositories;


import android.content.Context;

import androidx.lifecycle.LiveData;

public interface PermissionInterface {

    boolean hasLocationPermissions(Context context);

    LiveData<Boolean> liveDataHasLocationPermission(Context context);
}
