package com.example.pietruszka.myapplicationmapactivi;

/**
 * Created by Pietruszka on 26.03.2018.
 */

////klasa obiektów - możesz dodawać jakie chcesz atrybuty
public class historia {
    String data;
    String start;
    String koniec;
    String czas;
    String dystans;
    String rok;
    String miesiac;
    String dzien;
    String godzina;
    public historia(String data, String start, String koniec, String czas, String dystans) {
        this.data = data;
        this.start = start;
        this.koniec = koniec;
        this.czas = czas;
        this.dystans = dystans;
    }

    public String getdata() {
        return data;
    }

    public void setdata(String data) {
        this.data = data;
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

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getMiesiac() {
        return miesiac;
    }

    public void setMiesiac(String miesiac) {
        this.miesiac = miesiac;
    }

    public String getDzien() {
        return dzien;
    }

    public void setDzien(String dzien) { this.dzien = dzien; }

    public String getGodzina() {
        return godzina;
    }

    public void setGodzina(String godzina) {
        this.godzina = godzina;
    }

    public String getDystans() {
        return dystans;
    }

    public void setDystans(String dystans) {
        this.dystans = dystans;
    }
}

