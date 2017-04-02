package com.example.gresa_pc.openprject.ui.view;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Gresa-PC on 4/1/2017.
 */

public interface MapsView {
        void showMessageOnFailure(String message);
        void showProgressDialog();
        void hideProgressDialog();
        void showMessageOnEmpty(String message);
}
