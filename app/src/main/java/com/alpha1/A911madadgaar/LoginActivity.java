package com.alpha1.A911madadgaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alpha1.A911madadgaar.OTP.OTP_Send;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText cnic, phone;
    String CNIC,PHONE;
    FirebaseFirestore Database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Database = FirebaseFirestore.getInstance();
        login = findViewById(R.id.loginBtn);
        cnic = findViewById(R.id.cnicET);
        phone = findViewById(R.id.phoneET);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CNIC = cnic.getText().toString().trim();
                PHONE = phone.getText().toString().trim();
                if(CNIC.isEmpty() && PHONE.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Details.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Database.collection("users")
                            .document(CNIC)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        DocumentSnapshot documentSnapshot =  task.getResult();
                                        if(documentSnapshot != null && documentSnapshot.exists()) //checking is CNIC is already registered.
                                        {
                                            Toast.makeText(LoginActivity.this, "User Found", Toast.LENGTH_SHORT).show();
                                            if(documentSnapshot.exists())
                                            {
                                                String phonefromdb = documentSnapshot.getString("PHONE");
                                                if(phonefromdb.equals(PHONE))
                                                {
                                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent OTPVERIFICATION = new Intent(LoginActivity.this, OTP_Send.class);
                                                    OTPVERIFICATION.putExtra("MODE","LOGIN");
                                                    OTPVERIFICATION.putExtra("PHONE",PHONE);
                                                    startActivity(OTPVERIFICATION);
                                                }
                                                else
                                                {
                                                    Toast.makeText(LoginActivity.this, "CNIC Matched, Phone Didn't", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this, "Document Snapshot Doesnt Exists", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }
                                    else
                                    {

                                    }
                                }
                            });
                }
            }
        });

    }
}