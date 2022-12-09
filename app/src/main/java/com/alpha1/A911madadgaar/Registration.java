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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    Button register, test;
    EditText CNIC,PHONE,NAME;
    FirebaseFirestore db;
    String cnic,phone,name;
    Integer count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        register = findViewById(R.id.REGBTN); //register Button on Activity Screen.
        CNIC = findViewById(R.id.CNICET); // CNIC Edittext on Activity Screen.
        PHONE = findViewById(R.id.PHONEET); // PHONE Edittext on Activity Screen.
        NAME = findViewById(R.id.NAMEET); // NAME Edittext on Activity Screen.
        test = findViewById(R.id.testBtn);



        db = FirebaseFirestore.getInstance();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testintent = new Intent(Registration.this,OTP_Send.class);
                testintent.putExtra("PHONE","3164750937");
                testintent.putExtra("CNIC","123456789");
                testintent.putExtra("NAME","TEST USER");
                startActivity(testintent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                cnic = CNIC.getText().toString().trim(); //getting CNIC from editTexts.
                phone = PHONE.getText().toString().trim(); //getting PHONE from editTexts.
                name = NAME.getText().toString().trim(); //getting NAME from editTexts.

                if(cnic.isEmpty() || phone.isEmpty() || name.isEmpty()) //check for NULL editTexts.
                {
                    if(cnic.isEmpty())
                    {
                        Toast.makeText(Registration.this, "Please enter your CNIC.", Toast.LENGTH_SHORT).show();
                    }
                    else if (phone.isEmpty())
                    {
                        Toast.makeText(Registration.this, "Please enter your Phone Number.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Registration.this, "Please enter your Name.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    db.collection("users")
                            .document(cnic)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        DocumentSnapshot documentSnapshot =  task.getResult();
                                        if(documentSnapshot != null && documentSnapshot.exists()) //checking is CNIC is already registered.
                                        {
                                            Toast.makeText(Registration.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                                        }
                                        else // if user is not registered
                                        {
                                            db.collection("users")//checking if the provided number is registered to any other user.
                                                    .whereEqualTo("PHONE", phone)// will return all users who have used the provided number.
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    count++; //counting number of users who have used that number.
                                                                }
                                                                if(count == 0) //Provided number is not already Registered. (Registering the New User.)
                                                                {

                                                                    Intent OTPVERIFICATIONSTART = new Intent(Registration.this, OTP_Send.class);
                                                                    OTPVERIFICATIONSTART.putExtra("PHONE",phone);
                                                                    OTPVERIFICATIONSTART.putExtra("CNIC",cnic); // Passing the user's data to OTP Send Screen.
                                                                    OTPVERIFICATIONSTART.putExtra("NAME",name);
                                                                    startActivity(OTPVERIFICATIONSTART);
                                                                }
                                                                else //Provided number is already registered.
                                                                {
                                                                    Toast.makeText(Registration.this, "Number Already Exists. Exists.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            else //error while getting the task done.
                                                            {
                                                                Toast.makeText(Registration.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                    else//if document snapshot does not exists
                                    {

                                        Toast.makeText(Registration.this, "Error 404, Document Snapshot not found.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}