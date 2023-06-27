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
    //Je lie PermissionRepository à PermissionInterface, ça me permettra de faciliter l'injection
    @Binds
    public abstract PermissionInterface bindPermissionInterface(PermissionRepository permissionRepository);
}
