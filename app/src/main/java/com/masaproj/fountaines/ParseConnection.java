package com.masaproj.fountaines;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Roman on 12.01.2016.
 */
public class ParseConnection extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "7p8acFJHE16HKIp1ROUWtdd56VN7b2mP5abssZyb", "YdxssfmHOdb22UYED10FbN8xZJ3Ccz7r7eehjwUX");
    }
}
