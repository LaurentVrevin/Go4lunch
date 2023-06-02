package viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import repositories.LocationInterface;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();
    private LocationInterface locationInterface;

    @Inject
    private LocationViewModel(LocationInterface locationInterface){
        this.locationInterface = locationInterface;
    }

    public void updateLocation(LatLng location) {
        LatLng newLocation = locationInterface.getCurrentLocation();
        currentLocation.setValue(newLocation);
    }

    public MutableLiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }
}
