package com.masaproj.fountaines.listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.masaproj.fountaines.CommonData;
import com.masaproj.fountaines.entity.Fountaine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 19.01.2016.
 */
public class MarkerClickListenerImpl implements GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private Context context;

    public MarkerClickListenerImpl(GoogleMap mMap,Context context) {
        this.mMap = mMap;
        this.context = context;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        // Get URLs for the Directions API
        Location userLocation = CommonData.getInstance().getUserLocation();
        double latitude = userLocation.getLatitude();
        double longitude = userLocation.getLongitude();
        LatLng start = new LatLng(latitude, longitude);

        String fountaineId = marker.getSnippet();
        Fountaine fountaine = CommonData.getInstance().findFountaineById(fountaineId);
        LatLng dest = new LatLng (fountaine.getGeoPosition().getLatitude(),fountaine.getGeoPosition().getLongitude());
        String url = getDirectionsUrl(start, dest);
        ConnectAsyncTask task = new ConnectAsyncTask(url);
        task.execute();
        return true;
    }

    public void drawPath(String  result) {
        if(CommonData.getInstance().getPolyline()!=null){
            CommonData.getInstance().getPolyline().remove();
        }
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            CommonData.getInstance().setPolyline(mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true)
            ));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    // Building the url to the Directions API web service
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(origin.latitude);
        urlString.append(",");
        urlString
                .append(origin.longitude);
        urlString.append("&destination=");// to
        urlString
                .append(dest.latitude);
        urlString.append(",");
        urlString.append(dest.longitude);
        urlString.append("&sensor=false&mode=walking&alternatives=true");
        //urlString.append("&key=AIzaSyAfEK7eZ_kFF7TPN1x9J0RVgmbMqzaHrlM");
        return urlString.toString();
    }
    static class JSONParser {

        static InputStream is = null;
        static JSONObject jObj = null;
        static String json = "";
        // constructor
        public JSONParser() {
        }
        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }

    private class ConnectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        public ConnectAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Fetching route, Please wait...");
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
            Log.e("ConnectAsyncTask","onPreExecute");
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            Log.e("ConnectAsyncTask","doInBackground");
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.hide();
            if(result!=null){
                Log.e("ConnectAsyncTask","onPostExecute");
                drawPath(result);
            }
        }
    }

}
