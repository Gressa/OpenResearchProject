package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.Route;

import java.util.List;

/**
 * Created by Gresa-PC on 3/26/2017.
 */

public interface DirectionFinderView {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
