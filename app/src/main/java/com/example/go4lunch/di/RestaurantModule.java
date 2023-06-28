package com.example.go4lunch.di;


import com.example.go4lunch.repositories.RestaurantInterface;
import com.example.go4lunch.repositories.RestaurantRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public abstract class RestaurantModule {

    @Binds
    public abstract RestaurantInterface bindRestaurantInterface(RestaurantRepository restaurantRepository);

}
