package com.example.gresa_pc.openprject.ui.view;

import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

/**
 * Created by Gresa-PC on 3/28/2017.
 */

public class ParkingSitesContract {
    public interface View{
        void showAllParkingSites(List<ParkingSite> parkingSites);
        void showMessageOnFailure(String message);
        void showMessageOnEmptyParkingSites(String message);
        void showProgressDialog();
        void hideProgressDialog();
    }

    public interface Presenter{
        void onResume(ParkingSitesContract.View view);
        void onDrawParkingSites(List<ParkingSite> parkingSites, GoogleMap mMap);
    }
}
