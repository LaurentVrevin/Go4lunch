package com.example.go4lunch.di;



import com.example.go4lunch.repositories.UserInterface;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

import dagger.Binds;
import dagger.Module;

import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public abstract class UserModule {
    @Binds
    public abstract UserInterface bindUserInterface(UserRepository userRepository);
}