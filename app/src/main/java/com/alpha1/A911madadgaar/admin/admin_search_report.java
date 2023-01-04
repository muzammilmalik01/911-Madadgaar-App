package com.alpha1.A911madadgaar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.user.user_search_report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class admin_search_report extends AppCompatActivity {

    LottieAnimationView loading;
    EditText searchBar;
    ImageView searchBtn,proofImage;
    LinearLayout reportInfoSection,acceptRejectSection;
    Button adacceptButton,adrejectButton;
    CheckBox adcheckBox;
    FirebaseFirestore database;

    TextView incidenttxt,statusTxt,timeTxt,dateTxt,descriptiontxt,addresstxt,cityTxt,imageTxt,usreportnotxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_report);

        database = FirebaseFirestore.getInstance();
        loading = findViewById(R.id.adloadinAnim);
        searchBar = findViewById(R.id.adsearchBar);
        searchBtn = findViewById(R.id.adsearchBtn);
        reportInfoSection = findViewById(R.id.adreportInfoSection);
        acceptRejectSection = findViewById(R.id.acceptRejectSection);
        proofImage = findViewById(R.id.adproofImage);

        usreportnotxt = findViewById(R.id.adreportnotxt);
        incidenttxt = findViewById(R.id.adincidenttxt);
        statusTxt = findViewById(R.id.adstatusTxt);
        timeTxt =  findViewById(R.id.adtimeTxt);
        dateTxt = findViewById(R.id.addateTxt);
        descriptiontxt = findViewById(R.id.addescriptiontxt);
        addresstxt = findViewById(R.id.adaddresstxt);
        cityTxt = findViewById(R.id.adcityTxt);
        imageTxt = findViewById(R.id.adimageTxt);
        adacceptButton = findViewById(R.id.adacceptButton);
        adrejectButton = findViewById(R.id.adrejectButton);
        adcheckBox = findViewById(R.id.adcheckBox);

        loading.setVisibility(View.INVISIBLE);
        reportInfoSection.setVisibility(View.INVISIBLE);
        acceptRejectSection.setVisibility(View.INVISIBLE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportNo = searchBar.getText().toString().trim();
                loading.setVisibility(View.VISIBLE);
                if(!reportNo.isEmpty())
                {
                    database.collection("reports")
                            .document(reportNo)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if(documentSnapshot.exists())
                                        {
                                            reportFormAdminView.setAddress(documentSnapshot.getString("address"));
                                            reportFormAdminView.setCity(documentSnapshot.getString("city"));
                                            reportFormAdminView.setDate(documentSnapshot.getString("date"));
                                            reportFormAdminView.setDescription(documentSnapshot.getString("description"));
                                            reportFormAdminView.setIncident(documentSnapshot.getString("incident"));
                                            reportFormAdminView.setStatus(documentSnapshot.getString("status"));
                                            reportFormAdminView.setTime(documentSnapshot.getString("time"));
                                            reportFormAdminView.setUsercnic(documentSnapshot.getString("usercnic"));
                                            reportFormAdminView.setReportID(documentSnapshot.getString("reportno"));

                                            reportInfoSection.setVisibility(View.VISIBLE);
                                            loading.setVisibility(View.INVISIBLE);

                                            usreportnotxt.setText(reportFormAdminView.getReportID());
                                            addresstxt.setText(reportFormAdminView.getAddress());
                                            cityTxt.setText(reportFormAdminView.getCity());
                                            dateTxt.setText(reportFormAdminView.getDate());
                                            descriptiontxt.setText(reportFormAdminView.getDescription());
                                            incidenttxt.setText(reportFormAdminView.getIncident());
                                            statusTxt.setText(reportFormAdminView.getStatus());
                                            timeTxt.setText(reportFormAdminView.getTime());

                                            if(reportFormAdminView.getStatus().equals("Pending"))
                                            {
                                                acceptRejectSection.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                //commentSection.setVisibility(View.VISIBLE);
                                                acceptRejectSection.setVisibility(View.INVISIBLE);
                                            }

                                            if(documentSnapshot.getString("image").equals("code0"))
                                            {
                                                imageTxt.setText("Not Attached");
                                                proofImage.setImageResource(R.drawable.picture);
                                            }
                                            else
                                            {
                                                imageTxt.setText("Attached");
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageRef = storage.getReference();
                                                StorageReference pathReference = storageRef.child("reports").child(reportFormAdminView.getReportID());
                                                pathReference.getBytes(9000*9000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                                                        proofImage.setImageBitmap(bitmap);
                                                    }
                                                });
                                            }
                                        }
                                        else
                                        {
                                            loading.setVisibility(View.INVISIBLE);
                                            reportInfoSection.setVisibility(View.INVISIBLE);
                                            Toast.makeText(admin_search_report.this, "Report not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        loading.setVisibility(View.INVISIBLE);
                                        reportInfoSection.setVisibility(View.INVISIBLE);
                                        Toast.makeText(admin_search_report.this, "Error fetching data.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(admin_search_report.this, "Please enter the report number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adacceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adcheckBox.isChecked())
                {
                    Toast.makeText(admin_search_report.this, "User cannot be warned when the report is accepted.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    reportFormAdminView.setStatus("Accepted");
                    reportFormAdminView.setComment("Dear Citizen! Your report has been accepted and forwarded to the relevant authorities.");
                    Map<String, Object> update = new HashMap<>();
                    update.put("status",reportFormAdminView.getStatus());
                    update.put("comment",reportFormAdminView.getComment());
                    database.collection("reports")
                            .document(reportFormAdminView.getReportID())
                            .update(update)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(admin_search_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent gotoReportList = new Intent(admin_search_report.this,adminpanel.class);
                                    startActivity(gotoReportList);
                                    finish();
                                }
                            });
                }


            }
        });
        adrejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFormAdminView.setStatus("Rejected");
                reportFormAdminView.setComment("Dear Citizen, the report has been rejected because it has been found inauthentic or same incident has already been reported.");
                if(adcheckBox.isChecked())
                {
                    database.collection("users")
                            .document(reportFormAdminView.getUsercnic())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if(documentSnapshot.exists())
                                        {
                                            String warn = documentSnapshot.getString("WARNING");
                                            int warning = Integer.valueOf(warn);
                                            warning++;

                                            warn = String.valueOf(warning);
                                            Toast.makeText(admin_search_report.this, warn, Toast.LENGTH_SHORT).show();
                                            Map<String, Object> updatewarning = new HashMap<>();
                                            updatewarning.put("WARNING",warn);
                                            database.collection("users")
                                                    .document(reportFormAdminView.getUsercnic())
                                                    .update(updatewarning)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(admin_search_report.this, "User has been warned successfully.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            Map<String, Object> update = new HashMap<>();
                                            update.put("status",reportFormAdminView.getStatus());
                                            update.put("comment",reportFormAdminView.getComment());
                                            database.collection("reports")
                                                    .document(reportFormAdminView.getReportID())
                                                    .update(update)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(admin_search_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                            Intent gotoReportList = new Intent(admin_search_report.this,adminpanel.class);
                                                            startActivity(gotoReportList);
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }

                                }
                            });
                }
                else
                {
                    Map<String, Object> update = new HashMap<>();
                    update.put("status",reportFormAdminView.getStatus());
                    update.put("comment",reportFormAdminView.getComment());
                    database.collection("reports")
                            .document(reportFormAdminView.getReportID())
                            .update(update)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(admin_search_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent gotoReportList = new Intent(admin_search_report.this,adminpanel.class);
                                    startActivity(gotoReportList);
                                    finish();
                                }
                            });
                }
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent goback = new Intent(admin_search_report.this,adminpanel.class);
        startActivity(goback);
        finish();
    }
}