package com.alpha1.A911madadgaar.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity {
    CardView reportTile,searchReport;
    FirebaseFirestore Database;
    String cnic;
    TextView personDetailsTxt;
    ImageView logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        logoutBtn = findViewById(R.id.logoutBtn);
        SharedPreferences sh = getApplicationContext().getSharedPreferences("STATUS", Context.MODE_PRIVATE);
        cnic = sh.getString("CNIC",null);
        personDetailsTxt = findViewById(R.id.person_details_txt);
        Database = FirebaseFirestore.getInstance();
        Database.collection("users")
                .document(cnic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot =  task.getResult();
                            if(documentSnapshot.exists())
                            {
                                userClass.setCnic(documentSnapshot.getString("CNIC"));
                                userClass.setFullname(documentSnapshot.getString("NAME"));
                                userClass.setPhone(documentSnapshot.getString("PHONE"));
                                userClass.setWarning(documentSnapshot.getString("WARNING"));

                                personDetailsTxt.setText(userClass.getFullname()+"\n"+ userClass.getCnic());

                            }
                            else
                            {
                                Intent gotoLogin = new Intent(HomeScreen.this, LoginActivity.class);
                                SharedPreferences.Editor update = sh.edit();
                                update.putString("CNIC",null);
                                update.putBoolean("LOGGEDIN",false);
                                update.apply();
                                startActivity(gotoLogin);
                                finish();
                            }
                        }
                    }
                });

        reportTile = findViewById(R.id.reportTile);
        searchReport = findViewById(R.id.searchTile);
        reportTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReportSubmission = new Intent(HomeScreen.this, reportSubmission.class);
                startActivity(gotoReportSubmission);
                finish();
            }
        });
        searchReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReportSubmission = new Intent(HomeScreen.this, user_search_report.class);
                startActivity(gotoReportSubmission);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("STATUS",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putBoolean("LOGGEDIN",false);
                myEdit.putString("CNIC",null);
                myEdit.apply();
                Intent gotologin = new Intent(HomeScreen.this,LoginActivity.class);
                startActivity(gotologin);
                finish();
                userClass.setCnic(null);
                userClass.setFullname(null);
                userClass.setPhone(null);
                FirebaseAuth.getInstance().signOut();
            }
        });
        //TODO: onPause and onStop and onDestroy not implemented for Saved Preferences.
    }
}