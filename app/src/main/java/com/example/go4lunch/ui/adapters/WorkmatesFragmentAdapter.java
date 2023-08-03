package com.example.go4lunch.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.activities.YourLunchDetailActivity;

public class WorkmatesFragmentAdapter extends RecyclerView.Adapter<WorkmatesFragmentAdapter.WorkmatesFragmentViewHolder> {

    private List<User> userList;
    private List<Restaurant> restaurantList;
    private Restaurant restaurantItem;

    List<User> usersWithRestaurantId = new ArrayList<>();
    List<User> usersWithoutRestaurantId = new ArrayList<>();

    public WorkmatesFragmentAdapter(List<User> userList, List<Restaurant> restaurantList) {
        this.userList = userList;
        this.restaurantList = restaurantList;
        updateUsersLists();
    }

    // Met à jour les listes des utilisateurs ayant un restaurant sélectionné ou non
    private void updateUsersLists() {
        usersWithRestaurantId.clear();
        usersWithoutRestaurantId.clear();

        for (User user : userList) {
            if (user.getSelectedRestaurantId() != null) {
                usersWithRestaurantId.add(user);
            } else {
                usersWithoutRestaurantId.add(user);
            }
        }
    }

    @NonNull
    @Override
    public WorkmatesFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_listview, parent, false);
        return new WorkmatesFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesFragmentViewHolder holder, int position) {
        // Affiche les utilisateurs avec restaurant sélectionné en premier
        if (!usersWithRestaurantId.isEmpty() && position < usersWithRestaurantId.size()) {
            User user = usersWithRestaurantId.get(position);
            holder.userNameTextView.setText(user.getName() + " - (" + getSelectedRestaurantName(user) + ")");
            setClickListener(holder.itemView, user);
            String profilePictureUrl = user.getPictureUrl();
            if (profilePictureUrl != null) {
                Glide.with(holder.itemView.getContext())
                        .load(profilePictureUrl)
                        .circleCrop()
                        .into(holder.workmatesAvatar);
            }
        } else {
            // Affiche les utilisateurs sans restaurant sélectionné ensuite
            int adjustedPosition = position - usersWithRestaurantId.size();
            User user = usersWithoutRestaurantId.get(adjustedPosition);
            holder.userNameTextView.setText(user.getName() + " - (Pas de restaurant choisi)");
            setClickListener(holder.itemView, null);
            // Afficher une image par défaut pour les utilisateurs sans restaurant
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.default_avatar)
                    .circleCrop()
                    .into(holder.workmatesAvatar);
        }
    }

    // Récupère le nom du restaurant sélectionné par un utilisateur
    private String getSelectedRestaurantName(User user) {
        String selectedRestaurantId = user.getSelectedRestaurantId();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getPlaceId().equals(selectedRestaurantId)) {
                return restaurant.getName();
            }
        }
        return "";
    }

    // Définit le click listener pour un utilisateur
    private void setClickListener(View itemView, User user) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    Intent intent = new Intent(view.getContext(), YourLunchDetailActivity.class);
                    intent.putExtra("restaurant", getSelectedRestaurant(user));
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    // Récupère le restaurant sélectionné par un utilisateur
    private Restaurant getSelectedRestaurant(User user) {
        String selectedRestaurantId = user.getSelectedRestaurantId();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getPlaceId().equals(selectedRestaurantId)) {
                return restaurant;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (!usersWithRestaurantId.isEmpty()) {
            count += usersWithRestaurantId.size();
        }
        if (!usersWithoutRestaurantId.isEmpty()) {
            count += usersWithoutRestaurantId.size();
        }
        return count;
    }

    public static class WorkmatesFragmentViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private ImageView workmatesAvatar;

        public WorkmatesFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.tv_workmate_name);
            workmatesAvatar = itemView.findViewById(R.id.im_workmate);
        }
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        updateUsersLists();
        notifyDataSetChanged();
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }
}
