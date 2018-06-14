package com.example.pietruszka.myapplicationmapactivi;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Pietruszka on 22.03.2018.
 */

public class stacje {
    String nazwa;
    Double lat;
    Double lon;
    String dostRow;

    public stacje(String nazwa, Double lat, Double lon, String dostRow) {
        this.nazwa = nazwa;
        this.lat = lat;
        this.lon = lon;
        this.dostRow = dostRow;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getDostRow() {
        return dostRow;
    }

}
