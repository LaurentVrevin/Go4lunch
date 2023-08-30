package com.example.go4lunch.ui.activities;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.go4lunch.utils.LikesCounter;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import pub.devrel.easypermissions.EasyPermissions;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
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
    private Restaurant restaurant;
    private User currentUser;
    private Location currentLocation;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView imvProfilePhoto;

    private String restaurantSelectedIdByUser;
    private List<User> workmateslist;
    private List<Restaurant> restaurantslisteLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkAuth();
        configureViewModels();
        observeUserData();
        observeWorkmatesData();
        setupToolbar();
        setupNavigationDrawer();
        setupBottomNavigation();
        setupHeaderView();
        setupNavController();
        setupMapViewFragment();
        setupNavigationListener();
        observePermission();
        observeRestaurantsData();
        Log.d("USERAUTH", "onCreate" + restaurantSelectedIdByUser);

    }

    private void checkAuth() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Utilisateur connecté, récupérer l'ID de l'utilisateur
            userId = firebaseUser.getUid();

            Log.d("USERAUTH", "checkAuth / firebaseUser : " + firebaseUser.getDisplayName() +" " + firebaseUser.getUid() + " "
                    + " firebaseAuth : " + firebaseAuth.getCurrentUser().getDisplayName());

        } else {
            // Utilisateur non connecté, rediriger vers LoginActivity
            redirectToLogin();
        }
    }

    private void configureViewModels() {
        locationPermissionViewModel = new ViewModelProvider(this).get(LocationPermissionViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

    }

    private void observeUserData() {
        if (userId != null) {
            userViewModel.getCurrentUserFromFirestore(userId);
            userViewModel.getUserLiveData().observe(this, user -> {
                if (user != null && user.getUserId().equals(userId)) {
                    currentUser = user;
                    displayUserInfo(user);
                    restaurantSelectedIdByUser = user.getSelectedRestaurantId();
                    observeRestaurantSelectedByUser();
                }
            });
        }
    }

    private void observeRestaurantSelectedByUser() {
        if (restaurantSelectedIdByUser != null) {

            // Récupérez le restaurant sélectionné en utilisant restaurantSelectedIdByUser
            restaurantViewModel.getRestaurantById(restaurantSelectedIdByUser);
            restaurantViewModel.getSelectedRestaurantLiveData().observe(this, restaurantSelected -> {
                if (restaurantSelected != null) {
                    restaurant = restaurantSelected;
                    Log.d("USERAUTH", "observeRestaurantSelectedByUser " + restaurantSelectedIdByUser + " objet restaurant :" + restaurant );
                    // Maintenant que les données du restaurant sont disponibles, ouvrez les détails du restaurant

                }
            });
        } else {
            restaurant = null;
        }
    }

    private void openYourLunchActivityWithSelectedRestaurant() {
        if (restaurant != null) {
            observeRestaurantSelectedByUser();

            // Vérifier si l'ID du restaurant sélectionné correspond à restaurantSelectedIdByUser
            if (restaurantSelectedIdByUser != null && restaurantSelectedIdByUser.equals(restaurant.getPlaceId())) {
                // Ouvrir YourLunchDetailActivity avec le restaurant sélectionné
                Intent intent = new Intent(MainActivity.this, YourLunchDetailActivity.class);
                intent.putExtra("restaurant", restaurant);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Vous n'avez pas choisi ce restaurant", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vous n'avez pas choisi de restaurant", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeWorkmatesData() {
        userViewModel.getWorkmatesListFromFirestore(false);
        userViewModel.getUserListLiveData().observe(this, userList -> {
            workmateslist = userList;
        });
    }

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

    @SuppressLint("SetTextI18n")
    private void displayUserInfo(User user) {
        // Si les données de l'utilisateur ne sont pas nulles, les affiche dans les TextView correspondants
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            String profilePictureUrl = user.getPictureUrl();
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
            observeRestaurantsData();
        }
    }

    private void observeRestaurantsData() {
        if (currentLocation != null) {
            restaurantViewModel.getRestaurants(currentLocation);
            restaurantViewModel.getListRestaurantLiveData().observe(this, restaurantListData -> {
                restaurantslisteLike = restaurantListData;
                /*for(Restaurant restaurant : restaurantListData){
                    Log.d("MAINACTIVITYLOCATION", restaurant.getName() + " " + restaurant.getPhone());
                }*/

                LikesCounter.updateLikesCount(restaurantListData, workmateslist);
            });
        }
    }

    private void redirectToLogin() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_right);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Ne rien faire ici, car nous voulons gérer la recherche en temps réel
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mapViewFragment != null) {
                    mapViewFragment.filterMarkers(newText);
                }
                if (listViewFragment != null) {
                    listViewFragment.filterRestaurantList(newText);
                }
                /*if (workmatesFragment != null) {
                    workmatesFragment.filterWorkmatesList(newText);
                }*/
                return true;
            }
        });

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        observeUserData();

        Log.d("USERAUTH", "onResume : MainActivity ");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("USERAUTH", "onPause : MainActivity ");
    }


    @Override
    public void onStop() {
        super.onStop();
        locationPermissionViewModel.stopUpdateLocation();

        Log.d("USERAUTH", "onStop : mainActivity ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userViewModel.logOut();
        userViewModel.getUserLiveData().removeObservers(this);

        Log.d("USERAUTH", "onDestroy : mainActivity " +  firebaseAuth.getCurrentUser());

    }


}
