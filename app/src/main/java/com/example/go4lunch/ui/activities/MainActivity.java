package com.example.go4lunch.ui.activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Location currentLocation;
    private String userId;
    private String restaurantName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView imvProfilePhoto;
    private String restaurantSelectedIdByUser;
    private List<User> workmateslist;
    private List<Restaurant> restaurantslisteLike;
    private String restaurantId;

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


    }
    private void checkAuth() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // User is logged in, retrieve the user ID
            userId = firebaseUser.getUid();
        } else {
            // User is not logged in, redirect to LoginActivity
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
                    displayUserInfo(user);
                    restaurantSelectedIdByUser = user.getSelectedRestaurantId();
                    observeRestaurantSelectedByUser();
                }
            });
        }
    }

    private void observeRestaurantSelectedByUser() {
        if (restaurantSelectedIdByUser != null) {
            // Get the selected restaurant using restaurantSelectedIdByUser
            restaurantViewModel.getRestaurantById(restaurantSelectedIdByUser);
            restaurantViewModel.getSelectedRestaurantLiveData().observe(this, restaurantSelected -> {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (restaurantSelected != null) {
                    restaurant = restaurantSelected;
                    restaurantName = restaurant.getName();
                    restaurantId = restaurant.getPlaceId();


                    editor.putString("restaurant_name", restaurantName);
                    editor.putString("restaurant_Id", restaurantId);
                    editor.putString("user_id", userId);
                    editor.apply();
                }
                else{
                    editor.putString("restaurant_name", "");
                    editor.putString("restaurant_Id", "");
                    editor.putString("user_id", userId);
                    editor.apply();
                }
            });
        } else {
            restaurant = null;
        }
    }

    private void openYourLunchActivityWithSelectedRestaurant() {
        if (restaurant != null) {
            observeRestaurantSelectedByUser();
            // Check if the selected restaurant's ID matches restaurantSelectedIdByUser
            if (restaurantSelectedIdByUser != null && restaurantSelectedIdByUser.equals(restaurant.getPlaceId())) {
                // Open YourLunchDetailActivity with the selected restaurant
                Intent intent = new Intent(MainActivity.this, YourLunchDetailActivity.class);
                intent.putExtra("restaurant", restaurant);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.main_activity_no_restaurant_selected, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.main_activity_no_restaurant_selected, Toast.LENGTH_SHORT).show();
        }
    }

    private void observeWorkmatesData() {
        userViewModel.getWorkmatesListFromFirestore(true);
        userViewModel.getUserListLiveData().observe(this, userList -> workmateslist = userList);
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

    // Configuration of bottom navigation bar items
    @SuppressLint("NonConstantResourceId")
    private void configureBottomNavItem() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_mapview:
                    fragment = mapViewFragment;
                    break;
                case R.id.nav_listview:
                    // When the user clicks on "List view", display the ListViewFragment
                    if (listViewFragment == null) {
                        listViewFragment = new ListViewFragment();
                    }
                    fragment = listViewFragment;
                    break;
                case R.id.nav_workmates:
                    // When the user clicks on "Workmates", display the WorkmatesFragment
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
        // If user data is not null, display it in the corresponding TextViews
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
                .setPositiveButton(R.string.logout_positive_button, (dialog, which) -> logOutAndRedirect())
                .setNegativeButton(R.string.logout_negative_button, null)
                .show();
    }

    // PERMISSION

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
            // User dedied permission = logout
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

    // LOCATION

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
               LikesCounter.updateLikesCount(restaurantslisteLike, workmateslist);
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
                // Do nothing here as we want to handle real-time search
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
                return true;
            }
        });
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        observeUserData();
        Log.d("CYCLEDEVIE", "on Resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CYCLEDEVIE", "on Pause");
    }


    @Override
    public void onStop() {
        super.onStop();

        Log.d("CYCLEDEVIE", "on Stope");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userViewModel.logOut();
        userViewModel.getUserLiveData().removeObservers(this);
        locationPermissionViewModel.getCurrentLocation().removeObservers(this);
        //locationPermissionViewModel.stopUpdateLocation();
    }
}
