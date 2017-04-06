package com.example.gresa_pc.openprject.service;

import com.example.gresa_pc.openprject.model.GetDirectionSerialized;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gresa-PC on 3/30/2017.
 */

public interface GoogleApiService {
    @GET("maps/api/directions/json")
    Call<GetDirectionSerialized> getDirectionRoute(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key, @Query("alternatives") boolean alternatives);
}
