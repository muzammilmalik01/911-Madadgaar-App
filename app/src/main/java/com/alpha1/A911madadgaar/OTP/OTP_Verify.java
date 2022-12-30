package com.alpha1.A911madadgaar.OTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alpha1.A911madadgaar.HomeScreen;
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
    String MODE;
    LottieAnimationView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        MODE = getIntent().getStringExtra("MODE");
        CNIC = getIntent().getStringExtra("CNIC");
        PHONE = getIntent().getStringExtra("PHONE");
        NAME = getIntent().getStringExtra("NAME");
        db = FirebaseFirestore.getInstance();


        loading = findViewById(R.id.loadinAnim);
        loading.setVisibility(View.INVISIBLE);
        userotp = findViewById(R.id.userOTPET);//EditText where user enters OTP.
        VerificationID = getIntent().getStringExtra("vid");//Verification ID last Activity.
        verifybtn = findViewById(R.id.verify_btn);// click to verify the OTP.

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Userotp = userotp.getText().toString().trim();//OTP from EditText
                verifybtn.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                if(Userotp.length() == 6)
                {
                    if(VerificationID != null)
                    {
                        verifycode(Userotp);
                    }
                    else
                    {
                        verifybtn.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(OTP_Verify.this, "Error (No Verification ID)", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    verifybtn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
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
                                        verifybtn.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.INVISIBLE);
                                        if(MODE.equals("LOGIN"))
                                        {
                                            //Logged In.
                                            Intent Homescreen = new Intent(OTP_Verify.this, HomeScreen.class);
                                            // Saving Logged In Status and CNIC in Shared Preferences.
                                            SharedPreferences sharedPreferences = getSharedPreferences("STATUS",MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            myEdit.putBoolean("LOGGEDIN",true);
                                            myEdit.putString("CNIC",CNIC);
                                            myEdit.apply();
                                            startActivity(Homescreen);
                                            finish();
                                        }
                                        else if (MODE.equals("REG"))
                                        {
                                            //Reg Done
                                            if(CNIC != null && PHONE!= null && NAME!= null)
                                            {
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("CNIC", CNIC);
                                                user.put("PHONE", PHONE);
                                                user.put("NAME", NAME);
                                                user.put("WARNING", "0");
                                                db.collection("users").document(CNIC).set(user);
                                                Toast.makeText(OTP_Verify.this, "The user has been registered Successfully.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(OTP_Verify.this, HomeScreen.class));
                                                // Saving Logged In Status and CNIC in Shared Preferences.
                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("STATUS",MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                myEdit.putBoolean("LOGGEDIN",true);
                                                myEdit.putString("CNIC",CNIC);
                                                myEdit.apply();
                                                //finishing activity.
                                                finish();
                                            }
                                            else
                                            {
                                                verifybtn.setVisibility(View.VISIBLE);
                                                loading.setVisibility(View.INVISIBLE);
                                                Toast.makeText(OTP_Verify.this, "There was an error getting User Data from Intent.", Toast.LENGTH_SHORT).show();
                                                Intent intent =  new Intent(OTP_Verify.this,Registration.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                    }
                                    else
                                    {
                                        verifybtn.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.INVISIBLE);
                                        Toast.makeText(OTP_Verify.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });}
        });
    }
}