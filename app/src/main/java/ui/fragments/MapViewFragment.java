package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import viewmodels.MapViewViewModel;
import viewmodels.UserViewModel;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private LatLng currentMapPosition;
    private static final float MIN_ZOOM = 10.0f;
    private static final float MAX_ZOOM = 18.0f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private UserViewModel mUserViewModel;
    private MapViewViewModel mMapViewViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configureViewModels();
        return inflater.inflate(R.layout.fragment_mapview, container, false);
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mMapViewViewModel = new ViewModelProvider(requireActivity()).get(MapViewViewModel.class);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Exemple : Déplacer la caméra vers une position spécifique
        //LatLng position = new LatLng(49.1828, -0.3700); // Exemple : Caen
        if (currentMapPosition != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMapPosition, 12));
            googleMap.addMarker(new MarkerOptions().position(currentMapPosition).title("My Position"));
        } else if (hasLocationPermission()) {
            showUserLocation();
        } else {
            requestLocationPermission();
        }
        // Activer les contrôles de zoom
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Définir les limites de zoom
        googleMap.setMinZoomPreference(MIN_ZOOM);
        googleMap.setMaxZoomPreference(MAX_ZOOM);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (googleMap != null) {
            outState.putParcelable("map_position", googleMap.getCameraPosition().target);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            currentMapPosition = savedInstanceState.getParcelable("map_position");
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
    private void showUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 12));
                    googleMap.addMarker(new MarkerOptions().position(myPosition).title("My Position"));
                }
            }
        });
    }

    private boolean hasLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        return EasyPermissions.hasPermissions(requireContext(), perms);
    }

    private void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        EasyPermissions.requestPermissions(this, "Location permission is required to access your current location.", LOCATION_PERMISSION_REQUEST_CODE, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            showUserLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            Toast.makeText(requireContext(), "Malheureusement, l'application a besoin de votre localisation pour fonctionner", Toast.LENGTH_SHORT).show();
        mUserViewModel.logOut();
        }
    }
}
