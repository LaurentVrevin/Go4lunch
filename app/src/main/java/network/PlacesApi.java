package network;

import com.example.go4lunch.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import models.nearbysearch.NearbySearchResponse;
import models.nearbysearch.PlaceDetailsResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get data from Google places API
 */


public interface PlacesApi {
    @GET("nearbysearch/json" + BuildConfig.MAPS_API_KEY)
    Call<NearbySearchResponse> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type);

    @GET("details/json?" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetailsResponse> getPlaceDetailsResponse(
            @Query("fields") String fields,
            @Query("place_id") String placeId,
            @Query("key") String key);

    @GET("nearbysearch/json")
    Call<ResponseBody> getNearbyRestaurants(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey);

}
