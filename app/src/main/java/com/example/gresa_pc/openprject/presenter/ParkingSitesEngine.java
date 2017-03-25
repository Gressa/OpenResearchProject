package com.example.gresa_pc.openprject.presenter;

import android.util.Log;

import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.model.ParkingSiteLocation;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesView;

import java.util.List;

import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Gresa-PC on 3/23/2017.
 */

public class ParkingSitesEngine {

    private ParkingSitesView view;

    @Inject
    ApiService apiService;

    public ParkingSitesEngine(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setParkingSitesView(ParkingSitesView view){
        this.view = view;
    }
    public void getParkings() {
        Call<ParkingSiteLocation> call = this.apiService.getParkingLocation();
        call.enqueue(new Callback<ParkingSiteLocation>() {
            @Override
            public void onResponse(Call<ParkingSiteLocation> call, Response<ParkingSiteLocation> response) {
                Log.d(TAG, "onSuccess");
                List<ParkingSite> parkingSiteLocation = response.body().getParkingSites();
                view.onParkingSitesListLoadSuccess(parkingSiteLocation);
            }

            @Override
            public void onFailure(Call<ParkingSiteLocation> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                view.onParkingSitesListLoadFailure();
            }
        });
    }
}
