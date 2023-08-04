package com.example.go4lunch.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.adapters.YourLunchDetailWorkmatesAdapter;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class YourLunchDetailActivity extends AppCompatActivity {

    private ImageView placeImageView;
    private TextView placeNameTextView;
    private TextView placeAddressTextView;
    private boolean isRestaurantSelected;
    private FloatingActionButton fabDetailChoice;
    private RatingBar ratingBar;
    private User currentUser;
    private Restaurant restaurant;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    private Button buttonRestaurantLike;
    private Button buttonRestaurantWebsite;

    private String restaurantId;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;

    private LiveData<User> userLiveData;
    private List<User> userList;
    private RecyclerView workmatesRecyclerView;
    private YourLunchDetailWorkmatesAdapter workmatesListViewAdapter;


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
        buttonRestaurantWebsite = findViewById(R.id.bt_detail_restaurant_website);
        workmatesRecyclerView = findViewById(R.id.rv_detail_restaurant_workmates);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        configureViewModel();
        observeUserData();
        configureRecyclerView();

        // Récupérer les données du restaurant transmises depuis l'adapter
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("restaurant")) {
            restaurant = intent.getParcelableExtra("restaurant");
            //je recupere bien le restaurant via l'intent
            // Stocker l'ID du restaurant dans la variable restaurantId
            restaurantId = restaurant.getPlaceId();
            // Appeler la méthode getRestaurantById() du restaurantViewModel
            restaurantViewModel.getRestaurantById(restaurantId);

            // Observer le LiveData selectedRestaurantLiveData du restaurantViewModel
            restaurantViewModel.getSelectedRestaurantLiveData().observe(this, restaurant -> {
                // Vérifier si le restaurant n'est pas null
                if (restaurant != null) {
                    // Mettre à jour les vues avec les détails du restaurant
                    placeNameTextView.setText(restaurant.getName());
                    placeAddressTextView.setText(restaurant.getAddress());
                    ratingBar.setRating(restaurant.getRating());
                    loadRestaurantImage(restaurant);
                }
            });

        }
        //Bouton pour sélection un restaurant
        fabDetailChoice.setOnClickListener(view -> {
            // Inverser l'état du bouton
            updateUserSelectedRestaurant();
        });

        //Bouton pour liker un restaurant
        buttonRestaurantLike.setOnClickListener(view -> {
            updateUserLikedPlace();
        });

        //Bouton pour ouvrir le site du restaurant
        buttonRestaurantWebsite.setOnClickListener(view -> {
            // Vérifier si l'URL du site web du restaurant est disponible

            // Ouvrir le site web du restaurant dans le navigateur par défaut
            String websiteUrl = restaurantViewModel.getSelectedRestaurantLiveData().getValue().getWebsiteUrl();
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));

            startActivity(webIntent);
            Toast.makeText(YourLunchDetailActivity.this, "Le site web est : " , Toast.LENGTH_SHORT).show();

        });

        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void filterAndSortWorkmatesByRestaurantId(String restaurantId) {
        if (userList != null) {
            List<User> filteredList = new ArrayList<>();
            for (User user : userList) {
                if (user.getSelectedRestaurantId() != null && user.getSelectedRestaurantId().equals(restaurantId)) {
                    filteredList.add(user);
                }
            }
            workmatesListViewAdapter.setUserList(filteredList);
        }
    }

    private void configureViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUserFromFirestore(mAuth.getCurrentUser().getUid());
        userViewModel.getWorkmatesListFromFirestore(false);
        userViewModel.getUserLiveData().observe(this, user -> {
            currentUser = user;
            userId = currentUser.getUserId();
            userViewModel.getWorkmatesListFromFirestore(false);
        });

        // Observer les changements de la liste des workmates récupérée depuis Firestore
        userViewModel.getUserListLiveData().observe(this, userList -> {
            if (userList != null) {
                this.userList = userList;
                filterAndSortWorkmatesByRestaurantId(restaurantId);
            }
        });
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
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

    private void configureRecyclerView() {
        workmatesListViewAdapter = new YourLunchDetailWorkmatesAdapter(new ArrayList<User>());
        workmatesRecyclerView.setAdapter(workmatesListViewAdapter);
        workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setButtonColor(boolean isSelected) {
        if (isSelected) {
            int greenColor = ContextCompat.getColor(this, R.color.green);
            fabDetailChoice.getDrawable().setTintList(ColorStateList.valueOf(greenColor));
        } else {
            int blackColor = ContextCompat.getColor(this, R.color.black);
            fabDetailChoice.getDrawable().setTintList(ColorStateList.valueOf(blackColor));
        }
    }

    private void updateUserSelectedRestaurant() {
        if (currentUser != null) {
            if (currentUser.getSelectedRestaurantId() == null || !currentUser.getSelectedRestaurantId().equals(restaurantId)) {
                // L'utilisateur n'a pas encore sélectionné de restaurant ou a sélectionné un autre restaurant
                currentUser.setSelectedRestaurantId(restaurantId);
                setButtonColor(true); // Mettre le bouton en vert
                restaurantViewModel.setSelectedRestaurant(restaurant); // Mettre à jour le restaurant choisi
            } else {
                // L'utilisateur a déjà sélectionné ce restaurant, il souhaite le désélectionner
                currentUser.setSelectedRestaurantId(null);
                setButtonColor(false); // Enlever la couleur du bouton
                restaurantViewModel.setSelectedRestaurant(null); // Réinitialiser le restaurant choisi
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
