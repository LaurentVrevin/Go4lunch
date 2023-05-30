package network;

import network.PlacesApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Util class to manage Retrofit builder
 **/
public class RetrofitBuilder {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    public static PlacesApi buildPlacesApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //journal pour récupérer les requêtes json
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build();

        return retrofit.create(PlacesApi.class);
    }
}
