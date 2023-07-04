package com.example.go4lunch.ui.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.repositories.LocationInterface;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.utils.LocationPermission;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.google.android.gms.maps.model.MarkerOptions;

@AndroidEntryPoint
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final float MAX_ZOOM = 18.0f;
    private GoogleMap googleMap;
    private List<Restaurant> restaurantListData;
    private LocationPermissionViewModel locationPermissionViewModel;
    private RestaurantViewModel restaurantViewModel;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configureViewModels();
        return inflater.inflate(R.layout.fragment_mapview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        observePermission();
    }

    private void configureViewModels() {
        locationPermissionViewModel = new ViewModelProvider(requireActivity()).get(LocationPermissionViewModel.class);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }

    private void observePermission() {
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::observeData);
    }

    private void observeData(Boolean hasPermission) {
        if (hasPermission) {
            locationPermissionViewModel.getCurrentLocation().observe(requireActivity(), this::updateLocation);
            restaurantViewModel.getRestaurantsLiveData().observe(requireActivity(), this::updateRestaurantList);
            restaurantViewModel.getRestaurantsLiveData().observe(requireActivity(), this::setMarkers);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //this.googleMap.setMapStyle();
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::setCamera);
        if (location !=null) {
            getCurrentLocation(googleMap);
            googleMap.setMyLocationEnabled(true);
            setMarkers(restaurantListData); // Affiche les marqueurs des restaurants sur la carte
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void updateLocation(Location location) {
        if (this.isAdded()) {
            this.location = location;
            enableMyLocation(googleMap);
            getCurrentLocation(googleMap);
        }
    }

    @SuppressLint("MissingPermission")
    public void enableMyLocation(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(GoogleMap googleMap) {
        if (location != null) {
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, MAX_ZOOM));
        }
    }
    private void setCamera(boolean hasPermission) {
        if (hasPermission) {
            enableMyLocation(googleMap);
            getCurrentLocation(googleMap);
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPermissionViewModel.getUserLatLng(), MAX_ZOOM));
        }
    }
    private void updateRestaurantList(List<Restaurant> restaurantList) {
        restaurantListData = restaurantList;
        setMarkers(restaurantListData);
    }

    private void setMarkers(List<Restaurant> restaurantList) {
        googleMap.clear(); // Efface tous les marqueurs précédents


        if(restaurantList !=null) {
            for (Restaurant restaurant : restaurantList) {
                LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(restaurant.getName());
                googleMap.addMarker(markerOptions);
                Log.d("Restaurant", "Nom : " + restaurant.getName() + "les horaires " + restaurant.getOpeningHours());
            }
        }
    }

    @Override
     public void onStart() {
         super.onStart();
     }

     @Override
     public void onResume() {
         super.onResume();
     }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
             super.onSaveInstanceState(outState);
             if (googleMap != null) {
                 outState.putParcelable("map_position", googleMap.getCameraPosition().target);
             }
         }

}
