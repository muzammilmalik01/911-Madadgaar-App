package com.alpha1.A911madadgaar.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.admin.reports_list;

public class confirmation_screen extends AppCompatActivity {

    TextView reportnotxt,usernametxt,usercnictxt;
    Button closeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);


        reportnotxt = findViewById(R.id.reportnotxt);
        usernametxt = findViewById(R.id.usernametxt);
        usercnictxt = findViewById(R.id.usercnictxt);
        closeBtn = findViewById(R.id.closeBtn);

        reportnotxt.setText(reportForm.getReportID());
        usernametxt.setText(reportForm.getUsername());
        usercnictxt.setText(reportForm.getUsercnic());

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportForm.setIncident("Not Selected");
                reportForm.setReportID(null);
                reportForm.setCity(null);
                reportForm.setAddress(null);
                reportForm.setLat(0);
                reportForm.setLon(0);
                reportForm.setDate(null);
                reportForm.setTime(null);
                reportForm.setUsercnic(userClass.getCnic());
                reportForm.setUsername(userClass.getFullname());
                reportForm.setUserphone(userClass.getPhone());
                reportForm.setDescription(null);
                reportForm.setStatus("Pending");

                Intent gotoHomescreen = new Intent(confirmation_screen.this, HomeScreen.class);
                startActivity(gotoHomescreen);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        reportForm.setIncident("Not Selected");
        reportForm.setReportID(null);
        reportForm.setCity(null);
        reportForm.setAddress(null);
        reportForm.setLat(0);
        reportForm.setLon(0);
        reportForm.setDate(null);
        reportForm.setTime(null);
        reportForm.setUsercnic(userClass.getCnic());
        reportForm.setUsername(userClass.getFullname());
        reportForm.setUserphone(userClass.getPhone());
        reportForm.setDescription(null);
        reportForm.setStatus("Pending");
        Intent gotoHomescreen = new Intent(confirmation_screen.this, HomeScreen.class);
        startActivity(gotoHomescreen);
        finish();
    }
}