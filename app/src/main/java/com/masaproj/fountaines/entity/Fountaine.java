package com.masaproj.fountaines.entity;


import android.location.Location;

import java.io.File;

/**
 * Created by Roman on 25.12.2015.
 */
public class Fountaine {
    private String objectId;
    private File picture;
    private int rating;
    private boolean checked;
    private String[] comments;
    private FountaineGeoPosition geoPosition;

    public FountaineGeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(FountaineGeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }
}
