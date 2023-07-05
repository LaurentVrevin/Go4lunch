package com.example.go4lunch.ui.adapters;

import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.nearbysearch.OpeningHours;
import com.example.go4lunch.models.nearbysearch.Result;

import java.util.List;



import android.widget.ImageView;
import android.widget.RatingBar;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewViewHolder> {

    private List<Restaurant> restaurantList;
    private Location location;

    public ListViewAdapter(List<Restaurant> restaurantList, Location location) {
        this.restaurantList = restaurantList;
        this.location = location;
    }

    @NonNull
    @Override
    public ListViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ListViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);
    }
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ListViewViewHolder extends RecyclerView.ViewHolder {
        private ImageView placeImageView;
        private TextView placeNameTextView;
        private TextView placeRangeTextView;
        private TextView placeAddressTextView;
        private ImageView workmateImageView;
        private TextView workmateNumberTextView;
        private RatingBar ratingBar;
        private TextView placeOpeningTime;

        public ListViewViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImageView = itemView.findViewById(R.id.im_place);
            placeNameTextView = itemView.findViewById(R.id.tv_place_name);
            placeRangeTextView = itemView.findViewById(R.id.tv_place_range);
            placeAddressTextView = itemView.findViewById(R.id.tv_place_address);
            placeOpeningTime = itemView.findViewById(R.id.tv_place_open);
            workmateImageView = itemView.findViewById(R.id.im_workmate);
            workmateNumberTextView = itemView.findViewById(R.id.tv_workmate_number);
            ratingBar = itemView.findViewById(R.id.rb_list_view_rate);
        }

        public void bind(Restaurant restaurant) {
            // Mettre à jour les vues avec les données du restaurant
            placeNameTextView.setText(restaurant.getName());
            placeAddressTextView.setText(restaurant.getAddress());

            // Gérer l'affichage de l'ouverture du restaurant
            placeOpeningTime.setText(getOpeningHours(restaurant));

            // Mettre à jour la distance
            updateDistance(restaurant);

            // Charger l'image
            loadRestaurantImage(restaurant);

            // Mettre à jour le nombre de workmates
            updateWorkmatesCount(restaurant);

            ratingBar.setRating(restaurant.getRating());
        }

        private void updateDistance(Restaurant restaurant) {
            if (restaurant.getDistance() != null) {
                double distanceInMeters = restaurant.getDistance();
                String distanceText = String.format("%.0f m", distanceInMeters);
                placeRangeTextView.setText(distanceText);
            } else {
                placeRangeTextView.setText("");
            }
        }

        private void loadRestaurantImage(Restaurant restaurant) {
            if (restaurant.getPhotoUrls() != null && !restaurant.getPhotoUrls().isEmpty()) {
                String photoUrl = restaurant.getPhotoUrls().get(0); // Utilisez le premier URL de photo

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.lunch)
                        .override(placeImageView.getWidth(), placeImageView.getWidth()) // Ajuste la taille de l'image
                        .centerCrop(); // Effectue un recadrage centré pour obtenir un format carré

                Glide.with(itemView)
                        .load(photoUrl)
                        .apply(requestOptions)
                        .into(placeImageView);
            } else {
                // Si l'URL de l'image est nulle, afficher une image de placeholder
                placeImageView.setImageResource(R.drawable.lunch);
            }
        }

        private void updateWorkmatesCount(Restaurant restaurant) {
            int workmatesCount = restaurant.getWorkmatesCount();
            if (workmatesCount > 0) {
                workmateImageView.setVisibility(View.VISIBLE);
                workmateNumberTextView.setVisibility(View.VISIBLE);
                workmateNumberTextView.setText(String.valueOf(workmatesCount));
            } else {
                workmateImageView.setVisibility(View.GONE);
                workmateNumberTextView.setVisibility(View.GONE);
            }
        }

        private String getOpeningHours(Restaurant restaurant) {
            String openingHours = restaurant.getOpeningHours();
            if (openingHours != null) {
                if (openingHours.equals("Ouvert")) {
                    placeOpeningTime.setTextColor(Color.BLUE);
                    return openingHours;
                } else if (openingHours.equals("Fermé")) {
                    placeOpeningTime.setTextColor(Color.RED);
                    return openingHours;
                }
            }
            placeOpeningTime.setTextColor(Color.RED);
            return "Fermé";
        }
    }
}
