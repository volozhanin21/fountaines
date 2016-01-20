package com.masaproj.fountaines.entity;

/**
 * Created by Roman on 19.01.2016.
 */
public class FountaineGeoPosition {

    private double longitude;
    private double latitude;

    public FountaineGeoPosition(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
