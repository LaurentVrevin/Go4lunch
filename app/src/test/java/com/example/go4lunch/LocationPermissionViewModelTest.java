package com.example.go4lunch;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.repositories.LocationInterface;
import com.example.go4lunch.repositories.PermissionInterface;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class LocationPermissionViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private PermissionInterface permissionInterface;

    @Mock
    private LocationInterface locationInterface;

    @Mock
    private Location location;

    private LocationPermissionViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new LocationPermissionViewModel(permissionInterface, locationInterface);
    }

    @Test
    public void testGetUserLatLng() {
        // Simulate user location
        when(location.getLatitude()).thenReturn(49.1829);
        when(location.getLongitude()).thenReturn(-0.3708);
        when(locationInterface.getCurrentLocation()).thenReturn(location);

        LatLng userLatLng = viewModel.getUserLatLng();

        assertEquals(49.1829, userLatLng.latitude, 0.000001);
        assertEquals(-0.3708, userLatLng.longitude, 0.000001);
    }

    @Test
    public void testGetUserLocation() {
        // Simulate user location
        when(locationInterface.getCurrentLocation()).thenReturn(location);

        Location userLocation = viewModel.getUserLocation();

        assertEquals(location, userLocation);
    }

    @Test
    public void testHasPermission() {
        // Simulate permission granted
        when(permissionInterface.hasLocationPermissions(Mockito.any(Context.class))).thenReturn(true);

        Boolean hasPermission = viewModel.hasPermission(Mockito.mock(Context.class));

        assertEquals(true, hasPermission);
    }

    @Test
    public void testPermissionSet() {
        MutableLiveData<Boolean> hasPermissions = new MutableLiveData<>();
        viewModel.permissionSet(true);

        LiveData<Boolean> observedPermission = viewModel.observePermission();
        observedPermission.observeForever(hasPermissions::setValue);

        assertEquals(true, hasPermissions.getValue());
    }
}



