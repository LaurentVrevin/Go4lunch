package repositories;


import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;

public interface LocationInterface {
    void updateUserLocation(LatLng location);
    LiveData<LatLng>getUserLocation();
}