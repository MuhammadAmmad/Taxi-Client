package com.kerer.taxiapp.rest;

import com.kerer.taxiapp.model.RouteResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ivan on 07.01.17.
 */

public interface GoogleDirectionsApiService {
    @GET("maps/api/directions/json")
    Call<RouteResponse> getDirection(@Query("origin") String origin,
                                     @Query("destination") String destination,
                                     @Query("key") String googleApiKey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
