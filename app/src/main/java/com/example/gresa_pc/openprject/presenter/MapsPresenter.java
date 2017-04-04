package com.example.gresa_pc.openprject.presenter;

import android.graphics.Color;
import com.example.gresa_pc.openprject.R;
import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.ui.DirectionFinderContract;
import com.example.gresa_pc.openprject.ui.view.MapsView;
import com.example.gresa_pc.openprject.ui.ParkingSitesContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.List;
import static com.google.maps.android.PolyUtil.decode;

/**
 * Created by Gresa-PC on 4/1/2017.
 */

public class MapsPresenter implements DirectionFinderContract.LoadListenerDirectionFinder, ParkingSitesContract.LoadListenerParkingSites{
    private ParkingSitesContract mIParkingSiteEngine;
    private MapsView mView;
    private DirectionFinderContract mIDirectionFinderEngine;
    private List<ParkingSite> mParkingSites;
    private List<Route> routes;
    private GoogleMap mMap;

    public MapsPresenter(DirectionFinderContract iDirectionFinderEngine, ParkingSitesContract iParkingSitesEngine){
        this.mIDirectionFinderEngine = iDirectionFinderEngine;
        this.mIParkingSiteEngine = iParkingSitesEngine;
    }

    public void onResumeDirectionFinder(MapsView view, GoogleMap mMap, String origin, String destination, String key, boolean alternatives) {
        this.mView = view;
        this.mMap = mMap;
        mView.showProgressDialog();
        mIDirectionFinderEngine.getDirectionRoutes(this,origin,destination,key, alternatives);
    }

    public void onResumeParkingSites(MapsView view, GoogleMap mMap) {
        this.mView = view;
        this.mMap = mMap;
        mView.showProgressDialog();
        mIParkingSiteEngine.getParkingSites(this);
    }
    @Override
    public void onLoadedParkingSites(List<ParkingSite> parkingSiteList) {
        this.mParkingSites = parkingSiteList;
        if(mParkingSites != null && mParkingSites.size()>0){
            showParkingSites(mParkingSites,15);
            mView.hideProgressDialog();
        }
        else {
            String message = "There is no Parking Sites";
            mView.showMessageOnEmpty(message);
            mView.hideProgressDialog();
        }
    }
    @Override
    public void onErrorParkingSites(String message) {
        mView.showMessageOnFailure(message);
        mView.hideProgressDialog();
    }

    public boolean validateInputs(String... inputs) {
        for(String input : inputs){
            if(input.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onLoadedDirectionFinder(List<Route> routes) {
        this.routes = routes;
        showPathBetweenTwoLocations(routes);
        showListRoutes(routes, this.mParkingSites);
        mView.hideProgressDialog();
    }

    @Override
    public void onErrorDirectionFinder(String message) {
        mView.showMessageOnFailure(message);
        mView.hideProgressDialog();
    }

    public void showParkingSites(List<ParkingSite> parkingSites, int zoomIndex) {
        for (ParkingSite parkingSite : parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking))
                    .position(latLng).title(parkingSite.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to Mountain View
                    .zoom(zoomIndex)                    // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void showPathBetweenTwoLocations(List<Route> routes){
        if(routes.size() == 0 || routes.isEmpty()) {
            mView.showMessageOnEmpty("There is no routes");
        }
        else{
            int index = 0;
            List<LatLng> directRoute = null;
            for(Route route: routes) {
                String routePolyLine = route.getOverviewPolyline().getPoints();
                List<LatLng> list = decode(routePolyLine);
                if(index == 0){
                    directRoute = list;
                }
                else {
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(15)
                            .color(Color.GRAY)
                            .geodesic(true));
                }
                index++;
            }
            //draw direct route
            mMap.addPolyline(new PolylineOptions()
                    .addAll(directRoute)
                    .width(15)
                    .color(Color.BLUE)
                    .geodesic(true));

        }
    }

    public void showListRoutes(List<Route> routes, List<ParkingSite> parkingSites){
        List<ParkingSite> nearParkingSites = new ArrayList<>();
        for(ParkingSite parkingSite: parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            int index = 0;
            for (Route route: routes) {
                if(index == 0)
                    {
                    String routePolyLine = route.getOverviewPolyline().getPoints();
                    List<LatLng> list = decode(routePolyLine);
                    if(PolyUtil.isLocationOnPath(latLng,list,true,100)){
                        nearParkingSites.add(parkingSite);
                    }
                }
                index++;
            }
        }
        showParkingSites(nearParkingSites,7);
    }

    public void chosenRoute(LatLng latLng){
        mMap.clear();
        List<Route> selectedRoute = new ArrayList<>();
        List<LatLng> listLatLngSelectedRoute = null;
        for (Route route : routes) {
                String routePolyLine = route.getOverviewPolyline().getPoints();
                List<LatLng> listLatLng = decode(routePolyLine);
                //if we have more than one route choose first route
                if( selectedRoute.isEmpty() && PolyUtil.isLocationOnEdge(latLng,listLatLng,true,1000)){
                    selectedRoute.add(route);
                    listLatLngSelectedRoute = listLatLng;
                } else {
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(listLatLng)
                            .width(15)
                            .color(Color.GRAY)
                            .geodesic(true));
                }
            }

        if(!selectedRoute.isEmpty() && !mParkingSites.isEmpty() && mParkingSites!=null) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(listLatLngSelectedRoute)
                    .width(15)
                    .color(Color.BLUE)
                    .geodesic(true));
            showListRoutes(selectedRoute, mParkingSites);
        }
        else {
            mView.showMessageOnEmpty("Please click on Polyline");
        }
    }
}
