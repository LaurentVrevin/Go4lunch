package com.example.go4lunch.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class YourLunchActivity extends AppCompatActivity {

    private ImageView placeImageView;
    private TextView placeNameTextView;
    private TextView placeAddressTextView;
    private boolean isRestaurantSelected = false;
    private FloatingActionButton fabDetailChoice;
    private RatingBar ratingBar;
    private User currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    private Button buttonRestaurantLike;

    private String restaurantId;
    private UserViewModel userViewModel;
    private LiveData<User> userLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_lunch);

        placeImageView = findViewById(R.id.im_detail_place);
        placeNameTextView = findViewById(R.id.tv_detail_restaurant_name);
        placeAddressTextView = findViewById(R.id.tv_detail_restaurant_address);
        fabDetailChoice = findViewById(R.id.fab_detail_choice);
        ratingBar = findViewById(R.id.rb_detail_restaurant_rate);
        buttonRestaurantLike = findViewById(R.id.bt_detail_restaurant_like);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        configureViewModel();
        observeUserData();


        // Récupérer les données du restaurant transmises depuis l'adapter
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("restaurant")) {
            Restaurant restaurant = intent.getParcelableExtra("restaurant");


            // Utiliser les données du restaurant pour afficher les détails appropriés
            placeNameTextView.setText(restaurant.getName());
            placeAddressTextView.setText(restaurant.getAddress());
            ratingBar.setRating(restaurant.getRating());

            loadRestaurantImage(restaurant);

            // Stocker l'ID du restaurant dans la variable restaurantId
            restaurantId = restaurant.getPlaceId();

            }

        fabDetailChoice.setOnClickListener(view -> {
            // Inverser l'état du bouton
            updateUserSelectedRestaurant();
        });

        buttonRestaurantLike.setOnClickListener(view ->{
            updateUserLikedPlace();
        });

        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void configureViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUserFromFirestore(mAuth.getCurrentUser().getUid());
        userViewModel.getUserLiveData().observe(this, user -> {
            currentUser = user;
            userId = currentUser.getUserId();
            Log.d("yourlunchuserid", "user id is : " + userId);
        });
        observeUserData();
    }

    private void observeUserData() {
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                String selectedRestaurantId = user.getSelectedRestaurantId();
                if (selectedRestaurantId != null && selectedRestaurantId.equals(restaurantId)) {
                    setButtonColor(true); // Mettre le bouton en bleu
                } else {
                    setButtonColor(false); // Mettre le bouton en blanc
                }
            }
        });
    }

    private void setButtonColor(boolean isSelected) {
        if (isSelected) {
            fabDetailChoice.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue)));
        } else {
            fabDetailChoice.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        }
    }

    private void updateUserSelectedRestaurant() {
        if (currentUser != null) {
            if (currentUser.getSelectedRestaurantId() == null) {
                currentUser.setSelectedRestaurantId(restaurantId);
                setButtonColor(true);
            } else {
                currentUser.setSelectedRestaurantId(null);
                setButtonColor(false);
            }
            userViewModel.updateUserSelectedRestaurant(userId, currentUser);
        }
    }
    private void updateUserLikedPlace() {
        if (currentUser != null && restaurantId != null) {
            List<String> likedPlaces = currentUser.getLikedPlaces();
            if (likedPlaces.contains(restaurantId)) {
                // Le restaurant est déjà aimé, je lui affiche que c'est déjà liké
                Toast.makeText(this, "Ce restaurant a déjà été liké", Toast.LENGTH_SHORT).show();
            } else {
                likedPlaces.add(restaurantId);
                currentUser.setLikedPlaces(likedPlaces);
                userViewModel.updateUserLikedPlaces(currentUser.getUserId(), likedPlaces);
            }
        }
    }

    private void loadRestaurantImage(Restaurant restaurant) {
        List<String> photoUrls = restaurant.getPhotoUrls();
        if (photoUrls != null && !photoUrls.isEmpty()) {
            String photoUrl = photoUrls.get(0); // Utilise le premier URL de photo

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.lunch)
                    .override(placeImageView.getWidth(), placeImageView.getWidth()) // Ajuste la taille de l'image
                    .centerCrop(); // Effectue un recadrage centré pour obtenir un format carré

            Glide.with(this)
                    .load(photoUrl)
                    .apply(requestOptions)
                    .into(placeImageView);
        } else {
            // Si l'URL de l'image est nulle, affiche une image standard
            placeImageView.setImageResource(R.drawable.lunch);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Effectue l'action de retour arrière
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
