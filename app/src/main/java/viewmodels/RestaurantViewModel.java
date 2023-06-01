package viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import models.Restaurant;

public class RestaurantViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> restaurants;
    public LiveData<List<Restaurant>> getRestaurants(){
        if (restaurants == null){
            restaurants = new MutableLiveData<List<Restaurant>>();
            loadRestaurants();
        }
        return restaurants;
    }

    private void loadRestaurants() {

    }

    public RestaurantViewModel(){
    }
}
