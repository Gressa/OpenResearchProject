package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.Route;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

/**
 * Created by Gresa-PC on 3/31/2017.
 */

public class DirectionFinderContract {

    public interface View {
        void showListRoutes(List<Route> routes);

        void showMessageOnFailure(String message);
    }

    public interface Presenter{
        void onResume(View view, String origin, String destination, String key, boolean b);
        void onShowDrivingRouteBetweenTwoLocations(GoogleMap map, List<Route> routes);
    }
}

