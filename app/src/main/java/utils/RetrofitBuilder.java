package utils;

import network.PlacesApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utils class to manage Retrofit builder
 **/

public class RetrofitBuilder {

    // URL de base pour l'API des lieux
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    private Retrofit mRetrofit;

    public PlacesApi buildRetrofit() {
        // Création d'une instance Retrofit
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL) // Définition de l'URL de base
                .addConverterFactory(GsonConverterFactory.create()) // Utilisation de Gson pour convertir les réponses JSON en objets Java
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build()) // Configuration du client HTTP avec un intercepteur pour les journaux
                .build();

        // Création d'une instance de l'interface PlacesApi à partir de Retrofit
        return mRetrofit.create(PlacesApi.class);
    }
}
