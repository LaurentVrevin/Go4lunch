package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.BuildConfig;
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

import java.util.ArrayList;
import java.util.List;

import models.Restaurant;
import models.nearbysearch.NearbySearchResponse;
import models.nearbysearch.PlaceDetailsResponse;
import models.nearbysearch.Result;
import network.PlacesApi;
import network.RetrofitBuilder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.activities.LoginActivity;
import utils.RestaurantConverter;
import viewmodels.LocationViewModel;
import viewmodels.UserViewModel;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private SupportMapFragment supportMapFragment;
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private GoogleMap googleMap;
    private LatLng currentMapPosition;
    private static final float MIN_ZOOM = 10.0f;
    private static final float MAX_ZOOM = 18.0f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private UserViewModel mUserViewModel;
    private LocationViewModel mLocationViewModel;
    private List<Result> restaurantList;
    private List<Restaurant> restaurantListData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configureViewModels();
        restaurantListData = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_mapview, container, false);
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mLocationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        mLocationViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng location) {
                //Mise à jour du marker si l'user change de position
                if (googleMap != null && location != null) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(location).title("My Position"));
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (currentMapPosition != null) {
            googleMap.addMarker(new MarkerOptions().position(currentMapPosition).title("My Position"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMapPosition, 12));


        } else if (hasLocationPermission()) {
            showUserLocation();
            //retrieveNearbyRestaurants();
            NearbySearchRestaurantJson();
            updateMapWithMarkers(restaurantListData);
        } else {
            requestLocationPermission();
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMinZoomPreference(MIN_ZOOM);
        googleMap.setMaxZoomPreference(MAX_ZOOM);
    }

    /*private void retrieveNearbyRestaurants() {

        // Vérifier si currentMapPosition est null avant d'appeler l'API
        if (currentMapPosition != null) {
            PlacesApi placesApi = RetrofitBuilder.buildPlacesApi();

            // Récupérer les coordonnées de l'emplacement actuel
            double latitude = currentMapPosition.latitude;
            double longitude = currentMapPosition.longitude;

            // Définir le rayon de recherche en mètres
            int radius = 200;

            // Définir le type de lieu à rechercher (restaurant dans cet exemple)
            String type = "restaurant";

            // Appeler l'API pour récupérer les données des restaurants à proximité
            Call<ResponseBody> call = placesApi.getNearbyRestaurants(latitude + "," + longitude, radius, type, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String json = response.body().string();
                            Log.d("JSON", json);
                            // Utiliser un parser JSON pour extraire les résultats et les stocker dans la liste
                            restaurantList = parseRestaurantData(json);
                            // Mettre à jour la liste dans ListViewFragment

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Gérer les erreurs de réponse
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Gérer les erreurs de requête
                }
            });
        }
    }*/
    private void NearbySearchRestaurantJson() {
        // Vérifier si currentMapPosition est null avant d'appeler l'API
        if (currentMapPosition != null) {
            PlacesApi placesApi = RetrofitBuilder.buildPlacesApi();

            // Récupérer les coordonnées de l'emplacement actuel
            double latitude = currentMapPosition.latitude;
            double longitude = currentMapPosition.longitude;
            String location = latitude + "," + longitude;

            // Définir le rayon de recherche en mètres
            int radius = 200;

            // Définir le type de lieu à rechercher (restaurant dans cet exemple)
            String type = "restaurant";

            Call<NearbySearchResponse> nearbySearchResponseCall = placesApi.nearbySearch(location, radius, type);
            nearbySearchResponseCall.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                    if (response.isSuccessful()){
                        NearbySearchResponse nearbySearchResponse= response.body();
                        if (nearbySearchResponse!=null){
                            List<Result> resultList = nearbySearchResponse.getResults();

                            // Utiliser RestaurantConverter pour convertir les résultats en objets Restaurant
                            restaurantListData = RestaurantConverter.convertToRestaurantList(nearbySearchResponse);

                            // Afficher les données des restaurants dans le log
                            for (Restaurant restaurant : restaurantListData) {
                                Log.e("GETRESTOLIST", "onResponse: " + restaurant.toString());
                            }

                            // Mettre à jour la carte avec les marqueurs
                            updateMapWithMarkers(restaurantListData);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NearbySearchResponse> call, Throwable t) {

                }
            });
        }
    }
    private void updateMapWithMarkers(List<Restaurant> restaurantList) {
        // Effacer les marqueurs existants de la carte
        googleMap.clear();

        // Parcourir la liste des restaurants
        for (Restaurant restaurant : restaurantList) {
            // Obtenir les informations nécessaires du restaurant
            String name = restaurant.getName();
            double latitude = restaurant.getLatitude();
            double longitude = restaurant.getLongitude();

            // Créer un marqueur pour le restaurant sur la carte
            LatLng restaurantLatLng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(restaurantLatLng)
                    .title(name));
        }
    }


    /*private List<Result> parseRestaurantData(String json) {
        // Utiliser un parser JSON pour extraire les résultats des restaurants à partir du JSON
        // et les stocker dans une liste de type Result (ou tout autre modèle que vous utilisez)
        // et retourner cette liste
        List<Result> resultList = new ArrayList<>();
        // Code pour extraire les résultats du JSON et les ajouter à resultList
        return resultList;
    }*/


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (googleMap != null) {
            outState.putParcelable("map_position", googleMap.getCameraPosition().target);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            showUserLocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleMap != null) {
            googleMap.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleMap != null) {
            googleMap.clear();
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
        // Vérifier les autorisations de localisation
        if (hasLocationPermission()) {
            // Obtenir la dernière localisation de l'utilisateur
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Récupérer les coordonnées de la localisation de l'utilisateur
                        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, MAX_ZOOM));
                        googleMap.addMarker(new MarkerOptions().position(myPosition).title("My Position"));
                        currentMapPosition = myPosition;

                        // Une fois que la localisation de l'utilisateur est disponible,
                        // récupérer les restaurants à proximité
                        //retrieveNearbyRestaurants();
                        NearbySearchRestaurantJson();
                    }
                }
            });
        } else {
            // Si les autorisations de localisation n'ont pas été accordées, demander à l'utilisateur de les accorder
            requestLocationPermission();
        }
    }
    @SuppressLint("MissingPermission")
    private void updateLocationData() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mLocationViewModel.updateLocation(newLocation);
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
            showPermissionDeniedDialog();
        }
    }

    private void logOutAndRedirect() {
        mUserViewModel.logOut();
        // Rediriger vers l'activité LoginActivity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Permission de localisation refusée");
        builder.setMessage("Pour utiliser l'application, nous avons besoin de votre localisation. Voulez-vous activer la localisation maintenant ? Sinon vous serez déconnecté !!!");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestLocationPermission(); // Demander à nouveau la permission de localisation
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOutAndRedirect(); // Déconnecter l'utilisateur et le rediriger vers LoginActivity
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}