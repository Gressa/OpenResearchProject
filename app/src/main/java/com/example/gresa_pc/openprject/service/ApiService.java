package com.example.gresa_pc.openprject.service;

import com.example.gresa_pc.openprject.model.ParkingSiteLocation;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Gresa-PC on 3/24/2017.
 */

public interface ApiService {
    @GET("parking_sites.json")
    Call<ParkingSiteLocation> getParkingLocation();
}
