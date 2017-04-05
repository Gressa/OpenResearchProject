package com.example.gresa_pc.openprject;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.presenter.MapsPresenter;
import com.example.gresa_pc.openprject.ui.view.MapsView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {
    private GoogleMap mMap;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.etFrom)
    EditText etFrom;
    @BindView(R.id.etTo)
    EditText etTo;
    @BindView(R.id.button2)
    Button rootDetail1;
    @BindView(R.id.button3)
    Button rootDetail2;
    @BindView(R.id.button4)
    Button rootDetail3;
    @Inject
    MapsPresenter mMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        hideButtons();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.btn_searchLocations)
    public void btn_searchLocations(){
        boolean validInputs = mMapPresenter.validateInputs(etFrom.getText().toString(),etTo.getText().toString());
        if(validInputs){
            mMap.clear();
            hideButtons();
            mMapPresenter.onResumeDirectionFinder(this,mMap,etFrom.getText().toString(), etTo.getText().toString(), getResources().getString(R.string.google_maps_key), true);
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.fillTextFields),Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.button2)
    public void button2(){
        resetButtonsColor();
        rootDetail1.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
        mMapPresenter.selectRoute(rootDetail1.getText().toString());
    }

    @OnClick(R.id.button3)
    public void button3(){
        resetButtonsColor();
        rootDetail2.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
        mMapPresenter.selectRoute(rootDetail2.getText().toString());
    }

    @OnClick(R.id.button4)
    public void button4(){
        resetButtonsColor();
        rootDetail3.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
        mMapPresenter.selectRoute(rootDetail3.getText().toString());
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
    }

    @Override
    public void showMessageOnFailure(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyRouteMessage(){
        Toast.makeText(this,getString(R.string.noRoute),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyParkingMessage(){
        Toast.makeText(this,getString(R.string.noParking),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetails(String[] details) {
            resetButtonsColor();
            if (details.length == 1) {
                rootDetail1.setVisibility(View.VISIBLE);
                rootDetail1.setText(details[0]);
                rootDetail1.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
            }
            else if(details.length == 2){
                rootDetail1.setVisibility(View.VISIBLE);
                rootDetail1.setText(details[0]);
                rootDetail1.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
                rootDetail2.setVisibility(View.VISIBLE);
                rootDetail2.setText(details[1]);
            }
            else if(details.length == 3){
                rootDetail1.setVisibility(View.VISIBLE);
                rootDetail1.setText(details[0]);
                rootDetail1.setBackgroundColor(ContextCompat.getColor(this,R.color.blue_light));
                rootDetail2.setVisibility(View.VISIBLE);
                rootDetail2.setText(details[1]);
                rootDetail3.setVisibility(View.VISIBLE);
                rootDetail3.setText(details[2]);
            }
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

    private void resetButtonsColor(){
        rootDetail1.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        rootDetail2.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        rootDetail3.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
    }

    private void hideButtons(){
        rootDetail1.setVisibility(View.GONE);
        rootDetail2.setVisibility(View.GONE);
        rootDetail3.setVisibility(View.GONE);
    }
}
