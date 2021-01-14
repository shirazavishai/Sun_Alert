package com.example.sun_alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LightReceiver extends BroadcastReceiver {
    private final Light_Callback light_callback;
    public static final String GET_EXTRA_LIGHT = "GET_EXTRA_LIGHT";
    public static final String GET_EXTRA_COLOR = "GET_EXTRA_COLOR";
    public static final String ACTION_LIGHT = "ACTION_LIGHT";

    public LightReceiver(Light_Callback light_callback){
        this.light_callback=light_callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sunAlert LightReceiver:", "on Receive. Thread: " + Thread.currentThread().getName());
        float lightVal = intent.getFloatExtra(GET_EXTRA_LIGHT,0);
        int colorID = intent.getIntExtra(GET_EXTRA_COLOR,0);

        if(light_callback != null){
            light_callback.light(lightVal,colorID);
        }
    }

    public interface Light_Callback{
        void light(float value,int colorId);
    }
}


