package com.masaproj.fountaines.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.masaproj.fountaines.CommonData;
import com.masaproj.fountaines.R;
import com.masaproj.fountaines.entity.Fountaine;

import java.io.File;

public class FountaineDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fountaine_details);
        ImageView imageView = (ImageView)findViewById(R.id.activity_fountaine_details_picture);
        TextView fountaineLongitude = (TextView)findViewById(R.id.activity_fountaine_details_longitude);
        TextView fountaineLatitude = (TextView)findViewById(R.id.activity_fountaine_details_latitude);
        RatingBar fountaineRating = (RatingBar) findViewById(R.id.activity_fountaine_details_ratingBar);
        Intent intent = getIntent();
        String fountaineId = intent.getStringExtra("fountaineId");
        Fountaine fountaine = CommonData.getInstance().findFountaineById(fountaineId);
        if(fountaine!=null){
            File picture = fountaine.getPicture();
            Bitmap myBitmap = BitmapFactory.decodeFile(picture.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
            fountaineLongitude.setText("lon:" + fountaine.getGeoPosition().getLongitude());
            fountaineLatitude.setText("lat:" + fountaine.getGeoPosition().getLatitude());
            fountaineRating.setRating(fountaine.getRating());
        }
    }
}
