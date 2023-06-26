package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import models.Restaurant;
import repositories.LocationRepository;
import utils.LocationPermission;
import viewmodels.LocationViewModel;


public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final float MAX_ZOOM = 18.0f;
    private GoogleMap googleMap;
    private List<Restaurant> restaurantListData;
    private LocationViewModel locationViewModel;
    private LatLng currentLocation;
    private LocationPermission locationPermission;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        restaurantListData = new ArrayList<>();
        configureViewModels();
        return inflater.inflate(R.layout.fragment_mapview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //configureViewModels();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        // Observe user location updates
        locationViewModel.getUserLocation().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng location) {
                currentLocation = location;
                updateUserLocation();
            }
        });
    }

    private void configureViewModels() {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.setLocationRepository(new LocationRepository());
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (hasLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
            updateUserLocation();
        }
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }


    @SuppressLint("MissingPermission")
    public void updateUserLocation() {
        if (hasLocationPermission()){
            if (googleMap != null && currentLocation != null) {
                this.googleMap.setMyLocationEnabled(true);
                // Mettre à jour la position de l'utilisateur et placer le marqueur sur la carte
                googleMap.clear(); // Supprimer les anciens marqueurs
                   /*MarkerOptions markerOptions = new MarkerOptions()
                           .position(location)
                           .title("Your Location");
                   googleMap.addMarker(markerOptions);*/
                LatLng userLocation = currentLocation;
                // Zoom sur la position de l'utilisateur avec un niveau de zoom de 18
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, MAX_ZOOM));
                Log.d("LOCATION", "ma localisation est : " + currentLocation.latitude + ", " + currentLocation.longitude);

            }
        }
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
     public void onStart() {
         super.onStart();
         updateUserLocation();
     }

     @Override
     public void onResume() {
         super.onResume();
         updateUserLocation();
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
