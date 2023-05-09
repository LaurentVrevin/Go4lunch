package repositories;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import models.Restaurant;

public class RestaurantRepositoryImpl implements RestaurantInterfaceRepository {

    private static RestaurantRepositoryImpl instance;
    private final FirebaseFirestore firebaseFirestoreDatabase;
    private final CollectionReference restaurantCollection;

    public RestaurantRepositoryImpl(Context context) {
        firebaseFirestoreDatabase = FirebaseFirestore.getInstance();
        restaurantCollection = firebaseFirestoreDatabase.collection("restaurant");
    }

    public static synchronized RestaurantRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new RestaurantRepositoryImpl(context);
        }
        return instance;
    }

    @Override
    public void createRestaurantCollection() {
        restaurantCollection.document();
    }

    @Override
    public void addRestaurantsToFirestore(List<Restaurant> restaurants) {
        WriteBatch batch = firebaseFirestoreDatabase.batch();
        for (Restaurant restaurant : restaurants) {
            batch.set(restaurantCollection.document(restaurant.getId()), restaurant);
        }
        batch.commit();
    }

    @Override
    public void updateRestaurantsToFirestore(List<Restaurant> restaurants) {
        WriteBatch batch = firebaseFirestoreDatabase.batch();
        for (Restaurant restaurant : restaurants) {
            batch.set(restaurantCollection.document(restaurant.getId()), restaurant);
        }
        batch.commit();
    }

    @Override
    public void deleteRestaurantsToFirestore() {
        restaurantCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            WriteBatch batch = firebaseFirestoreDatabase.batch();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                batch.delete(documentSnapshot.getReference());
            }
            batch.commit();
        });
    }

    @Override
    public List<Restaurant> getRestaurantsToFirestore() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurantCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                restaurants.add(restaurant);
            }
        });
        return restaurants;
    }
}
