package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.ParkingSite;
import java.util.List;

/**
 * Created by Gresa-PC on 3/24/2017.
 */

public interface ParkingSitesView {
    void onParkingSitesListLoadSuccess(List<ParkingSite> parkingSites);
    void onParkingSitesListLoadFailure();
}
