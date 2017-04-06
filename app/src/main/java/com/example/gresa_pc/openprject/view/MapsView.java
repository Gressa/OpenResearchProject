package com.example.gresa_pc.openprject.view;

/**
 * Created by Gresa-PC on 4/1/2017.
 */

public interface MapsView {
    void showMessageOnFailure(String message);

    void showProgressDialog();

    void hideProgressDialog();

    void showDetails(String[] detail);

    void showEmptyRouteMessage();

    void showEmptyParkingMessage();
}
