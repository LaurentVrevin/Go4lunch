package com.example.go4lunch.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Util class to manage Retrofit builder
 **/
public class RetrofitBuilder {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    public static PlacesApi buildPlacesApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PlacesApi.class);
    }
}
