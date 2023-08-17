package com.example.go4lunch.utils;

import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;

import java.util.HashMap;
import java.util.List;

public class LikesCounter {
    public static void getLikesCountMap(List<Restaurant> restaurantList, List<User> userList) {
        HashMap<String, Integer> likesCountMap = new HashMap<>();

        // Parcourir la liste des utilisateurs
        for (User user : userList) {
            if (user == null) {
                continue;
            }
            // Récupérer la liste des restaurants likés par l'utilisateur
            List<String> likedPlaces = user.getLikedPlaces();

            // Parcourir la liste des restaurants likés par l'utilisateur
            for (String restaurantId : likedPlaces) {
                // Vérifier si le restaurant est déjà dans la likesCountMap
                if (likesCountMap.containsKey(restaurantId)) {
                    // S'il est présent, incrémenter le compteur de likes pour ce restaurant
                    int count = likesCountMap.get(restaurantId);
                    likesCountMap.put(restaurantId, count + 1);
                } else {
                    // S'il n'est pas présent, ajouter le restaurant à la likesCountMap avec un compteur initial de 1
                    likesCountMap.put(restaurantId, 1);
                }
            }
        }

        // Mettre à jour les compteurs de likes des restaurants
        for (Restaurant restaurant : restaurantList) {
            String restaurantId = restaurant.getPlaceId();

            if (likesCountMap.containsKey(restaurantId)) {
                int likesCount = likesCountMap.get(restaurantId);
                restaurant.setLikesCount(likesCount);
            } else {
                restaurant.setLikesCount(0); // Si le restaurant n'a pas de likes
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

