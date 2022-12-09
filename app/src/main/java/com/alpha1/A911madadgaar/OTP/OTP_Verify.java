package com.alpha1.A911madadgaar.OTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alpha1.A911madadgaar.LoginActivity;
import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class OTP_Verify extends AppCompatActivity {

    EditText userotp;
    String VerificationID,Userotp, CNIC,PHONE,NAME;
    Button verifybtn;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        CNIC = getIntent().getStringExtra("CNIC");
        PHONE = getIntent().getStringExtra("PHONE");
        NAME = getIntent().getStringExtra("NAME");
        db = FirebaseFirestore.getInstance();

        userotp = findViewById(R.id.userOTPET);//EditText where user enters OTP.
        VerificationID = getIntent().getStringExtra("vid");//Verification ID last Activity.
        verifybtn = findViewById(R.id.verify_btn);// click to verify the OTP.

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Userotp = userotp.getText().toString().trim();//OTP from EditText
                if(Userotp.length() == 6)
                {
                    if(VerificationID != null)
                    {
                        verifycode(Userotp);
                    }
                    else
                    {
                        Toast.makeText(OTP_Verify.this, "Error (No Verification ID)", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(OTP_Verify.this, "Please enter 6 Digit OTP Code.", Toast.LENGTH_SHORT).show();
                }
            }

            private void verifycode(String Code) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID,Code);
                signinbyCredentials(credential);
            }

            private void signinbyCredentials(PhoneAuthCredential credential) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        if(CNIC != null && PHONE!= null && NAME!= null)
                                        {
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("CNIC", CNIC);
                                            user.put("PHONE", PHONE);
                                            user.put("NAME", NAME);
                                            db.collection("users").document(CNIC).set(user);
                                            Toast.makeText(OTP_Verify.this, "The user has been registered Successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(OTP_Verify.this, LoginActivity.class));
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(OTP_Verify.this, "There was an error getting User Data from Intent.", Toast.LENGTH_SHORT).show();
                                            Intent intent =  new Intent(OTP_Verify.this,Registration.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(OTP_Verify.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });}
        });
    }
}