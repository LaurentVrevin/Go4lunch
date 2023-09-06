package com.example.go4lunch.utils;

import android.util.Log;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikesCounter {
    public static void updateLikesCount(List<Restaurant> restaurantList, List<User> userList) {
        Map<String, Integer> likesCountMap = new HashMap<>();

        // Initialiser la map avec des compteurs de likes à zéro pour chaque restaurant
        for (Restaurant restaurant : restaurantList) {
            likesCountMap.put(restaurant.getPlaceId(), 0);
        }

        // Parcourir la liste des utilisateurs
        for (User user : userList) {
            List<String> likedPlaces = user.getLikedPlaces();

            Log.d("LIKES_COUNTER", "User: " + user.getName() + " - Liked places: " + likedPlaces.toString());
            if (likedPlaces != null) {
                for (String restaurantId : likedPlaces) {
                    Log.d("LIKES_COUNTER", "User: " + user.getName() + " - Liking restaurant: " + restaurantId);

                    if (likesCountMap.containsKey(restaurantId)) {
                        int currentLikesCount = likesCountMap.get(restaurantId);
                        likesCountMap.put(restaurantId, currentLikesCount + 1);
                    } else {
                        likesCountMap.put(restaurantId, 1);
                    }
                }
            }

        }

        // Mettre à jour les compteurs de likes des restaurants dans la liste
        for (Restaurant restaurant : restaurantList) {
            String restaurantId = restaurant.getPlaceId();
            if (likesCountMap.containsKey(restaurantId)) {
                int likesCount = likesCountMap.get(restaurantId);
                restaurant.setLikesCount(likesCount);

                // Calculer la note en étoiles en fonction des likes
                float rating = calculateRatingFromLikesPercentage(likesCount, userList.size());
                restaurant.setRating(rating);
                Log.d("LIKES_COUNTER", "Restaurant: " + restaurant.getName() + " " + restaurant.getPlaceId() + " " + " - Likes: " + likesCount + " étoiles :" + restaurant.getRating());
            } else {
                restaurant.setLikesCount(0);
                restaurant.setLikesCount(0);
                restaurant.setRating(0.0f);
            }
        }
    }


    public static float calculateRatingFromLikesPercentage(int totalLikes, int totalUsers) {
        float likesPercentage = (float) totalLikes / totalUsers * 100;

        if (likesPercentage >= 66) {
            return 3.0f; // 3 étoiles
        } else if (likesPercentage >= 33) {
            return 2.0f; // 2 étoiles
        } else if (likesPercentage >= 1) {
            return 1.0f; // 1 étoile
        } else {
            return 0.0f; // 0 étoile
        }
    }
}

