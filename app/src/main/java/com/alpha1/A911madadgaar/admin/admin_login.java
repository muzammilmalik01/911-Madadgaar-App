package com.alpha1.A911madadgaar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_login extends AppCompatActivity {

    Button loginBtn;
    EditText adminIDEdit, adminPINEdit;
    FirebaseFirestore database;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        database = FirebaseFirestore.getInstance();
        loginBtn = findViewById(R.id.loginBtn);
        adminIDEdit = findViewById(R.id.adminID);
        adminPINEdit = findViewById(R.id.PIN);

        sharedPreferences = getSharedPreferences("ADMINID", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("STATUS",false))
        {
            //goto Admin Panel.
            Intent gotoAdminPanel = new Intent(admin_login.this,adminpanel.class);
            startActivity(gotoAdminPanel);
            finish();
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminid = adminIDEdit.getText().toString().trim();
                String adminpin = adminPINEdit.getText().toString().trim();

                if(!adminid.isEmpty() && !adminpin.isEmpty())
                {
                    database.collection("admin")
                            .document(adminid)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists())
                                    {
                                        String dbpin = documentSnapshot.getString("adminpin");
                                        if(dbpin.equals(adminpin))
                                        {
                                            sharedPreferences = getApplicationContext().getSharedPreferences("ADMINID",MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            myEdit.putString("ID",adminid);
                                            myEdit.putString("PIN",adminpin);
                                            myEdit.putBoolean("STATUS",true);
                                            myEdit.apply();
                                            Intent gotoAdminPanel = new Intent(admin_login.this,adminpanel.class);
                                            startActivity(gotoAdminPanel);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(admin_login.this, "Wrong Password Entered.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(admin_login.this, "Admin ID not found.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(admin_login.this, "Please enter the credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}