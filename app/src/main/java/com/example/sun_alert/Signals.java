package com.example.sun_alert;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Signals {
    private static Signals instance;
    private final Context context;

    public static Signals getInstance() {
        return instance;
    }

    private Signals(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new Signals(context);
        }
    }

    public void audio(int id) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context,id);
        mediaPlayer.start();
    }

    public void makeToast(String str,Context context){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}

