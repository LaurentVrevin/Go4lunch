package repositories;

import java.util.List;

import models.Restaurant;

public interface RestaurantInterfaceRepository {

    // Méthode pour créer la collection "restaurant" dans Firestore
    void createRestaurantCollection();

    // Méthode pour ajouter la liste des restaurants à la collection "restaurant" dans Firestore
    void addRestaurantsToFirestore(List<Restaurant> restaurants);

    // Méthode pour mettre à jour la liste des restaurants dans la collection "restaurant" dans Firestore
    void updateRestaurantsToFirestore(List<Restaurant> restaurants);

    // Méthode pour supprimer la liste des restaurants de la collection "restaurant" dans Firestore
    void deleteRestaurantsToFirestore();

    // Méthode pour récupérer la liste des restaurants de la collection "restaurant" dans Firestore
    List<Restaurant> getRestaurantsToFirestore();
}

