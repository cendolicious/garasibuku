package com.example.dickyeka.garasibuku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        new android.os.Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
                Boolean status = sharedPreferences.getBoolean("status", false);
                Intent intent = null;
                if (status)
                    intent = new Intent(SplashScreen.this, NavigationDrawer.class);
                else
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        },2500);
    }
}
