package com.example.gresa_pc.openprject;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.presenter.MapsPresenter;
import com.example.gresa_pc.openprject.ui.view.MapsView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {
    private static final String GOOGLE_API_KEY = "AIzaSyBMXirpILQ3x7CXtTKsA8-H8JQ4ngQmXo4";
    private GoogleMap mMap;
    @BindView(R.id.etFrom)
    EditText etFrom;
    @BindView(R.id.etTo)
    EditText etTo;

    @Inject
    MapsPresenter mMapPresenter;
//    @Inject ProgressDialog mProgressDialog;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        mParkingSitesPresenter.onResumeParkingSites(this,mMap);
    }

    @OnClick(R.id.btn_searchLocations)
    public void btn_searchLocations(){
        boolean validInputs = mMapPresenter.validateInputs(etFrom.getText().toString(),etTo.getText().toString());
        if(validInputs){
            mMap.clear();
            mMapPresenter.onResumeDirectionFinder(this,mMap,etFrom.getText().toString(), etTo.getText().toString(), GOOGLE_API_KEY, true);
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
        mMapPresenter.onResumeParkingSites(this, mMap);
        //on Map Click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMapPresenter.chosenRoute(latLng);
            }
        });
    }

    @Override
    public void showMessageOnFailure(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageOnEmpty(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog(){
        mProgressDialog.show();
    }

    @Override
    public  void hideProgressDialog(){
        mProgressDialog.dismiss();
    }

    @Override
    public void finish(){
        super.finish();
        ((App) getApplication()).releaseAppComponent();
    }
}
