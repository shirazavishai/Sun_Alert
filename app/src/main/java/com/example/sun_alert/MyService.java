package com.example.sun_alert;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final long ALERT_DELAY = 3000;
    private long lastAlert = 0;

    @Override
    public void onCreate() {
        Log.d("sunAlert Service:","OnCreate. Thread: "+ Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int StartId){
        Log.d("sunAlert Service:","OnStartCommand. Thread: "+ Thread.currentThread().getName());
        Signals.getInstance().makeToast("service start",this);
        begin();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void begin() {
        Log.d("sunAlert Service:","begin. Thread: "+ Thread.currentThread().getName());
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            float val = event.values[0];
            if(System.currentTimeMillis() > lastAlert + ALERT_DELAY) {
                if (val > 500.0f) {
                    Log.d("sunAlert Service:", "tooHigh");
                    Signals.getInstance().audio(R.raw.alert);
                    Signals.getInstance().audio(R.raw.alert);
                    makeBroadcastAction(val, getResources().getColor(R.color.red, null));
                    lastAlert = System.currentTimeMillis();
                }
                else if(val > 400.0f) {
                    Log.d("sunAlert Service:", "high");
                    Signals.getInstance().audio(R.raw.alert);
                    makeBroadcastAction(val, getResources().getColor(R.color.orange, null));
                    lastAlert = System.currentTimeMillis();
                }
                else if(val > 300.0f) {
                    Log.d("sunAlert Service:", "medium");
                    makeBroadcastAction(val, getResources().getColor(R.color.yellow, null));
                    lastAlert = System.currentTimeMillis();
                } else{
                    Log.d("sunAlert Service:", "low");
                    makeBroadcastAction(val, getResources().getColor(R.color.bright, null));
                    lastAlert = System.currentTimeMillis();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void makeBroadcastAction(float val, int colorID) {
        Log.d("sunAlert Service:", "Make Broadcast. Thread: " + Thread.currentThread().getName());
        Intent intent = new Intent(LightReceiver.ACTION_LIGHT);
        intent.putExtra(LightReceiver.GET_EXTRA_LIGHT,val);
        intent.putExtra(LightReceiver.GET_EXTRA_COLOR,colorID);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("sunAlert Service:", "STOP. Thread: " + Thread.currentThread().getName());
        super.onDestroy();
    }

}
