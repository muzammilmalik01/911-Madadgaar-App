package com.alpha1.A911madadgaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity {
    LinearLayout reportTile,searchReport;
    FirebaseFirestore Database;
    String cnic;
    TextView personDetailsTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
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
                                user.setCnic(documentSnapshot.getString("CNIC"));
                                user.setFullname(documentSnapshot.getString("NAME"));
                                user.setPhone(documentSnapshot.getString("PHONE"));
                                user.setWarning(documentSnapshot.getString("WARNING"));

                                personDetailsTxt.setText(user.getFullname()+"\n"+user.getCnic()+"\n0"+user.getPhone());

                            }
                            else
                            {
                                Intent gotoLogin = new Intent(HomeScreen.this,LoginActivity.class);
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
                Intent gotoReportSubmission = new Intent(HomeScreen.this,reportSubmission.class);
                startActivity(gotoReportSubmission);
            }
        });
        searchReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeScreen.this, "Coming Soon !😊", Toast.LENGTH_SHORT).show();
            }
        });
        //TODO: onPause and onStop and onDestroy not implemented for Saved Preferences.
    }
}