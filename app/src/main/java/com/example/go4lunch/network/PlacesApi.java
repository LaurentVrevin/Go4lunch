package com.example.go4lunch.network;

import com.example.go4lunch.BuildConfig;

import com.example.go4lunch.models.nearbysearch.NearbySearchResponse;
import com.example.go4lunch.models.nearbysearch.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get data from Google places API
 */


public interface PlacesApi {
    @GET("nearbysearch/json?type=restaurant&key=" + BuildConfig.MAPS_API_KEY)
    Call<NearbySearchResponse> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius);

    @GET("details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetailsResponse> getPlaceDetailsResponse(
            @Query("fields") String fields,
            @Query("place_id") String placeId);

}
