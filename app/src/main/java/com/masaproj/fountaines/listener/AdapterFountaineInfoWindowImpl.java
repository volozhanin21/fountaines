package com.masaproj.fountaines.listener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.masaproj.fountaines.CommonData;
import com.masaproj.fountaines.R;
import com.masaproj.fountaines.entity.Fountaine;

import java.io.File;

/**
 * Created by Roman on 19.01.2016.
 */
public class AdapterFountaineInfoWindowImpl implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public AdapterFountaineInfoWindowImpl(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fountaine_info_window, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.fountaine_info_window_picture);
        TextView fountaineLongitude = (TextView) view.findViewById(R.id.fountaine_info_window_longitude);
        TextView fountaineLatitude = (TextView) view.findViewById(R.id.fountaine_info_window_latitude);
        final String fountaineId = marker.getSnippet();
        Fountaine fountaine = CommonData.getInstance().findFountaineById(fountaineId);
        if (fountaine != null) {
            File picture = fountaine.getPicture();
            Bitmap myBitmap = BitmapFactory.decodeFile(picture.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
            fountaineLongitude.setText("lon:" + fountaine.getGeoPosition().getLongitude());
            fountaineLatitude.setText("lat:" + fountaine.getGeoPosition().getLatitude());

        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
