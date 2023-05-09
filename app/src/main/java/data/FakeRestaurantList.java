package data;

import java.util.ArrayList;

import models.Restaurant;

public class FakeRestaurantList {
        public static ArrayList<Restaurant> getRestaurantList() {
            ArrayList<Restaurant> restaurantList = new ArrayList<>();
            restaurantList.add(new Restaurant("1", "Le Bistrot du Boucher", "16 Rue Saint-Laurent, 14000 Caen", "02 31 86 30 47", "https://www.bistrotduboucher.com/", "https://www.example.com/bistrotduboucher.jpg", 49.1825781, -0.3700876, "09:00", "22:00", 4));
            restaurantList.add(new Restaurant("2", "La Belle Epoque", "20 Place Fontette, 14000 Caen", "02 31 50 73 37", "https://www.labelleepoque-caen.com/", "https://www.example.com/labelleepoque.jpg", 49.1820343, -0.3701363, "10:00", "23:00", 4));
            restaurantList.add(new Restaurant("3", "La Fourchette du Printemps", "2 Rue de l'Ancienne Mairie, 14000 Caen", "02 31 86 39 94", "https://www.lafourchetteduprintemps.fr/", "https://www.example.com/lafourchetteduprintemps.jpg", 49.1855849, -0.3672825, "11:30", "14:00 / 19:00", 4));
            restaurantList.add(new Restaurant("4", "Le Casier", "28 Rue de Bras, 14000 Caen", "02 31 86 68 30", "https://www.lecasier-restaurant.com/", "https://www.example.com/lecasier.jpg", 49.1842621, -0.3684613, "12:00", "14:00 / 19:30", 4));
            restaurantList.add(new Restaurant("5", "Le Quai 52", "52 Quai Vendeuvre, 14000 Caen", "02 31 50 40 52", "https://www.quai52.com/", "https://www.example.com/quai52.jpg", 49.1798585, -0.3744667, "11:30", "14:00 / 19:00", 4));
            restaurantList.add(new Restaurant("6", "L'Assiette", "59 Rue Saint-Jean, 14000 Caen", "02 31 86 22 80", "https://www.lassiettecaen.fr/", "https://www.example.com/lassiette.jpg", 49.1831397, -0.3708246, "12:00", "14:00 / 19:30", 4));
            restaurantList.add(new Restaurant("7", "La Maison", "38 Rue Caponière, 14000 Caen", "02 31 83 32 95", "https://www.lamaison-caen.com/", "https://www.example.com/lamaison.jpg", 49.1862621, -0.3649333, "12:00", "14:00 / 19:30", 4));
    return new ArrayList<>(restaurantList);
        }

}
