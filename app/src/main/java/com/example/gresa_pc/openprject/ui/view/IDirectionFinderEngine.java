package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.Route;

import java.util.List;

/**
 * Created by Gresa-PC on 3/31/2017.
 */

public interface IDirectionFinderEngine {
    interface LoadListener {
        void onLoaded(List<Route> routes);
        void onError(String message);
    }
    //void getDirectionRoute(LoadListener listener);
    void getDirectionRoutes(LoadListener listener,String origin, String destination, String key, boolean alternatives);
}