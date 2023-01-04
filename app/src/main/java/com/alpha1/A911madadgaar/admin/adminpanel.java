package com.alpha1.A911madadgaar.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.user.HomeScreen;
import com.alpha1.A911madadgaar.user.LoginActivity;
import com.alpha1.A911madadgaar.user.userClass;

public class adminpanel extends AppCompatActivity {

    CardView viewReportTitle,searchReportTile;
    ImageView adlogoutBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        searchReportTile = findViewById(R.id.searchReportTile);
        viewReportTitle = findViewById(R.id.viewReports);
        adlogoutBtn = findViewById(R.id.adlogoutBtn);
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
                Intent gotoSearchReport = new Intent(adminpanel.this,admin_search_report.class);
                startActivity(gotoSearchReport);
                finish();
            }
        });
        adlogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ADMINID",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putBoolean("STATUS",false);
                myEdit.putString("ID",null);
                myEdit.putString("PIN",null);
                myEdit.apply();
                Intent gotologin = new Intent(adminpanel.this, admin_login.class);
                startActivity(gotologin);
                finish();
            }
        });
    }
}