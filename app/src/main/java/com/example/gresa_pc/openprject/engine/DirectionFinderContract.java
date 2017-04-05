package com.example.gresa_pc.openprject.engine;

import com.example.gresa_pc.openprject.model.Route;
import java.util.List;

/**
 * Created by Gresa-PC on 3/31/2017.
 */

public interface DirectionFinderContract {
    interface LoadListenerDirectionFinder {
        void onLoadedDirectionFinder(List<Route> routes);
        void onErrorDirectionFinder(String message);
    }
    void getDirectionRoutes(LoadListenerDirectionFinder listener,String origin, String destination, String key, boolean alternatives);
}