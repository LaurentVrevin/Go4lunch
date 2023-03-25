package com.example.go4lunch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    // déclaration des variables
    private AppBarConfiguration mAppBarConfiguration; // pour la configuration de la barre d'app
    private ActivityMainBinding binding; // pour lier l'activité à son layout
    private NavController navController;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // on utilise ActivityMainBinding pour lier l'activité à son layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // on configure la toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // on récupère le DrawerLayout et le NavigationView
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        /*viewPager = findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager()) {};
        viewPager.setAdapter(adapter);*/

        //fragmentManager = getSupportFragmentManager();
        // on récupère le NavController et on le configure avec la AppBarConfiguration
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // on configure la AppBarConfiguration avec les menus à afficher en tant que destinations principales
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_mapview, R.id.nav_listview, R.id.nav_workmates)
                .setDrawerLayout(drawer)
                .build();


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // on configure le NavigationView pour qu'il utilise le NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Utilisez le NavController pour afficher le fragment MapViewFragment
        //navController.navigate(R.id.nav_mapview);


        }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // on ajoute les items du menu à la barre d'app si présents
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
