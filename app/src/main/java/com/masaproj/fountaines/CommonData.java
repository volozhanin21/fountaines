package com.masaproj.fountaines;

/**
 * Created by Roman on 19.01.2016.
 */
import android.location.Location;

import com.google.android.gms.maps.model.Polyline;
import com.masaproj.fountaines.entity.Fountaine;

import java.util.ArrayList;

/**
 * Created by Roman on 25.12.2015.
 */
public class CommonData {
    private ArrayList<Fountaine> fountaines;
    private Location userLocation;
    private Polyline polyline;

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    private static CommonData ourInstance = new CommonData();

    public static CommonData getInstance() {
        return ourInstance;
    }

    private CommonData() {
        fountaines = new ArrayList<>();
    }

    public ArrayList<Fountaine> getAllFountaines(){
        return fountaines;
    }

    public Fountaine findFountaineById(String fountaineId){
        for(Fountaine fountaine:fountaines){
            if(fountaine.getObjectId().equals(fountaineId))
                return fountaine;
        }
        return null;
    }
}
