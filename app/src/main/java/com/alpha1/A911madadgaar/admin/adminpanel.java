package com.alpha1.A911madadgaar.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;

public class adminpanel extends AppCompatActivity {

    LinearLayout viewReportTitle,searchReportTile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        searchReportTile = findViewById(R.id.searchReportTile);
        viewReportTitle = findViewById(R.id.viewReports);
        viewReportTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReportLists = new Intent(adminpanel.this,reports_list.class);
                startActivity(gotoReportLists);
            }
        });
        searchReportTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(adminpanel.this, "Coming Soon! 😊", Toast.LENGTH_SHORT).show();
            }
        });
    }
}