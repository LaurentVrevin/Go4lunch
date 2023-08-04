package com.example.go4lunch.ui.activities;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.fragments.ListViewFragment;
import com.example.go4lunch.ui.fragments.MapViewFragment;
import com.example.go4lunch.ui.fragments.WorkmatesFragment;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import pub.devrel.easypermissions.EasyPermissions;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private MapViewFragment mapViewFragment;
    private ListViewFragment listViewFragment;
    private WorkmatesFragment workmatesFragment;
    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private LocationPermissionViewModel locationPermissionViewModel;
    private RestaurantViewModel restaurantViewModel;
    private Restaurant restaurantMainActivity;
    private User user;
    private Location currentLocation;
    private String userId;
    private FirebaseAuth mAuth;
    private ImageView imvProfilePhoto;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private String restaurantSelectedIdByUser;
    private int radius = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        configureViewModels();
        checkAuth();
        observeUserData();
        setupToolbar();
        setupNavigationDrawer();
        setupBottomNavigation();
        setupHeaderView();
        setupNavController();
        setupMapViewFragment();
        setupNavigationListener();
        observePermission();
    }

    //VIEW

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void setupNavigationDrawer() {
        drawer = binding.drawerLayout;
        navigationView = binding.navViewDrawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = binding.bottomNavView;
        configureBottomNavItem();
    }

    private void setupHeaderView() {
        View headerView = navigationView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.tv_header_profilename);
        tvUserEmail = headerView.findViewById(R.id.tv_header_email);
        imvProfilePhoto = headerView.findViewById(R.id.iv_header_Avatar);
        userId = mAuth.getCurrentUser().getUid();

        userViewModel.getCurrentUserFromFirestore(userId);
        userViewModel.getUserLiveData().observe(this, this::displayUserInfo);

    }

    private void setupNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();
    }

    private void setupMapViewFragment() {
        mapViewFragment = new MapViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, mapViewFragment)
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    private void setupNavigationListener() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_yourlunch:
                    openYourLunchActivityWithSelectedRestaurant();
                    break;
                case R.id.nav_settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    break;
                case R.id.nav_logout:
                    showLogoutConfirmationDialog();
                    break;
            }
            if (intent != null) {
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });
    }

    // Configuration des éléments de la barre de navigation inférieure
    private void configureBottomNavItem() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_mapview:
                    fragment = mapViewFragment;
                    break;
                case R.id.nav_listview:
                    // Lorsque l'utilisateur clique sur l'élément "List view", on affiche le fragment ListViewFragment
                    if (listViewFragment == null) {
                        listViewFragment = new ListViewFragment();
                    }
                    fragment = listViewFragment;
                    break;
                case R.id.nav_workmates:
                    // Lorsque l'utilisateur clique sur l'élément "Workmates", on affiche le fragment WorkmatesFragment
                    if (workmatesFragment == null) {
                        workmatesFragment = new WorkmatesFragment();
                    }
                    fragment = workmatesFragment;
                    break;
            }
            if (fragment != null && !fragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
                return true;
            } else {
                return false;
            }
        });
    }


    //USER

    private void checkAuth() {
        if (mAuth.getCurrentUser() != null) {
            // Utilisateur connecté, récupérer l'ID de l'utilisateur
            userId = mAuth.getCurrentUser().getUid();
            Log.d("USERAUTH", "L'id de l'utilisateur est : " + userId);
        } else {
            // Utilisateur non connecté, rediriger vers LoginActivity
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void displayUserInfo(User user) {
        Log.d("MainActivity", "displayUserInfo called");
        // Si les données de l'utilisateur ne sont pas nulles, les affiche dans les TextView correspondants
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            String profilePictureUrl = user.getPictureUrl();
            Log.d("USERIDLIVEDATA", "l'id de l'utilisateur via le livedata est : " + user.getUserId());
            if (user.getPictureUrl() != null) {
                setProfilePicture(profilePictureUrl);
            }
        }
    }

    private void setProfilePicture(String profilePicture) {
        if (profilePicture != null && imvProfilePhoto != null) {
            Glide.with(this)
                    .load(profilePicture)
                    .circleCrop()
                    .into(imvProfilePhoto);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // YOUR LUNCH ACTIVITY

    private void configureViewModels() {
        locationPermissionViewModel = new ViewModelProvider(this).get(LocationPermissionViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    private void observeUserData() {
        userViewModel.getCurrentUserFromFirestore(userId);
        userViewModel.getUserLiveData().observe(this, user -> {
            displayUserInfo(user);
            restaurantSelectedIdByUser = user.getSelectedRestaurantId();
            if (restaurantSelectedIdByUser != null) {
                // Récupérez le restaurant sélectionné en utilisant restaurantSelectedIdByUser
                restaurantViewModel.getRestaurantById(restaurantSelectedIdByUser);
                restaurantViewModel.getSelectedRestaurantLiveData().observe(this, restaurant ->{
                    if(restaurant!=null){
                        restaurantMainActivity = restaurant;
                        Log.d("TESTRESTO", restaurantMainActivity.getName());
                    }

                });
            } else {
                restaurantMainActivity = null;
                Log.d("TESTRESTO", "NULL");
            }

        });

    }

    private void openYourLunchActivityWithSelectedRestaurant() {

            if (restaurantMainActivity != null) {
                // Ouvrir YourLunchDetailActivity avec le restaurant sélectionné
                Intent intent = new Intent(MainActivity.this, YourLunchDetailActivity.class);
                intent.putExtra("restaurant", restaurantMainActivity);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Vous n'avez pas choisi de restaurant", Toast.LENGTH_SHORT).show();
            }

    }


    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.logout_positive_button, (dialog, which) -> {
                    logOutAndRedirect();
                })
                .setNegativeButton(R.string.logout_negative_button, null)
                .show();
    }
    //PERMISSION

    private void checkPermission(Boolean hasPermission) {
        if (hasPermission) {
            locationPermissionViewModel.permissionSet(true);
            observeLocation();
        } else {
            locationPermissionViewModel.permissionSet(false);
            requestPermission();
        }
    }

    public void requestPermission() {

        EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_rationale_location),
                LOCATION_PERMISSION_REQUEST_CODE,
                ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );
    }

    private void observePermission() {
        locationPermissionViewModel.liveDataHasPermission(this).observe(this, this::checkPermission);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        locationPermissionViewModel.permissionSet(true);
        observeLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        logOutAndRedirect();
    }

    public void logOutAndRedirect() {
        userViewModel.logOut();
        // Rediriger vers l'activité LoginActivity
        redirectToLogin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //LOCATION

    private void observeLocation() {
        locationPermissionViewModel.startUpdateLocation(this, this);
        locationPermissionViewModel.getCurrentLocation().observe(this, this::updateLocation);
    }

    private void updateLocation(Location location) {
        currentLocation = location;
        if (currentLocation != null) {
            restaurantViewModel.getRestaurants(currentLocation);
            // Mettre à jour votre liste de restaurants avec restaurantList
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //configureViewModels();
        observeUserData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        locationPermissionViewModel.stopUpdateLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
