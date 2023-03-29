package com.example.go4lunch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.SettingsActivity;
import com.example.go4lunch.ui.YourLunchActivity;
import com.example.go4lunch.ui.listview.ListViewFragment;
import com.example.go4lunch.ui.mapview.MapViewFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MapViewFragment mMapViewFragment;
    private ListViewFragment mListViewFragment;
    private WorkmatesFragment mWorkmatesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navViewDrawer;
        BottomNavigationView bottomNavigationView = binding.bottomNavView;
        configureBottomNavItem();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // gérer le clique sur yourlunch et sur settings pour ouvrir leurs activités respectives
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.nav_yourlunch:
                        intent = new Intent(MainActivity.this, YourLunchActivity.class);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void configureBottomNavItem(){
        binding.bottomNavView.setOnNavigationItemSelectedListener(this::selectBottomNavItem);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean selectBottomNavItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_mapview:
                if (mMapViewFragment == null) {
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if (!mMapViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, mMapViewFragment).commit();
                }
                return true;
            case R.id.nav_listview:
                if (mListViewFragment == null) {
                    this.mListViewFragment = ListViewFragment.newInstance();
                }
                if (!mListViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, mListViewFragment).addToBackStack(null).commit();

                }
                return true;
            case R.id.nav_workmates:
                if (mWorkmatesFragment == null) {
                    this.mWorkmatesFragment = WorkmatesFragment.newInstance();
                }
                if (!mWorkmatesFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, mWorkmatesFragment).disallowAddToBackStack().commit();
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


}
