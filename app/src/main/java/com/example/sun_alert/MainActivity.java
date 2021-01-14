package com.example.sun_alert;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView main_LBL_info;
    private TextView main_LBL_value;
    private ImageView main_IMG_sun;
    private ImageView main_IMG_cloud1;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("sunAlert Main:","onCreate");

        findViews();

        Intent intent = new Intent(this,MyService.class);
        startService(intent);

        LightReceiver lightReceiver = new LightReceiver(new LightReceiver.Light_Callback() {
            @Override
            public void light(float value,int colorId) {
                String string = "";
                if(value>=500){
                    string="Sun is burn";
                }else if(value>=400){
                    string="Sun is warm";
                }else if(value>=300){
                    string="Sun is medium";
                }else{
                    string="That's perfect!";
                }
                main_LBL_info.setText(string);
                main_LBL_value.setText(value+"");
                setSunColor(colorId);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LightReceiver.ACTION_LIGHT);
        registerReceiver(lightReceiver, intentFilter);
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(main_IMG_sun.getWidth(), main_IMG_sun.getHeight());
            lp.setMargins(random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
            main_IMG_cloud1.setLayoutParams(lp);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(main_IMG_sun.getWidth(), main_IMG_sun.getHeight());
            lp.setMargins(random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
            main_IMG_cloud1.setLayoutParams(lp);
            animation.setRepeatCount(Animation.INFINITE);
        }
    }

    private void setSunColor(int colorId) {
        main_IMG_sun.setColorFilter(colorId);
    }

    @Override
    protected void onResume() {
        Log.d("sunAlert Main:", "onResume");
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            throw new AssertionError();
        else actionBar.hide();
    }

    private void findViews() {
        main_LBL_info = findViewById(R.id.main_LBL_info);
        main_LBL_value = findViewById(R.id.main_LBL_value);
        main_IMG_sun = findViewById(R.id.main_IMG_sun);
        main_IMG_cloud1 = findViewById(R.id.main_IMG_cloud1);
        ImageView main_IMG_background = findViewById(R.id.main_IMG_background);
        Glide
                .with(this)
                .load(R.drawable.sunny_day)
                .centerCrop()
                .into(main_IMG_background);

        TranslateAnimation animation= setAnimate();
        main_IMG_cloud1.startAnimation(animation);
    }

    private TranslateAnimation setAnimate(){
        TranslateAnimation animation = new TranslateAnimation(random.nextInt(1000), random.nextInt(1000), random.nextInt(1000), random.nextInt(1000));
        animation.setDuration(6000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        return animation;
    }

    @Override
    protected void onDestroy() {
        Log.d("sunAlert Main:", "on destroy");
        super.onDestroy();
        stopService(new Intent(MainActivity.this, MyService.class));
    }
}