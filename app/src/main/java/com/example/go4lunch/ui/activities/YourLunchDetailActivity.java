package com.example.go4lunch.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.MyApplication;
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



    // Initialisation
    private void initializeViews() {
        placeImageView = findViewById(R.id.im_detail_place);
        placeNameTextView = findViewById(R.id.tv_detail_restaurant_name);
        placeAddressTextView = findViewById(R.id.tv_detail_restaurant_address);
        fabDetailChoice = findViewById(R.id.fab_detail_choice);
        numberofNote = findViewById(R.id.tv_note);
        ratingBar = findViewById(R.id.rb_detail_restaurant_rate);
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
            //selectedRestaurantId = currentUser.getSelectedRestaurantId();
        });
    }

    // Barre d'outils
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    // Actions des boutons
    private void setupButtonActions() {
        fabDetailChoice.setOnClickListener(view -> updateUserSelectedRestaurant());
        buttonRestaurantLike.setOnClickListener(view -> updateUserLikedPlace());
        buttonRestaurantWebsite.setOnClickListener(view -> openRestaurantWebsite());
    }

    private void openRestaurantWebsite() {
        // Ouvrir le site web du restaurant dans le navigateur par défaut
        String websiteUrl = Objects.requireNonNull(restaurantViewModel.getSelectedRestaurantLiveData().getValue()).getWebsiteUrl();
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        startActivity(webIntent);
    }

    private void setSelectionRestaurantButtonColor(boolean isRestaurantSelected) {
        int color = isRestaurantSelected ? R.color.green : R.color.black;
        int tintColor = ContextCompat.getColor(this, color);
        fabDetailChoice.getDrawable().setTintList(ColorStateList.valueOf(tintColor));
    }
    private void updateStarButtonColor(boolean isLiked) {
        int textColorId = isLiked ? R.color.yellow : R.color.orange;

        buttonRestaurantLike.setTextColor(ContextCompat.getColor(this, textColorId));
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
                // Filtrer et obtenir la liste des collègues ayant choisi le même restaurant
                List<User> filteredList = new ArrayList<>();
                for (User user : workmatesList) {
                        if (!user.getUserId().equals(currentUser.getUserId())
                                && user.getSelectedRestaurantId() != null
                                && user.getSelectedRestaurantId().equals(restaurantId)) {
                        filteredList.add(user);
                    }
                }
                workmatesListViewAdapter.setWorkmatesList(filteredList);

            }
        });
    }

    private void observeSelectedRestaurantByUser() {
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                selectedRestaurantId = user.getSelectedRestaurantId();
                Log.d("USERAUTH", "YourLunchActivity, observeSelectedRestaurantByUser : " + user.getName() + " " + user.getSelectedRestaurantId());
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
        if (intent != null && intent.hasExtra("restaurant")) {
            restaurant = intent.getParcelableExtra("restaurant");
            Log.d("USERAUTH", "getIntentData : " + restaurant.getName());
            restaurantId = restaurant.getPlaceId();
            restaurantViewModel.getRestaurantById(restaurantId);
            int likesCount = restaurant.getLikesCount();
            float ratingCount = restaurant.getRating();
            // Obtenir la liste des restaurants likés par l'utilisateur

            restaurantViewModel.getSelectedRestaurantLiveData().observe(this, observeRestaurant -> {
                if (observeRestaurant != null) {
                    Log.d("USERAUTH", "getIntentData + getSelectedRestaurantLiveData : " + restaurant.getName());
                    placeNameTextView.setText(restaurant.getName());
                    placeAddressTextView.setText(restaurant.getAddress());
                    ratingBar.setRating(ratingCount);
                    loadRestaurantImage(restaurant);
                    numberofNote.setText(String.valueOf(likesCount) + "/" + workmatesList.size());
                    Log.d("LIKES_COUNTER", "Nombre de like : " + restaurant.getLikesCount() + " " + likesCount + " " + restaurant.getName());

                }
            });
        }
    }

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

                Toast.makeText(this, "Vous avez enlevé le like de ce restaurant", Toast.LENGTH_SHORT).show();
                //updateStarButtonColor(false);
            } else {
                likedPlaces.add(restaurantId);
                currentUser.setLikedPlaces(likedPlaces);
                userViewModel.updateUserLikedPlaces(currentUser.getUserId(), likedPlaces);

                Toast.makeText(this, "Vous avez liké ce restaurant", Toast.LENGTH_SHORT).show();
                //updateStarButtonColor(true);
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
