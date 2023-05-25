package repositories;


import com.example.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import models.Restaurant;
import retrofit2.Callback;

public class MapViewRepositoryImpl implements MapViewInterfaceRepository {
    //For data
    private static final String GOOGLE_MAP_API_KEY = BuildConfig.MAPS_API_KEY;

    @Inject
    public MapViewRepositoryImpl(){

    }


    @Override
    public void fetchNearbyRestaurants(String location, Callback<List<Restaurant>> callback) {

    }
}