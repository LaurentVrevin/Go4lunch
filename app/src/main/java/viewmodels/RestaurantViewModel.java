package viewmodels;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import models.Restaurant;
import repositories.RestaurantRepository;

public class RestaurantViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> restaurantsLiveData;
    private MutableLiveData<Restaurant> restaurantLiveData;
    private RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>> getRestaurants(Location location, int radius, String type) {
        if (restaurantsLiveData == null) {
            restaurantsLiveData = restaurantRepository.getRestaurants(location, radius);
        }
        return restaurantsLiveData;
    }

    public LiveData<Restaurant> getRestaurantById(String placeId) {
        if (restaurantLiveData == null) {
            restaurantLiveData = restaurantRepository.getRestaurantById(placeId);
        }
        return restaurantLiveData;
    }
}
