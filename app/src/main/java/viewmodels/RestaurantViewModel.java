package viewmodels;


import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.util.List;

import models.Restaurant;
import repositories.RestaurantInterfaceRepository;
import repositories.RestaurantRepositoryImpl;

public class RestaurantViewModel extends ViewModel {
    private final RestaurantInterfaceRepository restaurantInterfaceRepository;

    public RestaurantViewModel(Context context) {
        this.restaurantInterfaceRepository = new RestaurantRepositoryImpl(context);
    }

    public void createRestaurantCollection() {
        restaurantInterfaceRepository.createRestaurantCollection();
    }

    public void addRestaurantsToFirestore(List<Restaurant> restaurants) {
        restaurantInterfaceRepository.addRestaurantsToFirestore(restaurants);
    }

    public void updateRestaurantsToFirestore(List<Restaurant> restaurants) {
        restaurantInterfaceRepository.updateRestaurantsToFirestore(restaurants);
    }

    public void deleteRestaurantsToFirestore() {
        restaurantInterfaceRepository.deleteRestaurantsToFirestore();
    }

    public List<Restaurant> getRestaurantsToFirestore() {
        return restaurantInterfaceRepository.getRestaurantsToFirestore();
    }
}
