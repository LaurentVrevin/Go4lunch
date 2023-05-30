package network;

import com.google.android.gms.maps.model.LatLng;

import models.nearbysearch.NearbySearchResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get data from Google places API
 */


public interface PlacesApi {
    @GET("nearbysearch/json")
    Call<NearbySearchResponse> nearbySearch(
            @Query("location") String location,
            @Query("type") String type,
            @Query("key") String apiKey
    );

    @GET("nearbysearch/json")
    Call<ResponseBody> getNearbyRestaurants(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey);


}
