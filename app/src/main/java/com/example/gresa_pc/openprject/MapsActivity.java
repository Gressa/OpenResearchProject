package com.example.gresa_pc.openprject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.model.ParkingSite;
import com.example.gresa_pc.openprject.model.Route;
import com.example.gresa_pc.openprject.presenter.ParkingSitesPresenter;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesContract;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.google.android.gms.wearable.DataMap.TAG;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ParkingSitesContract.View{
    private static final String GOOGLE_API_KEY = "AIzaSyBMXirpILQ3x7CXtTKsA8-H8JQ4ngQmXo4";
    private GoogleMap mMap;
    @BindView(R.id.etFrom)
    EditText etFrom;
    @BindView(R.id.etTo)
    EditText etTo;

    @Inject
    ParkingSitesPresenter mParkingSitesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mParkingSitesPresenter.onResume(this);
    }

    @OnClick(R.id.btn_search)
    public void btn_search(){
        Log.d(TAG,"Button clicked");
        if(etFrom.getText().toString().isEmpty() || etTo.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please fill text fields",Toast.LENGTH_SHORT).show();
        }
        else {
            mMap.clear();
          //  mDirectionFinderPresenter.onResume(this,etFrom.getText().toString(), etTo.getText().toString(),GOOGLE_API_KEY, true);
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
    public void showAllParkingSites(List<ParkingSite> parkingSites) {
        mParkingSitesPresenter.onDrawParkingSites(parkingSites,mMap);
    }

    @Override
    public void showMessageOnFailure(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageOnEmptyParkingSites(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
