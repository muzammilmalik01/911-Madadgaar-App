package com.alpha1.A911madadgaar.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class admin_view_report extends AppCompatActivity {

    FirebaseFirestore database;
    TextView reportnotxt,incidenttxt,statusTxt,timeTxt,dateTxt,descriptiontxt,addresstxt,cityTxt,imageTxt;
    Button accept,reject;
    CheckBox warnuser;
    ImageView proofImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_report);
        database = FirebaseFirestore.getInstance();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading the report.");
//        progressDialog.show();
        reportnotxt = findViewById(R.id.reportnotxt);
        incidenttxt = findViewById(R.id.incidenttxt);
        statusTxt = findViewById(R.id.statusTxt);
        timeTxt = findViewById(R.id.timeTxt);
        dateTxt = findViewById(R.id.dateTxt);
        descriptiontxt = findViewById(R.id.descriptiontxt);
        addresstxt = findViewById(R.id.addresstxt);
        cityTxt = findViewById(R.id.cityTxt);
        imageTxt = findViewById(R.id.imageTxt);

        accept = findViewById(R.id.acceptButton);
        reject = findViewById(R.id.rejectButton);
        warnuser = findViewById(R.id.checkBox);

        proofImage = findViewById(R.id.proofImage);


        reportFormAdminView.setReportID(getIntent().getStringExtra("reportno"));
        if(!reportFormAdminView.getReportID().isEmpty())
        {
            database.collection("reports")
                    .document(reportFormAdminView.getReportID())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists())
                            {
                                reportFormAdminView.setAddress(documentSnapshot.getString("address"));
                                reportFormAdminView.setCity(documentSnapshot.getString("city"));
                                reportFormAdminView.setDate(documentSnapshot.getString("date"));
                                reportFormAdminView.setDescription(documentSnapshot.getString("description"));
                                reportFormAdminView.setImage(documentSnapshot.getString("image"));
                                reportFormAdminView.setIncident(documentSnapshot.getString("incident"));
                                reportFormAdminView.setStatus(documentSnapshot.getString("status"));
                                reportFormAdminView.setTime(documentSnapshot.getString("time"));
                                reportFormAdminView.setUsercnic(documentSnapshot.getString("usercnic"));


                                reportnotxt.setText(reportFormAdminView.getReportID());
                                incidenttxt.setText(reportFormAdminView.getIncident());
                                statusTxt.setText(reportFormAdminView.getStatus());
                                timeTxt.setText(reportFormAdminView.getTime());
                                dateTxt.setText(reportFormAdminView.getDate());
                                descriptiontxt.setText(reportFormAdminView.getDescription());
                                addresstxt.setText(reportFormAdminView.getAddress());
                                cityTxt.setText(reportFormAdminView.getCity());

                                if(reportFormAdminView.getImage().equals("code0"))
                                {
                                    imageTxt.setText("Not Attatched");
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
                                Toast.makeText(admin_view_report.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warnuser.isChecked())
                {
                    Toast.makeText(admin_view_report.this, "User cannot be warned when the report is accepted.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(admin_view_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent gotoReportList = new Intent(admin_view_report.this,reports_list.class);
                                    startActivity(gotoReportList);
                                    finish();
                                }
                            });
                }


            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFormAdminView.setStatus("Rejected");
                reportFormAdminView.setComment("Dear Citizen, the report has been rejected because it has been found inauthentic or same incident has already been reported.");
                if(warnuser.isChecked())
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
                                            Toast.makeText(admin_view_report.this, warn, Toast.LENGTH_SHORT).show();
                                            Map<String, Object> updatewarning = new HashMap<>();
                                            updatewarning.put("WARNING",warn);
                                            database.collection("users")
                                                    .document(reportFormAdminView.getUsercnic())
                                                    .update(updatewarning)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(admin_view_report.this, "User has been warned successfully.", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(admin_view_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                            Intent gotoReportList = new Intent(admin_view_report.this,reports_list.class);
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
                                    Toast.makeText(admin_view_report.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent gotoReportList = new Intent(admin_view_report.this,reports_list.class);
                                    startActivity(gotoReportList);
                                    finish();
                                }
                            });
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Start a new activity when the back button is pressed
        Intent intent = new Intent(this, reports_list.class);
        startActivity(intent);
        finish();
    }
}