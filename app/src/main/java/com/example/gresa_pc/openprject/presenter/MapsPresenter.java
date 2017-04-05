package com.example.gresa_pc.openprject.presenter;

import android.graphics.Color;
import com.example.gresa_pc.openprject.R;
import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.engine.DirectionFinderContract;
import com.example.gresa_pc.openprject.view.MapsView;
import com.example.gresa_pc.openprject.engine.ParkingSitesContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.google.maps.android.PolyUtil.decode;

/**
 * Created by Gresa-PC on 4/1/2017.
 */

public class MapsPresenter implements DirectionFinderContract.LoadListenerDirectionFinder, ParkingSitesContract.LoadListenerParkingSites{
    private final Integer ZOOM_INDEX_HIGH = 15;
    private final Integer ZOOM_INDEX_LOW = 7;
    private final Integer POLYLINE_WIDTH = 15;
    private final Integer TOLERANCE = 100;
    private final ParkingSitesContract mIParkingSiteEngine;
    private MapsView mView;
    private final DirectionFinderContract mIDirectionFinderEngine;
    private List<ParkingSite> mParkingSites;
    private List<Route> routes;
    private GoogleMap mMap;
    private Map<Route,String> detailRoute;
    private String[] detailButton;

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
            showParkingSites(mParkingSites,ZOOM_INDEX_HIGH);
            mView.hideProgressDialog();
        }
        else {
            mView.showEmptyParkingMessage();
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
        List<ParkingSite> nearParkingSites = showListRoutes(routes, this.mParkingSites);
        showParkingSites(nearParkingSites,ZOOM_INDEX_LOW);
        mView.showDetails(detailButton);
        mView.hideProgressDialog();
    }

    @Override
    public void onErrorDirectionFinder(String message) {
        mView.showMessageOnFailure(message);
        mView.hideProgressDialog();
    }

    private void showParkingSites(List<ParkingSite> parkingSites, int zoomIndex) {
        for (ParkingSite parkingSite : parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking))
                    .position(latLng).title(parkingSite.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to Mountain View
                    .zoom(zoomIndex)            // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void showPathBetweenTwoLocations(List<Route> routes){
        detailRoute = new HashMap<>();
        detailButton = new String[routes.size()];
        if(routes.size() == 0 || routes.isEmpty()) {
            mView.showEmptyRouteMessage();
        }
        else{
            int index = 0;
            List<LatLng> fastestRoute = new ArrayList<>();
            for(Route route: routes) {
                String routePolyLine = route.getOverviewPolyline().getPoints();
                List<LatLng> list = decode(routePolyLine);
                String duration = route.getLegs().get(0).getDuration().getText();
                String distance = route.getLegs().get(0).getDistance().getText();
                duration = parseDetails(duration);
                String detail = duration+" "+distance;
                detailButton[index]= detail;
                detailRoute.put(route,detail);
                if(index == 0){
                    fastestRoute = list;
                }
                else {
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(POLYLINE_WIDTH)
                            .color(Color.GRAY)
                            .geodesic(true));
                }
                index++;
            }
            //draw fastest route
            mMap.addPolyline(new PolylineOptions()
                    .addAll(fastestRoute)
                    .width(POLYLINE_WIDTH)
                    .color(Color.BLUE)
                    .geodesic(true));
        }
    }

    private List<ParkingSite> showListRoutes(List<Route> routes, List<ParkingSite> parkingSites){
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
                    if(PolyUtil.isLocationOnPath(latLng,list,true,TOLERANCE)){
                        nearParkingSites.add(parkingSite);
                    }
                }
                index++;
            }
        }
        return nearParkingSites;
    }



    public void selectRoute(String detail){
        mMap.clear();
        List<Route> selectedRoute = new ArrayList<>();
        for(Map.Entry<Route,String> route: detailRoute.entrySet()){
            if(route.getValue().equals(detail)){
                selectedRoute.add(route.getKey());
            }
        }

        for (Route route : routes) {
            if (route != selectedRoute.get(0)) {
                String routePolyLine = route.getOverviewPolyline().getPoints();
                List<LatLng> listLatLng = decode(routePolyLine);
                mMap.addPolyline(new PolylineOptions()
                        .addAll(listLatLng)
                        .width(POLYLINE_WIDTH)
                        .color(Color.GRAY)
                        .geodesic(true));
            }
        }
        String routePolyLine = selectedRoute.get(0).getOverviewPolyline().getPoints();
        List<LatLng> listLatLngSelectedRoute = decode(routePolyLine);
        if(!mParkingSites.isEmpty() && mParkingSites!=null) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(listLatLngSelectedRoute)
                    .width(POLYLINE_WIDTH)
                    .color(Color.BLUE)
                    .geodesic(true));
            List<ParkingSite> nearParkingSites = showListRoutes(selectedRoute, mParkingSites);
            showParkingSites(nearParkingSites,ZOOM_INDEX_LOW);
        }
    }


    private String parseDetails(String duration){
        String[] parseDuration = duration.split(" ");
        if(parseDuration.length == 2){
            parseDuration[0] = "00:"+parseDuration[0]+"h";
        }
        else {
            if(parseDuration[1].equals("day")){
                parseDuration[0] = parseDuration[0]+"d "+parseDuration[2]+"h";
            }
            else {
                if (parseDuration[2].length() == 1) {
                    parseDuration[2] = "0" + parseDuration[2];
                }
                parseDuration[0] = parseDuration[0] + ":" + parseDuration[2] + "h";
            }
        }
        return parseDuration[0];
    }
}
