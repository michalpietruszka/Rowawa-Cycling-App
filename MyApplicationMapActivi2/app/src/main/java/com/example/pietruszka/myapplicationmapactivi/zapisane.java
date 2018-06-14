package com.example.pietruszka.myapplicationmapactivi;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pietruszka on 26.03.2018.
 */


public class zapisane{
    LatLng startWsp;
    LatLng koniecWsp;
    String start;
    String koniec;
    String czas;
    String dystans;

    public zapisane(LatLng startWsp, LatLng koniecWsp, String start, String koniec, String czas, String dystans) {
        this.startWsp = startWsp;
        this.koniecWsp = koniecWsp;
        this.start = start;
        this.koniec = koniec;
        this.czas = czas;
        this.dystans = dystans;
    }

    public LatLng getStartWsp() {
        return startWsp;
    }

    public void setStartWsp(LatLng startWsp) {
        this.startWsp = startWsp;
    }

    public LatLng getKoniecWsp() {
        return koniecWsp;
    }

    public void setKoniecWsp(LatLng koniecWsp) {
        this.koniecWsp = koniecWsp;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getKoniec() {
        return koniec;
    }

    public void setKoniec(String koniec) {
        this.koniec = koniec;
    }

    public String getCzas() {
        return czas;
    }

    public void setCzas(String czas) {
        this.czas = czas;
    }

    public String getDystans() {
        return dystans;
    }

    public void setDystans(String dystans) {
        this.dystans = dystans;
    }
}

