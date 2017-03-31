package com.example.gresa_pc.openprject.presenter;

import android.graphics.Color;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.ui.view.DirectionFinderContract;
import com.example.gresa_pc.openprject.ui.view.IDirectionFinderEngine;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
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
        mIDirectionFinderEngine.getDirectionRoutes(this,origin,destination,key, alternatives);
    }

    @Override
    public void onShowDrivingRouteBetweenTwoLocations(GoogleMap map, List<Route> routes) {
        for(Route route: routes) {
            String routePolyLine = route.getOverviewPolyline().getPoints();
            List<LatLng> list = decode(routePolyLine);
            map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(Color.BLUE)
                    .geodesic(true));
        }
    }

    @Override
    public void onLoaded(List<Route> routes) {
        mView.showListRoutes(routes);
    }

    @Override
    public void onError(String message) {
        mView.showMessageOnFailure(message);
    }
}

