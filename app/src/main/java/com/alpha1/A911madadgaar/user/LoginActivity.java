package com.alpha1.A911madadgaar.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha1.A911madadgaar.OTP.OTP_Send;
import com.alpha1.A911madadgaar.admin.admin_login;
import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.user.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText cnic, phone;
    String CNIC,PHONE;
    FirebaseFirestore Database;
    ImageView adminBtn;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Database = FirebaseFirestore.getInstance();
        login = findViewById(R.id.loginBtn);
        cnic = findViewById(R.id.cnicET);
        phone = findViewById(R.id.phoneET);
        register = findViewById(R.id.regBtn);
        adminBtn = findViewById(R.id.adminBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CNIC = cnic.getText().toString().trim();
                PHONE = phone.getText().toString().trim();
                if(CNIC.length() !=13 || PHONE.length() !=10)
                {
                    if(CNIC.length() !=13)
                    {
                        Toast.makeText(LoginActivity.this, "Enter your 13 digit CNIC.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Enter 10 digit phone number.\nFormat: 3xxxxxxxxx ", Toast.LENGTH_SHORT).show();
                    }

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
                                            if(documentSnapshot.exists())
                                            {
                                                String phonefromdb = documentSnapshot.getString("PHONE");
                                                if(phonefromdb.equals(PHONE))
                                                {
                                                    Intent OTPVERIFICATION = new Intent(LoginActivity.this, OTP_Send.class);
                                                    OTPVERIFICATION.putExtra("MODE","LOGIN");
                                                    OTPVERIFICATION.putExtra("PHONE",PHONE);
                                                    OTPVERIFICATION.putExtra("CNIC",CNIC);
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoReg = new Intent(LoginActivity.this, Registration.class);
                startActivity(gotoReg);
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAdminLogin = new Intent(LoginActivity.this,admin_login.class);
                startActivity(gotoAdminLogin);
            }
        });

    }
}