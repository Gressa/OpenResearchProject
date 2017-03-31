package com.example.gresa_pc.openprject.presenter;

import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.ui.view.IParkingSitesEngine;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Gresa-PC on 3/28/2017.
 */

public class ParkingSitesPresenter implements ParkingSitesContract.Presenter, IParkingSitesEngine.LoadListener{
    private ParkingSitesContract.View mView;
    private IParkingSitesEngine mIParkingSiteEngine;

    public ParkingSitesPresenter(IParkingSitesEngine iParkingSitesEngine) {
        mIParkingSiteEngine = iParkingSitesEngine;
    }

    @Override
    public void onResume(ParkingSitesContract.View view) {
        this.mView = view;
        mView.showProgressDialog();
        mIParkingSiteEngine.getParkings(this);
    }

    @Override
    public void onDrawParkingSites(List<ParkingSite> parkingSites, GoogleMap mMap) {
        for (ParkingSite parkingSite : parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(parkingSite.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to Mountain View
                    .zoom(15)                    // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLoaded(List<ParkingSite> parkingSites) {
        List<ParkingSite> allParkingSites = parkingSites;
        if(allParkingSites != null && allParkingSites.size()>0){
            mView.showAllParkingSites(allParkingSites);
            mView.hideProgressDialog();
        }
        else {
            String message = "There is no Parking Sites";
            mView.showMessageOnEmptyParkingSites(message);
            mView.hideProgressDialog();
        }
    }

    @Override
    public void onError(String message) {
        mView.showMessageOnFailure(message);
    }

}
