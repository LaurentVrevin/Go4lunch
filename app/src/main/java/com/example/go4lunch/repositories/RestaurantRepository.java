package com.example.go4lunch.repositories;

import android.util.Log;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

import com.example.go4lunch.models.Restaurant;

import com.example.go4lunch.models.nearbysearch.NearbySearchResponse;
import com.example.go4lunch.models.nearbysearch.PlaceDetailsResponse;
import com.example.go4lunch.models.nearbysearch.Result;
import com.example.go4lunch.network.PlacesApi;
import com.example.go4lunch.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private MutableLiveData<List<Restaurant>> restaurantsLiveData;
    private final PlacesApi placesApi;

    public RestaurantRepository() {
        restaurantsLiveData = new MutableLiveData<>();
        placesApi = RetrofitBuilder.buildPlacesApi();
    }


    public MutableLiveData<List<Restaurant>> getRestaurants(Location location, int radius) {

        radius = 200;

        // Créer l'appel d'API pour rechercher les lieux à proximité
        Call<NearbySearchResponse> nearbySearchResponseCall = placesApi.nearbySearch(location.getLatitude() + "," + location.getLongitude(), radius);
        nearbySearchResponseCall.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    // La réponse de l'appel d'API est réussie
                    NearbySearchResponse nearbySearchResponse = response.body();
                    if (nearbySearchResponse != null) {
                        // Obtenir la liste des résultats de recherche
                        List<Result> resultList = nearbySearchResponse.getResults();

                        // Convertir les résultats en objets Restaurant
                        List<Restaurant> restaurantListData = new ArrayList<>();
                        for (Result result : resultList) {
                            Restaurant restaurant = new Restaurant(result);
                            restaurantListData.add(restaurant);
                        }
                        // Calculer et définir la distance entre la localisation de l'utilisateur et les restaurants
                        for (Restaurant restaurant : restaurantListData) {
                            Location restaurantLocation = new Location("userLocation");
                            restaurantLocation.setLatitude(restaurant.getLatitude());
                            restaurantLocation.setLongitude(restaurant.getLongitude());
                            restaurant.setDistance((double) location.distanceTo(restaurantLocation));
                        }

                        // Afficher les données des restaurants dans le log
                        for (Restaurant restaurant : restaurantListData) {
                            Log.e("GETRESTOLIST", "onResponse: " + restaurant.toString());
                        }

                        // Mettre à jour la carte avec les marqueurs
                        updateMapWithMarkers(restaurantListData);

                        // Mettre à jour les données des restaurants LiveData
                        restaurantsLiveData.setValue(restaurantListData);
                    }
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                // Gérer l'échec de l'appel d'API
            }
        });

        return restaurantsLiveData;
    }

    public MutableLiveData<Restaurant> getRestaurantById(String placeId) {
        MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();

        // Créer l'appel d'API pour obtenir les détails d'un lieu
        Call<PlaceDetailsResponse> placeDetailsResponseCall = placesApi.getPlaceDetailsResponse("name,formatted_address,formatted_phone_number,website,photos,rating,geometry", placeId);
        placeDetailsResponseCall.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                if (response.isSuccessful()) {
                    // La réponse de l'appel d'API est réussie
                    PlaceDetailsResponse placeDetailsResponse = response.body();
                    if (placeDetailsResponse != null) {
                        // Obtenir les détails du restaurant à partir de la réponse
                        Result result = placeDetailsResponse.getResult();
                        if (result != null) {
                            // Créer un objet Restaurant à partir des détails
                            Restaurant restaurant = new Restaurant(result);

                            // Mettre à jour la distance entre la localisation de l'utilisateur et le restaurant
                            Location restaurantLocation = new Location("userLocation");
                            restaurantLocation.setLatitude(restaurant.getLatitude());
                            restaurantLocation.setLongitude(restaurant.getLongitude());
                            restaurant.setDistance((double) restaurantLocation.distanceTo(restaurantLocation));

                            // Mettre à jour les données du restaurant LiveData
                            restaurantLiveData.setValue(restaurant);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                // Gérer l'échec de l'appel d'API
            }
        });

        return restaurantLiveData;
    }


    private void updateMapWithMarkers(List<Restaurant> restaurantListData) {
        // Parcourir la liste des restaurants
        for (Restaurant restaurant : restaurantListData) {
            // Obtenir les informations nécessaires du restaurant
            String name = restaurant.getName();
            double latitude = restaurant.getLatitude();
            double longitude = restaurant.getLongitude();

            // Créer un marqueur pour le restaurant sur la carte
            LatLng restaurantLatLng = new LatLng(latitude, longitude);
            /* googleMap.addMarker(new MarkerOptions()
                    .position(restaurantLatLng)
                    .title(name)); */
        }
    }


}
