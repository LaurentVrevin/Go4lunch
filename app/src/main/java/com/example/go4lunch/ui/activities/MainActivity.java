package com.example.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.fragments.listview.ListViewFragment;
import com.example.go4lunch.ui.fragments.mapview.MapViewFragment;
import com.example.go4lunch.ui.fragments.workmates.WorkmatesFragment;
import com.example.go4lunch.ui.manager.UserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private MapViewFragment mMapViewFragment;
    private ListViewFragment mListViewFragment;
    private WorkmatesFragment mWorkmatesFragment;
    private ActivityMainBinding binding;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager();

        setupToolbar();
        setupNavigationDrawer();
        setupBottomNavigation();
        setupHeaderView();
        setupNavController();
        setupMapViewFragment();
        setupNavigationListener();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.appBarMain.toolbar);
    }

    private void setupNavigationDrawer() {
        drawer = binding.drawerLayout;
        navigationView = binding.navViewDrawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        displayUserInfo();
    }

    private void setupNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();
    }

    private void setupMapViewFragment() {
        mMapViewFragment = new MapViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, mMapViewFragment)
                .commit();
    }

    private void setupNavigationListener() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_yourlunch:
                    intent = new Intent(MainActivity.this, YourLunchActivity.class);
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

    private void displayUserInfo() {
        String firstName = userManager.getFirstName();
        String lastName = userManager.getLastName();
        String email = userManager.getEmail();

        if (tvUserName != null) {
            tvUserName.setText(firstName + " " + lastName);
        }
        if (tvUserEmail != null) {
            tvUserEmail.setText(email);
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.logout_positive_button, (dialog, which) -> {
                    userManager.signOut(this).addOnSuccessListener(aVoid ->{

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                })
                .setNegativeButton(R.string.logout_negative_button, null)
                .show();
    }

    // Configuration des éléments de la barre de navigation inférieure
    private void configureBottomNavItem() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_mapview:
                    // Lorsque l'utilisateur clique sur l'élément "Map view", on affiche le fragment MapViewFragment
                    if (mMapViewFragment == null) {
                        mMapViewFragment = new MapViewFragment();
                    }
                    fragment = mMapViewFragment;
                    break;
                case R.id.nav_listview:
                    // Lorsque l'utilisateur clique sur l'élément "List view", on affiche le fragment ListViewFragment
                    if (mListViewFragment == null) {
                        mListViewFragment = new ListViewFragment();
                    }
                    fragment = mListViewFragment;
                    break;
                case R.id.nav_workmates:
                    // Lorsque l'utilisateur clique sur l'élément "Workmates", on affiche le fragment WorkmatesFragment
                    if (mWorkmatesFragment == null) {
                        mWorkmatesFragment = new WorkmatesFragment();
                    }
                    fragment = mWorkmatesFragment;
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

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}