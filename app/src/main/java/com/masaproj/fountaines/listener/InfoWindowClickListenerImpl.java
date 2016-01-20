package com.masaproj.fountaines.listener;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.masaproj.fountaines.CommonData;
import com.masaproj.fountaines.activity.FountaineDetailsActivity;
import com.masaproj.fountaines.entity.Fountaine;

/**
 * Created by Roman on 19.01.2016.
 */
public class InfoWindowClickListenerImpl implements GoogleMap.OnInfoWindowClickListener{

    private Context context;
    public InfoWindowClickListenerImpl(Context context){
        this.context = context;
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        final String fountaineId = marker.getSnippet();
        Fountaine fountaine = CommonData.getInstance().findFountaineById(fountaineId);
        if(fountaine!=null){
            Intent intent = new Intent(context,FountaineDetailsActivity.class);
            intent.putExtra("fountaineId",fountaineId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
