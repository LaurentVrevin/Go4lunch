package repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import models.Restaurant;
import models.nearbysearch.Location;
import models.nearbysearch.NearbySearchResponse;
import models.nearbysearch.Result;
import network.PlacesApi;
import network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RestaurantConverter;

public class RestaurantRepository {

    private MutableLiveData<List<Restaurant>> restaurantsLiveData;
    private final PlacesApi placesApi;



    public RestaurantRepository() {
        restaurantsLiveData = new MutableLiveData<>();
        placesApi = RetrofitBuilder.buildPlacesApi();

    }

    public MutableLiveData<List<Restaurant>> getRestaurantsLiveData(Location location, int radius, String type) {
        MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();

        if (restaurantsLiveData != null) {
            Call<NearbySearchResponse> nearbySearchResponseCall = placesApi.nearbySearch(location.getLat() +","+ location.getLng(), radius, type);
            nearbySearchResponseCall.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                    if (response.isSuccessful()) {
                        NearbySearchResponse nearbySearchResponse = response.body();
                        if (nearbySearchResponse != null) {
                            List<Result> resultList = nearbySearchResponse.getResults();

                            // Utiliser RestaurantConverter pour convertir les résultats en objets Restaurant
                            List<Restaurant> restaurantListData = RestaurantConverter.convertToRestaurantList(nearbySearchResponse);

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
        }

        return restaurantsLiveData;
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
                    .title(name));*/
        }
    }


    public void getRestaurants() {
        // Utiliser l'instance de placesApi pour effectuer l'appel d'API et mettre à jour les données dans restaurantsLiveData

    }

    public void getRestaurantDetails(String restaurantId) {
        // Utiliser l'instance de placesApi pour effectuer l'appel d'API et mettre à jour les détails du restaurant dans restaurantsLiveData

    }
}

