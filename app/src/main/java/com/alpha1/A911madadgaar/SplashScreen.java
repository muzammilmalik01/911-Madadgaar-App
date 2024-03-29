package com.alpha1.A911madadgaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.alpha1.A911madadgaar.user.HomeScreen;
import com.alpha1.A911madadgaar.user.LoginActivity;
import com.alpha1.A911madadgaar.user.Registration;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sh;
    public static final String STATUS = "STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sh = getSharedPreferences(STATUS, Context.MODE_PRIVATE);
                Boolean LOGINSTATUS = sh.getBoolean("LOGGEDIN", false);
                String CNIC = sh.getString("CNIC", null);
                if(LOGINSTATUS && CNIC!=null)
                {
                    Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, 2000);
    }
}