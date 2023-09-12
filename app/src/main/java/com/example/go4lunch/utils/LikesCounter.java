package com.example.go4lunch.utils;

import android.util.Log;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikesCounter {
    public static void updateLikesCount(List<Restaurant> restaurantList, List<User> userList) {

        if (restaurantList == null || userList == null) {
            // One of the lists is null, do not perform the update
            return;
        }
        Map<String, Integer> likesCountMap = new HashMap<>();

        // Initialize the map with like counters set to zero for each restaurant
        for (Restaurant restaurant : restaurantList) {
            likesCountMap.put(restaurant.getPlaceId(), 0);
        }

        // Iterate through the list of users
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

        // Update the like counters of the restaurants in the list
        for (Restaurant restaurant : restaurantList) {
            String restaurantId = restaurant.getPlaceId();
            if (likesCountMap.containsKey(restaurantId)) {
                int likesCount = likesCountMap.get(restaurantId);
                restaurant.setLikesCount(likesCount);

                // Calculate star rating based on likes
                float rating = calculateRatingFromLikesPercentage(likesCount, userList.size());
                restaurant.setRating(rating);
                Log.d("LIKES_COUNTER", "Restaurant: " + restaurant.getName() + " " + restaurant.getPlaceId() + " " + " - Likes: " + likesCount + " Stars:" + restaurant.getRating());
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
            return 3.0f; // 3 stars
        } else if (likesPercentage >= 33) {
            return 2.0f; // 2 stars
        } else if (likesPercentage >= 1) {
            return 1.0f; // 1 star
        } else {
            return 0.0f; // 0 star
        }
    }
}
