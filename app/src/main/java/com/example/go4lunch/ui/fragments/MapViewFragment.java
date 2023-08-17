package com.example.go4lunch.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.activities.YourLunchDetailActivity;
import com.example.go4lunch.utils.WorkmatesCounter;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.example.go4lunch.models.Restaurant;

import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@AndroidEntryPoint
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final float MAX_ZOOM = 18.0f;
    private GoogleMap googleMap;
    private List<Restaurant> restaurantListData;
    private List<User> userListData;
    private LocationPermissionViewModel locationPermissionViewModel;
    private RestaurantViewModel restaurantViewModel;
    private UserViewModel userViewModel;
    private Location location;
    
    private static HashMap<String, Integer> workmatesCountMap = new HashMap<>();

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
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void observePermission() {
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::observeData);
    }

    private void observeData(Boolean hasPermission) {
        if (hasPermission) {
            locationPermissionViewModel.getCurrentLocation().observe(requireActivity(), this::updateLocation);
            restaurantViewModel.getListRestaurantLiveData().observe(requireActivity(), this::updateRestaurantList);
            restaurantViewModel.getListRestaurantLiveData().observe(requireActivity(), this::setMarkers);
            //userViewModel.getWorkmatesListFromFirestore(false);

            userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userList -> {
                if (userList != null) {
                    updateWorkmatesList(userList);
                    updateWorkmatesCount(userList);
                }
            });
        }
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::setCamera);
        if (location !=null) {
            getCurrentLocation(googleMap);
            googleMap.setMyLocationEnabled(true);
            setMarkers(restaurantListData); // Affiche les marqueurs des restaurants sur la carte

        }
        // Mettre en place le gestionnaire d'événements OnMarkerClickListener
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Vérifier si le clic provient d'un marqueur de restaurant
                if (restaurantListData != null) {
                    for (Restaurant restaurant : restaurantListData) {
                        if (restaurant.getName().equals(marker.getTitle())) {
                            // Le marqueur appartient à un restaurant, ouvrir l'activité de détails du restaurant
                            openYourLunchDetailActivity(restaurant);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

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
        if (restaurantList != null) {
            restaurantListData = restaurantList;
            setMarkers(restaurantListData);
        }
    }

    //Je passe la liste du livedata dans userListData avec pour objet User via users
    private void updateWorkmatesList(List<User> users){
        userListData = users;
    }
    private void updateWorkmatesCount(List<User> userList) {
        WorkmatesCounter.updateWorkmatesCount(userList, workmatesCountMap);
    }

    private void setMarkers(List<Restaurant> restaurantList) {
        googleMap.clear(); // Efface tous les marqueurs précédents

        if(restaurantList !=null) {
            for (Restaurant restaurant : restaurantList) {
                if(restaurant !=null){
                    LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(restaurant.getName());

                    // Vérifier si le restaurant a été choisi par un ou plusieurs workmates
                    if (workmatesCountMap.containsKey(restaurant.getPlaceId())) {
                        // Le restaurant a été choisi, donc marquer en vert
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else {
                        // Le restaurant n'a pas été choisi, garder l'icône par défaut
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    googleMap.addMarker(markerOptions);
                    Log.d("Restaurant", "Nom : " + restaurant.getName() + "les horaires " + restaurant.getOpeningHours());
                }

            }
        }
    }
    private void openYourLunchDetailActivity(Restaurant restaurant) {

        Intent intent = new Intent(requireContext(), YourLunchDetailActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
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
    //SEARCH
    public void filterMarkers(String query) {
        googleMap.clear(); // Effacer tous les marqueurs précédents

        if (restaurantListData != null) {
            for (Restaurant restaurant : restaurantListData) {
                if (restaurant != null && restaurant.getName() != null && restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                    LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(restaurant.getName());

                    // Vérifier si le restaurant a été choisi par un ou plusieurs workmates
                    if (workmatesCountMap.containsKey(restaurant.getPlaceId())) {
                        // Le restaurant a été choisi, donc marquer en vert
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else {
                        // Le restaurant n'a pas été choisi, garder l'icône par défaut
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    googleMap.addMarker(markerOptions);
                }
            }
        }
    }

}
