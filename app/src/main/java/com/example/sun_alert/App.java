package com.example.sun_alert;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Signals.init(this);
    }

}
