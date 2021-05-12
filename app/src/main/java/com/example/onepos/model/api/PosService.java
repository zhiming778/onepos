package com.example.onepos.model.api;

import android.content.ContentValues;

import androidx.room.RawQuery;

import com.example.onepos.model.Address;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PosService {

    @GET("REST/v1/Routes")
    Call<ContentValues> getRoute(@Query("wayPoint.1") String startingPoint, @Query("wayPoint.2") String destination,
                                 @Query("wayPoint.n") int numOfPoints, @Query("distanceUnit") String unit, @Query("key") String key);

    @GET("REST/v1/Imagery/Map/Road/Routes?dpi=large")
    Call<ResponseBody> getMapImage(@Query("wayPoint.1") String startingPoint, @Query("wayPoint.2") String destination, @Query("key") String key);

    @GET("REST/v1/Autosuggest?countryFilter=US")
    Call<List<Address>> getSuggestAddresses(@Query("query") String query, @Query("userLocation") String coordinates, @Query("key") String key);
}
