package com.example.pocketdevs.pocketcloset.entity;

import java.io.Serializable;

/**
 * Class to hold temperature parameters
 */

public class Temperature implements Serializable {

    private int temp;
    private String city;
    private double lat;
    private double lon;

    public Temperature(){

    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
