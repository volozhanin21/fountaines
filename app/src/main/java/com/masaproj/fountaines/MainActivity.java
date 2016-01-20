package com.masaproj.fountaines;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.model.Marker;
import com.masaproj.fountaines.activity.FountaineDetailsActivity;
import com.masaproj.fountaines.db.ParseQueries;
import com.masaproj.fountaines.entity.Fountaine;
import com.masaproj.fountaines.listener.AdapterFountaineInfoWindowImpl;
import com.masaproj.fountaines.listener.InfoWindowClickListenerImpl;
import com.masaproj.fountaines.listener.MarkerClickListenerImpl;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ParseQueries.loadAllFountaines();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLocationListener = new MyLocationListener();

        googleMap.setMyLocationEnabled(true);
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            double latitude = location.getLatitude();
            // Getting longitude of the current location
            double longitude = location.getLongitude();
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            CommonData.getInstance().setUserLocation(location);
        }

        // Add a marker in Sydney and move the camera
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(CommonData.getInstance().getPolyline()!=null){
                    CommonData.getInstance().getPolyline().remove();
                }
            }
        });
        mMap.setOnMarkerClickListener(new MarkerClickListenerImpl(mMap,getApplicationContext()));
        mMap.setInfoWindowAdapter(new AdapterFountaineInfoWindowImpl(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(new InfoWindowClickListenerImpl(getApplicationContext()));
        for(Fountaine fountaine:CommonData.getInstance().getAllFountaines()){
            LatLng fountaineLocation = new LatLng(fountaine.getGeoPosition().getLatitude(),
                    fountaine.getGeoPosition().getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(fountaineLocation);
            markerOptions.snippet(fountaine.getObjectId());
            mMap.addMarker(markerOptions);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, myLocationListener);
    }


    private MyLocationListener myLocationListener;
    private class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();
            // Getting longitude of the current location
            double longitude = location.getLongitude();
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // Showing the current location in Google Map
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            CommonData.getInstance().setUserLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
