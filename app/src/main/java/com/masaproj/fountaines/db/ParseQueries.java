package com.masaproj.fountaines.db;

import com.masaproj.fountaines.CommonData;
import com.masaproj.fountaines.entity.Fountaine;
import com.masaproj.fountaines.entity.FountaineGeoPosition;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Roman on 19.01.2016.
 */
public class ParseQueries {

    public static void loadAllFountaines(){
        CommonData.getInstance().getAllFountaines().clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Fountain");
        Fountaine fountaine;
        try {
            for(ParseObject object:query.find()){
                fountaine = new Fountaine();
                fountaine.setObjectId(object.getObjectId());

                ParseGeoPoint parseGeoPoint = object.getParseGeoPoint("location");
                fountaine.setGeoPosition(new FountaineGeoPosition(parseGeoPoint.getLongitude(), parseGeoPoint.getLatitude()));

                ParseFile picture = object.getParseFile("picture");
                fountaine.setPicture(picture.getFile());

                fountaine.setRating(object.getInt("rating"));
                fountaine.setChecked(object.getBoolean("checked"));
                List<Object> comments = object.getList("comments");
                if(comments!=null){
                    fountaine.setComments((String[]) (comments.toArray()));
                }
                CommonData.getInstance().getAllFountaines().add(fountaine);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
