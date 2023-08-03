package com.example.go4lunch.utils;

import com.example.go4lunch.models.User;

import java.util.HashMap;
import java.util.List;

public class WorkmatesCounter {
    public static void updateWorkmatesCount(List<User> userList, HashMap<String, Integer> workmatesCountMap) {
        workmatesCountMap.clear(); // Effacer la carte actuelle

        for (User user : userList) {
            String selectedRestaurantId = user.getSelectedRestaurantId();
            if (selectedRestaurantId != null) {
                // Incrémenter le compteur pour chaque restaurant choisi par un workmate
                if (workmatesCountMap.containsKey(selectedRestaurantId)) {
                    int count = workmatesCountMap.get(selectedRestaurantId);
                    workmatesCountMap.put(selectedRestaurantId, count + 1);
                } else {
                    workmatesCountMap.put(selectedRestaurantId, 1);
                }
            }
        }
    }
}
