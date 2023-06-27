package com.example.go4lunch.utils;


import com.example.go4lunch.viewmodels.LocationPermissionViewModel;



public class LocationPermission {


    /*private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    public final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final AppCompatActivity activity;
    private LocationCallback locationCallback;
    private LocationPermissionViewModel mLocationPermissionViewModel;
    private MainActivity mainActivity;


    public LocationPermission(AppCompatActivity activity) {
        this.activity = activity;
        this.isPermissionGranted=hasLocationPermission(activity);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        // Mettez à jour la localisation de l'utilisateur avec la nouvelle position
                        mLocationPermissionViewModel.updateUserLocation(userLocation);
                        Log.d("MainActivity", "User location updated: " + userLocation.latitude + ", " + userLocation.longitude);
                    }
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

    }

    public void checkLocationPermissions() {
        mLocationPermissionViewModel = new ViewModelProvider(activity).get(LocationPermissionViewModel.class);
        if (EasyPermissions.hasPermissions(activity, perms)) {
            // Les autorisations de localisation sont déjà accordées
            getUserPosition();
        } else {
            // Les autorisations de localisation ne sont pas accordées, demander à l'utilisateur de les accorder
            EasyPermissions.requestPermissions(activity, "You have to accept localisation", LOCATION_PERMISSION_REQUEST_CODE, perms);
        }
    }
    private boolean isPermissionGranted = false;


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Les autorisations de localisation ont été accordées par l'utilisateur
        // mLocationViewModel = new ViewModelProvider(activity).get(LocationViewModel.class);
        this.isPermissionGranted = true;
        getUserPosition();

    }
    private boolean hasLocationPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Vérifier si l'utilisateur a refusé définitivement les autorisations
        //mLocationViewModel = new ViewModelProvider(activity).get(LocationViewModel.class);
        mainActivity.logOutAndRedirect();
    }



    @SuppressLint("MissingPermission")
    public void getUserPosition() {
        if(!this.isPermissionGranted){
            return;
        }

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());
        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);

    }


    public LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20000); // Intervalles de mise à jour de la position en millisecondes
        return locationRequest;
    }

    @SuppressLint("MissingPermission")
    public void stopLocationUpdates() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Transmettre les résultats de la demande d'autorisations à EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }*/
}
