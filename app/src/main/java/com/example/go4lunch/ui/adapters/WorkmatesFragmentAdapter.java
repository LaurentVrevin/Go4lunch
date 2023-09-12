package com.example.go4lunch.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
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
import com.example.go4lunch.utils.LikesCounter;

public class WorkmatesFragmentAdapter extends RecyclerView.Adapter<WorkmatesFragmentAdapter.WorkmatesFragmentViewHolder> {

    private List<User> workmatesList;
    private List<Restaurant> restaurantList;
    private String profilePictureUrl;
    List<User> usersWithRestaurantId = new ArrayList<>();
    List<User> usersWithoutRestaurantId = new ArrayList<>();

    public WorkmatesFragmentAdapter(List<User> workmatesList, List<Restaurant> restaurantList) {
        this.workmatesList = workmatesList;
        this.restaurantList = restaurantList;
        updateUsersLists();
    }

    // Update the lists of users with a selected restaurant or without
    private void updateUsersLists() {
        usersWithRestaurantId.clear();
        usersWithoutRestaurantId.clear();

        for (User user : workmatesList) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WorkmatesFragmentViewHolder holder, int position) {
        // Display users with a selected restaurant first
        if (!usersWithRestaurantId.isEmpty() && position < usersWithRestaurantId.size()) {
            User user = usersWithRestaurantId.get(position);
            String selectedRestaurantId = user.getSelectedRestaurantId();
            profilePictureUrl = user.getPictureUrl();

            if (isRestaurantInList(selectedRestaurantId)) {
                String selectedRestaurantName = getSelectedRestaurantName(user);
                holder.userNameTextView.setText(user.getName().split(" ")[0] + holder.itemView.getContext().getString(R.string.workmates_fragment_adapter_is_eating_at) + "(" + selectedRestaurantName + ")");
                setClickListener(holder.itemView, user);
            } else {
                holder.userNameTextView.setText(user.getName().split(" ")[0] + holder.itemView.getContext().getString(R.string.workmates_fragment_adapter_no_restaurant_selected));
                setClickListener(holder.itemView, null);
            }

            if (profilePictureUrl != null) {
                Glide.with(holder.itemView.getContext())
                        .load(profilePictureUrl)
                        .circleCrop()
                        .into(holder.workmatesAvatar);
            }
        } else {
            // Display users without a selected restaurant next
            int adjustedPosition = position - usersWithRestaurantId.size();
            User user = usersWithoutRestaurantId.get(adjustedPosition);
            holder.userNameTextView.setText(user.getName().split(" ")[0] + holder.itemView.getContext().getString(R.string.workmates_fragment_adapter_no_restaurant_selected));
            setClickListener(holder.itemView, null);
            profilePictureUrl = user.getPictureUrl();

            if (profilePictureUrl != null) {
                Glide.with(holder.itemView.getContext())
                        .load(profilePictureUrl)
                        .circleCrop()
                        .into(holder.workmatesAvatar);
            }
        }
    }

    // Check if the restaurant chosen by the workmate exists in the list of restaurants
    private boolean isRestaurantInList(String restaurantId) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getPlaceId().equals(restaurantId)) {
                return true;
            }
        }
        return false;
    }

    // Get the name of the restaurant selected by a user
    private String getSelectedRestaurantName(User user) {
        String selectedRestaurantId = user.getSelectedRestaurantId();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getPlaceId().equals(selectedRestaurantId)) {
                return restaurant.getName();
            }
        }
        return "";
    }

    // Set the click listener for a user
    private void setClickListener(View itemView, User user) {
        itemView.setOnClickListener(view -> {
            if (user != null) {
                Intent intent = new Intent(view.getContext(), YourLunchDetailActivity.class);
                intent.putExtra("restaurant", getSelectedRestaurant(user));
                view.getContext().startActivity(intent);
            }
        });
    }

    // Get the restaurant selected by a user
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

        private final TextView userNameTextView;
        private final ImageView workmatesAvatar;

        public WorkmatesFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.tv_workmate_name);
            workmatesAvatar = itemView.findViewById(R.id.im_workmate_listview_avatar);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setfilteredUsersList(List<User> filteredList) {
        this.workmatesList = filteredList;
        updateUsersLists();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAllUsersList(List<User> allUsersList) {
        LikesCounter.updateLikesCount(restaurantList, allUsersList);
        updateUsersLists();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }
}
