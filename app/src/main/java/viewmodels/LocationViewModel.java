package viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();

    public void updateLocation(LatLng location) {
        currentLocation.setValue(location);
    }

    public MutableLiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }
}