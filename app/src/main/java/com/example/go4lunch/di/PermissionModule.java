package com.example.go4lunch.di;


import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

import com.example.go4lunch.repositories.PermissionInterface;
import com.example.go4lunch.repositories.PermissionRepository;

@InstallIn(ViewModelComponent.class)
@Module
public abstract class PermissionModule {
    // I bind PermissionRepository to PermissionInterface, which will make injection easier for me.
    @Binds
    public abstract PermissionInterface bindPermissionInterface(PermissionRepository permissionRepository);
}
