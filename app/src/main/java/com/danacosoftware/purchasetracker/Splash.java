package com.danacosoftware.purchasetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Daniel Phillips on 8/3/2016.
 */
public class Splash extends Activity {
    Thread timer;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView SplashImage = (ImageView)findViewById(R.id.splash_imageView);
        Random rn = new Random();
        int num = rn.nextInt(100);
        Log.d("random",num+"");
        if ((num % 2) == 0){
            SplashImage.setImageResource(R.mipmap.splash);
        }else{
            SplashImage.setImageResource(R.mipmap.new_splash);
        }
        timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent startIntent = new Intent(Splash.this, MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                }

            }
        };
        timer.start();
    }
}
