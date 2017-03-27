package com.example.gresa_pc.openprject;

import android.content.Context;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.presenter.DirectionFinderEngine;
import com.example.gresa_pc.openprject.ui.view.DirectionFinderView;
import com.example.gresa_pc.openprject.model.Location;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.presenter.ParkingSitesEngine;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.google.android.gms.wearable.DataMap.TAG;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ParkingSitesView ,DirectionFinderView {
    private GoogleMap mMap;
    @Inject
    ParkingSitesEngine engine;
    @Inject
    DirectionFinderEngine directionFinderEngine;
    @BindView(R.id.etFrom)
    EditText etFrom;
    @BindView(R.id.etTo)
    EditText etTo;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        engine.setView(this);
        engine.getParkings();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.btn_search)
    public void btn_search(){
        Log.d(TAG,"Button clicked");
        if(etFrom.getText().toString().isEmpty() || etTo.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please fill text fields",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            LatLng from = getLocationFromAddress(this, etFrom.getText().toString());
            originMarkers.add(mMap.addMarker(new MarkerOptions().position(from)));
            LatLng to = getLocationFromAddress(this, etTo.getText().toString());
            mMap.clear();
            try {
                directionFinderEngine.setView(this);
                directionFinderEngine.setOrigin(etFrom.getText().toString());
                directionFinderEngine.setDestination(etTo.getText().toString());
                directionFinderEngine.execute();
                //new DirectionFinderEngine(etFrom.getText().toString(), etTo.getText().toString()).execute();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
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
    }

    @Override
    public void onParkingSitesListLoadSuccess(List<ParkingSite> parkingSites) {
        drawParkingSites(parkingSites);
    }

    @Override
    public void onParkingSitesListLoadFailure() {
        Log.d(TAG, "mainFailure");
    }


    public void drawParkingSites(List<ParkingSite> parkingSites) {
        for (ParkingSite parkingSite:parkingSites) {
            Location location = parkingSite.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(parkingSite.getTitle()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to Mountain View
                    .zoom(14)                    // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                //    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                 //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
