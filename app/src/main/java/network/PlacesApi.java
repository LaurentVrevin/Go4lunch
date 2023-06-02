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
    @GET("nearbysearch/json?type=restaurant&key=" + BuildConfig.MAPS_API_KEY)
    Call<NearbySearchResponse> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius);

    @GET("details/json?key=" + BuildConfig.MAPS_API_KEY)
    Call<PlaceDetailsResponse> getPlaceDetailsResponse(
            @Query("fields") String fields,
            @Query("place_id") String placeId);

}
