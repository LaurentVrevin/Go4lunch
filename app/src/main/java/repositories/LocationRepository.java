package repositories;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.maps.model.LatLng;



public class LocationRepository implements LocationInterface {
    private MutableLiveData<LatLng>userLocation = new MutableLiveData<>();


    @Override
    public void updateUserLocation(LatLng location) {
        userLocation.setValue(location);
    }

    @Override
    public MutableLiveData<LatLng> getUserLocation() {
        return userLocation;
    }
}