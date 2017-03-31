package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.ParkingSite;

import java.util.List;

/**
 * Created by Gresa-PC on 3/29/2017.
 */

public interface IParkingSitesEngine {
    interface LoadListener {
        void onLoaded(List<ParkingSite> parkingSiteList);
        void onError(String message);
    }
    void getParkings(LoadListener listener);
}
