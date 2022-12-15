package com.alpha1.A911madadgaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {
    TextView cnicSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        SharedPreferences sh = getApplicationContext().getSharedPreferences("STATUS", Context.MODE_PRIVATE);
        Boolean LOGINSTATUS = sh.getBoolean("LOGGEDIN", false);
        String CNIC = sh.getString("CNIC", "");
        cnicSP = findViewById(R.id.cnicSP);
        cnicSP.setText("CNIC: " + CNIC+"\nLogged In: "+LOGINSTATUS);
        //TODO: onPause and onStop and onDestroy not implemented for Saved Preferences.
    }
}