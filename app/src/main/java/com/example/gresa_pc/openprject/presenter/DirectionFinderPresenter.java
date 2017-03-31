package com.example.gresa_pc.openprject.presenter;

import android.graphics.Color;

import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.ui.view.DirectionFinderContract;
import com.example.gresa_pc.openprject.ui.view.IDirectionFinderEngine;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import static com.google.maps.android.PolyUtil.decode;

/**
 * Created by Gresa-PC on 3/31/2017.
 */

public class DirectionFinderPresenter implements DirectionFinderContract.Presenter, IDirectionFinderEngine.LoadListener{
    private DirectionFinderContract.View mView;
    private IDirectionFinderEngine mIDirectionFinderEngine;

    public DirectionFinderPresenter(IDirectionFinderEngine iDirectionFinderEngine){
        this.mIDirectionFinderEngine = iDirectionFinderEngine;
    }

    @Override
    public void onResume(DirectionFinderContract.View view, String origin, String destination, String key, boolean alternatives) {
        this.mView = view;
        mView.showProgressDialog();
        mIDirectionFinderEngine.getDirectionRoutes(this,origin,destination,key, alternatives);
    }

    @Override
    public void onShowDrivingRouteBetweenTwoLocations(GoogleMap map, List<Route> routes) {
        for(Route route: routes) {
            String routePolyLine = route.getOverviewPolyline().getPoints();
            List<LatLng> list = decode(routePolyLine);
            map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(15)
                    .color(Color.BLUE)
                    .geodesic(true));
        }
        mView.showNearParkingSites();
    }

    @Override
    public void onShowNearParkingSites(List<ParkingSite> parkingSites, List<Route> routes, GoogleMap mMap) {
//        List<LatLng> nearParkingSites = new ArrayList<>();
        List<ParkingSite> nearParkingSites = new ArrayList<>();
        for(ParkingSite parkingSite: parkingSites)
        {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            for (Route route: routes) {
                String routePolyLine = route.getOverviewPolyline().getPoints();
                List<LatLng> list = decode(routePolyLine);
                if(PolyUtil.isLocationOnPath(latLng,list,true,100)){
                   // nearParkingSites.add(latLng);
                    nearParkingSites.add(parkingSite);
                }
            }
        }
        mView.showNearParkingSitesOnMap(nearParkingSites, mMap);
    }

    @Override
    public void onShowNearParkingSitesOnMap(List<ParkingSite> latLngs, GoogleMap map) {
        for (ParkingSite parkingSite : latLngs) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(parkingSite.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to Mountain View
                    .zoom(7)                    // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLoaded(List<Route> routes) {
        mView.showListRoutes(routes);
        mView.hideProgressDialog();
    }

    @Override
    public void onError(String message) {
        mView.showMessageOnFailure(message);
        mView.hideProgressDialog();
    }
}

