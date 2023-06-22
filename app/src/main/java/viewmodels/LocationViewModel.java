package viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import repositories.LocationInterface;
import repositories.LocationRepository;

public class LocationViewModel extends ViewModel  {
    private LocationInterface mLocationInterface;
    private LocationRepository mLocationRepository;
    private MutableLiveData<LatLng>userLocation = new MutableLiveData<>();

    public LocationViewModel(LocationRepository locationRepository){
        this.mLocationRepository=locationRepository;
    }
    public LocationViewModel(){
        mLocationRepository = new LocationRepository();
    }

    // Met à jour la position de l'utilisateur
    public void updateUserLocation(LatLng location){
        userLocation.setValue(location);
    }

    // Récupère les mises à jour de la position de l'utilisateur
    public MutableLiveData<LatLng> getUserLocation(){
        return userLocation;
    }

    public void setLocationRepository(LocationRepository locationRepository) {
        mLocationRepository = locationRepository;
    }
}
