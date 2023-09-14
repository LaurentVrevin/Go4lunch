package com.example.go4lunch.di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import com.example.go4lunch.repositories.LocationInterface;
import com.example.go4lunch.repositories.LocationRepository;

@InstallIn(SingletonComponent.class)
@Module
public abstract class LocationModule {

    @Binds
    public abstract LocationInterface bindLocationInterface(LocationRepository locationRepository);
}
