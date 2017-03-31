package com.example.gresa_pc.openprject.engine;

/**
 * Created by Gresa-PC on 3/31/2017.
 */

import com.example.gresa_pc.openprject.model.GetDirectionSerialized;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.service.GoogleApiService;
import com.example.gresa_pc.openprject.ui.view.IDirectionFinderEngine;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gresa-PC on 3/26/2017.
 */

public class DirectionFinderEngine implements IDirectionFinderEngine {
    private GoogleApiService googleApiService;

    public DirectionFinderEngine(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    @Override
    public void getDirectionRoutes(final LoadListener listener, String origin, String destination, String key, boolean alternatives){
        retrofit2.Call<GetDirectionSerialized> call = googleApiService.getDirectionRoute(origin, destination, key, alternatives);
        call.enqueue(new Callback<GetDirectionSerialized>() {
            @Override
            public void onResponse(retrofit2.Call<GetDirectionSerialized> call, Response<GetDirectionSerialized> response) {
                List<Route> routes = response.body().getRoutes();
                listener.onLoaded(routes);
            }

            @Override
            public void onFailure(retrofit2.Call<GetDirectionSerialized> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }
}


