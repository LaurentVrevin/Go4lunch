package repositories;


import java.util.List;

import models.Restaurant;
import retrofit2.Callback;

public interface MapViewInterfaceRepository {
    void fetchNearbyRestaurants(String location, Callback<List<Restaurant>> callback);
}