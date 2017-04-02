package com.example.gresa_pc.openprject.engine;

import android.util.Log;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.model.ParkingSiteLocation;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesContract;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Gresa-PC on 3/23/2017.
 */

public class ParkingSitesEngine implements ParkingSitesContract {
   private ApiService mApiService;
//    @Inject ApiService mApiService;

    public ParkingSitesEngine(ApiService mApiService){
        this.mApiService = mApiService;
    }

    @Override
    public void getParkings(final LoadListenerParkingSites listener){
        Call<ParkingSiteLocation> call = mApiService.getParkingLocation();
        call.enqueue(new Callback<ParkingSiteLocation>() {
            @Override
            public void onResponse(Call<ParkingSiteLocation> call, Response<ParkingSiteLocation> response) {
                Log.d(TAG, "onSuccess");
                List<ParkingSite> parkingSiteLocation = response.body().getParkingSites();
                listener.onLoadedParkingSites(parkingSiteLocation);
            }
            @Override
            public void onFailure(Call<ParkingSiteLocation> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                listener.onErrorParkingSites(t.getMessage());
            }
        });
    }
}
