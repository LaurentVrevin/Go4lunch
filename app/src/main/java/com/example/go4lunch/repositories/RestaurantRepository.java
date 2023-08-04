package com.example.go4lunch.repositories;

import android.location.Location;
import android.util.Log;


import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.nearbysearch.NearbySearchResponse;
import com.example.go4lunch.models.nearbysearch.PlaceDetailsResponse;
import com.example.go4lunch.models.nearbysearch.Result;
import com.example.go4lunch.network.PlacesApi;
import com.example.go4lunch.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantRepository implements RestaurantInterface{

    private static final String PLACES_API_KEY = BuildConfig.MAPS_API_KEY;
    private MutableLiveData<List<Restaurant>> restaurantsListLiveData;

    private final PlacesApi placesApi;
    private Map<String, Restaurant> cachedRestaurants;
    private int radius;

    @Inject
    public RestaurantRepository() {
        restaurantsListLiveData = new MutableLiveData<>();
        placesApi = RetrofitBuilder.buildPlacesApi();
        cachedRestaurants = new HashMap<>();
    }

    //On récupère les restaurants sur la position, et on les stocks dans une liste "Restaurant" observable
    public MutableLiveData<List<Restaurant>> getRestaurants(Location location, int radius) {

        String cacheKey = generateCacheKey(location.getLatitude(), location.getLongitude(), radius);

        // Vérifie si le restaurant est en cache, si c'est le cas alors je me sers des restaurants mis en cache,
        // ça limite les appels API et c'est bien pour le porte monnaie
        if (cachedRestaurants.containsKey(cacheKey)) {
            List<Restaurant> cachedData = new ArrayList<>(cachedRestaurants.values());
            restaurantsListLiveData.setValue(cachedData);
            return restaurantsListLiveData;
        }

        // Appel d'API pour rechercher les lieux à proximité
        Call<NearbySearchResponse> nearbySearchResponseCall = placesApi.nearbySearch(location.getLatitude() + "," + location.getLongitude(), radius);
        nearbySearchResponseCall.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    NearbySearchResponse nearbySearchResponse = response.body();
                    if (nearbySearchResponse != null) {
                        List<Result> resultList = nearbySearchResponse.getResults();
                        List<Restaurant> restaurantListData = new ArrayList<>();

                        for (Result result : resultList) {
                            Restaurant restaurant = new Restaurant(result);
                            restaurantListData.add(restaurant);
                        }

                        for (Restaurant restaurant : restaurantListData) {
                            Location restaurantLocation = new Location("userLocation");
                            restaurantLocation.setLatitude(restaurant.getLatitude());
                            restaurantLocation.setLongitude(restaurant.getLongitude());
                            restaurant.setDistance((double) location.distanceTo(restaurantLocation));
                        }

                        // Met à jour les données en cache
                        cacheRestaurants(location.getLatitude(), location.getLongitude(), radius, restaurantListData);

                        restaurantsListLiveData.setValue(restaurantListData);
                    }
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                // Gère l'échec de l'appel d'API
            }
        });

        return restaurantsListLiveData;
    }

    public MutableLiveData<Restaurant> getRestaurantById(String placeId) {
        MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();

        // Vérifie si le restaurant est en cache, si c'est le cas alors je me sers des restaurants mis en cache,
        // ça limite les appels API et c'est bien pour le porte monnaie
        if (cachedRestaurants.containsKey(placeId)) {
            Restaurant cachedRestaurant = cachedRestaurants.get(placeId);
            restaurantLiveData.setValue(cachedRestaurant);
            return restaurantLiveData;
        }

        // Appel d'API pour obtenir les détails d'un lieu
        Call<PlaceDetailsResponse> placeDetailsResponseCall = placesApi.getPlaceDetailsResponse("name,vicinity,formatted_phone_number,website,photos,rating,geometry", placeId);
        placeDetailsResponseCall.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                if (response.isSuccessful()) {
                    PlaceDetailsResponse placeDetailsResponse = response.body();
                    if (placeDetailsResponse != null) {
                        Result result = placeDetailsResponse.getResult();
                        if (result != null) {
                            Restaurant restaurant = new Restaurant(result);

                            Location restaurantLocation = new Location("userLocation");
                            restaurantLocation.setLatitude(restaurant.getLatitude());
                            restaurantLocation.setLongitude(restaurant.getLongitude());
                            restaurant.setDistance((double) restaurantLocation.distanceTo(restaurantLocation));

                            // Mettre à jour le restaurant en cache
                            cacheRestaurant(restaurant.getPlaceId(), restaurant);

                            restaurantLiveData.setValue(restaurant);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                // Gère l'échec de l'appel d'API
            }
        });

        return restaurantLiveData;
    }


    private void cacheRestaurants(double latitude, double longitude, int radius, List<Restaurant> restaurantsListDataCached) {
        String cacheKey = generateCacheKey(latitude, longitude, radius);

        // Remplace les anciennes données en cache
        cachedRestaurants.put(cacheKey, null);
        for (Restaurant restaurant : restaurantsListDataCached) {
            cachedRestaurants.put(restaurant.getPlaceId(), restaurant);
        }
        Log.d("RestaurantRepository", "Restaurants cached successfully.");

    }

    private void cacheRestaurant(String placeId, Restaurant restaurant) {
        cachedRestaurants.put(placeId, restaurant);
    }

    private String generateCacheKey(double latitude, double longitude, int radius) {
        return String.format("%.6f_%.6f_%d", latitude, longitude, radius);
    }

    public void updateRadius(int selectedRadius) {
        radius = selectedRadius;
    }
}