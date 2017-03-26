package com.example.gresa_pc.openprject.model;

/**
 * Created by Gresa-PC on 3/26/2017.
 */

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}

