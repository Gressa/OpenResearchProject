package com.example.gresa_pc.openprject.model;

/**
 * Created by Gresa-PC on 3/23/2017.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingSiteLocation {

    @SerializedName("parking_sites")
    @Expose
    private List<ParkingSite> parkingSites = null;

    public List<ParkingSite> getParkingSites() {
        return parkingSites;
    }

    public void setParkingSites(List<ParkingSite> parkingSites) {
        this.parkingSites = parkingSites;
    }

}
