package network;

import models.nearbysearch.NearbySearchResponse;
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


}
