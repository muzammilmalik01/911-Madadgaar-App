package com.alpha1.A911madadgaar.OTP;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alpha1.A911madadgaar.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP_Send extends AppCompatActivity {

    Button sendotp;
    String PHONE;
    TextView phoneTxt;
    FirebaseAuth mAuth;
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);

        sendotp = findViewById(R.id.sendOTPbtn);
        PHONE = getIntent().getStringExtra("PHONE");
        phoneTxt = findViewById(R.id.PHONETXT);
        phoneTxt.setText("Send OTP to verify this number: +92"+PHONE+"?");
        loading = findViewById(R.id.loadinAnim);
        loading.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance(); 
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendotp.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                sendverificationcode(PHONE);
            }

            private void sendverificationcode(String phoneNumber) //Sending OTP to user's Phone.
            {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(OTP_Send.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    Toast.makeText(OTP_Send.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                    sendotp.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    sendotp.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(OTP_Send.this, "Verification Failed.", Toast.LENGTH_SHORT).show();
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        Toast.makeText(OTP_Send.this, "In-Valid Request", Toast.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        Toast.makeText(OTP_Send.this, "Quota Exceded", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCodeSent(@NonNull String s,
                        @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the  user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    loading.setVisibility(View.INVISIBLE);
                    sendotp.setVisibility(View.VISIBLE);
                    Toast.makeText(OTP_Send.this, "The OTP has been sent.", Toast.LENGTH_SHORT).show();
                    Intent verify = new Intent(OTP_Send.this,OTP_Verify.class);
                    verify.putExtra("vid",s);
                    verify.putExtra("PHONE",PHONE); //User's Phone
                    verify.putExtra("CNIC",getIntent().getStringExtra("CNIC")); //User's Cnic
                    verify.putExtra("NAME",getIntent().getStringExtra("NAME")); // User's Name
                    verify.putExtra("MODE",getIntent().getStringExtra("MODE"));
                    startActivity(verify);
                    finish();
                }
            };
        });
        
    }
}