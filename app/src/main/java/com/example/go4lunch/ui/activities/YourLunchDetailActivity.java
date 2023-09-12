package com.example.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.adapters.YourLunchDetailWorkmatesAdapter;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class YourLunchDetailActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 0631;

    private ImageView placeImageView;
    private TextView placeNameTextView;
    private TextView placeAddressTextView;
    private TextView numberofNote;

    private FloatingActionButton fabDetailChoice;
    private RatingBar ratingBar;
    private User currentUser;
    private Restaurant restaurant;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;

    private Button getButtonRestaurantCall;
    private Button buttonRestaurantLike;
    private Button buttonRestaurantWebsite;
    private String restaurantId;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;

    private LiveData<User> userLiveData;
    private List<User> workmatesList;
    private RecyclerView workmatesRecyclerView;
    private YourLunchDetailWorkmatesAdapter workmatesListViewAdapter;
    private String selectedRestaurantId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_lunch);

        initializeViews();
        setupFirebaseAuth();
        configureViewModel();
        observeSelectedRestaurantByUser();
        getIntentData();
        setupButtonActions();
        configureRecyclerView();
        observeWorkmateList();
        setupToolbar();

    }

    // Initialization
    private void initializeViews() {
        placeImageView = findViewById(R.id.im_detail_place);
        placeNameTextView = findViewById(R.id.tv_detail_restaurant_name);
        placeAddressTextView = findViewById(R.id.tv_detail_restaurant_address);
        fabDetailChoice = findViewById(R.id.fab_detail_choice);
        ratingBar = findViewById(R.id.rb_detail_restaurant_rate);
        getButtonRestaurantCall = findViewById(R.id.bt_detail_restaurant_call);
        buttonRestaurantLike = findViewById(R.id.bt_detail_restaurant_like);
        buttonRestaurantWebsite = findViewById(R.id.bt_detail_restaurant_website);
        workmatesRecyclerView = findViewById(R.id.rv_detail_restaurant_workmates);
    }

    // Firebase
    private void setupFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    // ViewModels
    private void configureViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        userViewModel.getUserLiveData().observe(this, user -> {
            currentUser = user;
            userId = currentUser.getUserId();
        });
    }

    // Toolbar
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    // Button Actions
    private void setupButtonActions() {
        fabDetailChoice.setOnClickListener(view -> updateUserSelectedRestaurant());
        buttonRestaurantLike.setOnClickListener(view -> updateUserLikedPlace());
        buttonRestaurantWebsite.setOnClickListener(view -> openRestaurantWebsite());
        getButtonRestaurantCall.setOnClickListener(view -> callRestaurantPhoneNumber());
    }

    private void openRestaurantWebsite() {
        // Open the restaurant's website in the default browser
        String websiteUrl = Objects.requireNonNull(restaurantViewModel.getSelectedRestaurantLiveData().getValue()).getWebsiteUrl();
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        startActivity(webIntent);
    }

    private void callRestaurantPhoneNumber() {
        String phoneNumber = restaurant.getPhone();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Create an intent for the call action
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            // Check if the app has CALL_PHONE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                // Request call permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
            }
        } else {
            // The restaurant doesn't have a phone number
            Toast.makeText(this, R.string.your_lunch_detail_no_phone_number, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Call permission granted
                callRestaurantPhoneNumber();
            } else {
                // Call permission denied
                Toast.makeText(this, R.string.your_lunch_detail_call_permission_needed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setSelectionRestaurantButtonColor(boolean isRestaurantSelected) {
        int color = isRestaurantSelected ? R.color.green : R.color.black;
        int tintColor = ContextCompat.getColor(this, color);
        fabDetailChoice.getDrawable().setTintList(ColorStateList.valueOf(tintColor));
    }

    private void configureRecyclerView() {
        workmatesListViewAdapter = new YourLunchDetailWorkmatesAdapter(new ArrayList<>());
        workmatesRecyclerView.setAdapter(workmatesListViewAdapter);
        workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Observations
    private void observeWorkmateList(){
        userViewModel.getUserListLiveData().observe(this, userList -> {
            if (userList != null) {
                this.workmatesList = userList;
                // Filter and get the list of workmates who selected the same restaurant
                List<User> filteredList = new ArrayList<>();
                for (User user : workmatesList) {
                    if (!user.getUserId().equals(currentUser.getUserId())
                            && user.getSelectedRestaurantId() != null
                            && user.getSelectedRestaurantId().equals(restaurantId)) {
                        filteredList.add(user);
                    }
                }
                workmatesListViewAdapter.setWorkmatesList(filteredList);
                // Sauvegardez la liste filtrée dans SharedPreferences
                List<String> workmateNamesList = new ArrayList<>();
                for (User user : filteredList) {
                    workmateNamesList.add(user.getName());
                }
                // Convert the list of names to a single string with a separator
                String workmateNames = TextUtils.join(", ", workmateNamesList);
                // Save the string to SharedPreferences
                saveWorkmateNames(workmateNames);
            }
        });
    }
    // Save the workmate names to SharedPreferences
    private void saveWorkmateNames(String workmateNames) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workmate_names", workmateNames);
        editor.apply();
    }


    private void observeSelectedRestaurantByUser() {
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                selectedRestaurantId = user.getSelectedRestaurantId();
                if (selectedRestaurantId != null && selectedRestaurantId.equals(restaurantId)) {
                    setSelectionRestaurantButtonColor(true);
                } else {
                    setSelectionRestaurantButtonColor(false);
                }
            }
        });
    }

    // Intent
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("restaurant")) {
                restaurant = intent.getParcelableExtra("restaurant");
                restaurantId = restaurant.getPlaceId();
                restaurantViewModel.getRestaurantById(restaurantId);
                float ratingCount = restaurant.getRating();

                restaurantViewModel.getSelectedRestaurantLiveData().observe(this, observeRestaurant -> {
                    if (observeRestaurant != null) {
                        placeNameTextView.setText(restaurant.getName());
                        placeAddressTextView.setText(restaurant.getAddress());
                        ratingBar.setRating(ratingCount);
                        loadRestaurantImage(restaurant);
                    }
                });
            }/*
            else if (intent.hasExtra("restaurantId")) {
                // Cas où la clé est "restaurantId"
                restaurantId = intent.getStringExtra("restaurantId");
                // Utilisez restaurantId pour obtenir les données du restaurant depuis votre ViewModel
                restaurantViewModel.getRestaurantById(restaurantId);
                restaurant = restaurantViewModel.getSelectedRestaurantLiveData().getValue();
                //float ratingCount = restaurant.getRating();

                restaurantViewModel.getSelectedRestaurantLiveData().observe(this, observeRestaurant -> {
                    if (observeRestaurant != null) {
                        placeNameTextView.setText(observeRestaurant.getName());
                        placeAddressTextView.setText(observeRestaurant.getAddress());
                        //ratingBar.setRating(ratingCount);
                        loadRestaurantImage(observeRestaurant);
                    }
                });
            }*/

        }
    }

    /** pour charger
     * SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
     * SharedPreferences.Editor editor = sharedPreferences.edit();
     * editor.putString("restaurant_name", restaurantName);
     * editor.apply();
     */

    /** pour récupérer
     * SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
     * String restaurantName = sharedPreferences.getString("restaurant_name", "");
     */

    // Updates
    private void updateUserSelectedRestaurant() {
        if (currentUser != null) {
            boolean isRestaurantSelected = currentUser.getSelectedRestaurantId() != null
                    && currentUser.getSelectedRestaurantId().equals(restaurantId);

            currentUser.setSelectedRestaurantId(isRestaurantSelected ? null : restaurantId);
            setSelectionRestaurantButtonColor(!isRestaurantSelected);
            userViewModel.updateUserSelectedRestaurant(userId, currentUser);
        }
    }

    private void updateUserLikedPlace() {
        if (currentUser != null && restaurantId != null) {
            List<String> likedPlaces = currentUser.getLikedPlaces();

            if (likedPlaces.contains(restaurantId)) {
                likedPlaces.remove(restaurantId);
                currentUser.setLikedPlaces(likedPlaces);
                userViewModel.updateUserLikedPlaces(currentUser.getUserId(), likedPlaces);

                Toast.makeText(this, R.string.your_lunch_detail_you_disliked, Toast.LENGTH_SHORT).show();
            } else {
                likedPlaces.add(restaurantId);
                currentUser.setLikedPlaces(likedPlaces);
                userViewModel.updateUserLikedPlaces(currentUser.getUserId(), likedPlaces);

                Toast.makeText(this, R.string.your_lunch_detail_you_liked, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Images
    private void loadRestaurantImage(Restaurant restaurant) {
        List<String> photoUrls = restaurant.getPhotoUrls();
        if (photoUrls != null && !photoUrls.isEmpty()) {
            String photoUrl = photoUrls.get(0);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.lunch)
                    .override(placeImageView.getWidth(), placeImageView.getWidth())
                    .centerCrop();

            Glide.with(this)
                    .load(photoUrl)
                    .apply(requestOptions)
                    .into(placeImageView);
        } else {
            placeImageView.setImageResource(R.drawable.lunch);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
