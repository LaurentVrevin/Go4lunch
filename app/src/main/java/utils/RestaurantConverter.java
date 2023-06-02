package utils;



//Cette classe me permet de convertir les données récupérées via NearbySearchResponse
//en objet java que je stock dans une liste restaurantList selon mon model Restaurant

import java.util.ArrayList;
import java.util.List;

import models.Restaurant;
import models.nearbysearch.NearbySearchResponse;
import models.nearbysearch.OpeningHours;
import models.nearbysearch.Photo;
import models.nearbysearch.Result;

public class RestaurantConverter {
   /* public static List<Restaurant> convertToRestaurantList(NearbySearchResponse response) {
        // Liste des restaurants convertis
        List<Restaurant> restaurantList = new ArrayList<>();

        // Récupérer les résultats de la recherche
        List<Result> results = response.getResults();
        if (results != null) {
            // Parcourir les résultats
            for (Result result : results) {
                // Récupérer les informations du restaurant
                String id = result.getPlaceId();
                String name = result.getName();
                String address = result.getVicinity();
                String phone = result.getFormattedPhoneNumber();
                String websiteUrl = result.getWebsite();
                List<Photo> photoUrl = result.getPhotos();
                double latitude = result.getGeometry().getLocation().getLat();
                double longitude = result.getGeometry().getLocation().getLng();
                String openingHours = getOpeningHours(result);
                String closingHours = getClosingHours(result);
                Double rating = result.getRating();
                

                // Créer un objet Restaurant avec les informations récupérées
                Double distance = null;
                Restaurant restaurant = new Restaurant(id, name, address, phone, websiteUrl, photoUrl,
                        latitude, longitude, openingHours, closingHours, rating, distance);

                // Ajouter le restaurant à la liste
                restaurantList.add(restaurant);
            }
        }

        // Retourner la liste des restaurants convertis
        return restaurantList;
    }

   private static String getOpeningHours(Result result) {
        OpeningHours openingHours = result.getOpeningHours();
        if (openingHours != null && openingHours.isOpenNow()) {
            // Le lieu est ouvert actuellement
            return "Ouvert";
        } else {
            // Le lieu est fermé actuellement
            return "Fermé";
        }
    }

    private static String getClosingHours(Result result) {
        OpeningHours openingHours = result.getOpeningHours();
        if (openingHours != null && openingHours.isOpenNow()) {
            // Le lieu est ouvert actuellement, il n'y a pas d'heure de fermeture spécifique
            return "Pas d'heure de fermeture spécifique";
        } else if (openingHours != null && openingHours.getCloseTime() != null) {
            // Le lieu est fermé actuellement et il y a une heure de fermeture spécifique
            return openingHours.getCloseTime();
        } else {
            // Les informations sur les heures de fermeture ne sont pas disponibles
            return "Inconnu";
        }
    }*/
}
