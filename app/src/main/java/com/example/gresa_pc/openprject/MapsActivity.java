package com.example.gresa_pc.openprject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.presenter.ParkingSitesEngine;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import static com.google.android.gms.wearable.DataMap.TAG;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ParkingSitesView {

    @Inject
    ParkingSitesEngine engine;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
       // App.getAppComponent().inject(this);
        ((App) getApplication()).getAppComponent().inject(this);
        engine.setView(this);
        engine.getParkings();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onParkingSitesListLoadSuccess(List<ParkingSite> parkingSites) {
        for (ParkingSite parkingSite:parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "+parkingSite.getTitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onParkingSitesListLoadFailure() {
        Log.d(TAG, "mainFailure");
    }
}
