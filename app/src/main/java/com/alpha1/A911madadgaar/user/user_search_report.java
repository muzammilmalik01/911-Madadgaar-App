package com.alpha1.A911madadgaar.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alpha1.A911madadgaar.R;
import com.alpha1.A911madadgaar.admin.reportFormAdminView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class user_search_report extends AppCompatActivity {

    LottieAnimationView loading;
    EditText searchBar;
    ImageView searchBtn,proofImage;
    LinearLayout reportInfoSection,commentSection;
    FirebaseFirestore database;

    TextView incidenttxt,statusTxt,timeTxt,dateTxt,usernameTxt,usercnicTxt,descriptiontxt,addresstxt,cityTxt,imageTxt,commentTxt,usreportnotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_report);

        removedata();

        database = FirebaseFirestore.getInstance();
        loading = findViewById(R.id.loadinAnim);
        searchBar = findViewById(R.id.searchBar);
        searchBtn = findViewById(R.id.searchBtn);
        reportInfoSection = findViewById(R.id.reportInfoSection);
        commentSection = findViewById(R.id.commentSection);
        proofImage = findViewById(R.id.usproofImage);

        usreportnotxt = findViewById(R.id.usreportnotxt);
        incidenttxt = findViewById(R.id.usincidenttxt);
        statusTxt = findViewById(R.id.usstatusTxt);
        timeTxt =  findViewById(R.id.ustimeTxt);
        dateTxt = findViewById(R.id.usdateTxt);
        usernameTxt = findViewById(R.id.ususernameTxt);
        usercnicTxt = findViewById(R.id.ususercnicTxt);
        descriptiontxt = findViewById(R.id.usdescriptiontxt);
        addresstxt = findViewById(R.id.usaddresstxt);
        cityTxt = findViewById(R.id.uscityTxt);
        imageTxt = findViewById(R.id.usimageTxt);
        commentTxt = findViewById(R.id.uscommentTxt);

        loading.setVisibility(View.INVISIBLE);
        reportInfoSection.setVisibility(View.INVISIBLE);

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
                                            reportForm.setAddress(documentSnapshot.getString("address"));
                                            reportForm.setCity(documentSnapshot.getString("city"));
                                            reportForm.setDate(documentSnapshot.getString("date"));
                                            reportForm.setDescription(documentSnapshot.getString("description"));
                                            reportForm.setIncident(documentSnapshot.getString("incident"));
                                            reportForm.setStatus(documentSnapshot.getString("status"));
                                            reportForm.setTime(documentSnapshot.getString("time"));
                                            reportForm.setUsercnic(documentSnapshot.getString("usercnic"));
                                            reportForm.setUserphone(documentSnapshot.getString("userphone"));
                                            reportForm.setUsername(documentSnapshot.getString("username"));
                                            reportForm.setReportID(documentSnapshot.getString("reportno"));

                                            reportInfoSection.setVisibility(View.VISIBLE);
                                            loading.setVisibility(View.INVISIBLE);

                                            usreportnotxt.setText(reportForm.getReportID());
                                            addresstxt.setText(reportForm.getAddress());
                                            cityTxt.setText(reportForm.getCity());
                                            dateTxt.setText(reportForm.getDate());
                                            descriptiontxt.setText(reportForm.getDescription());
                                            incidenttxt.setText(reportForm.getIncident());
                                            statusTxt.setText(reportForm.getStatus());
                                            timeTxt.setText(reportForm.getTime());
                                            usercnicTxt.setText(reportForm.getUsercnic());
                                            usernameTxt.setText(reportForm.getUsername());

                                            if(reportForm.getStatus().equals("Pending"))
                                            {
                                                commentSection.setVisibility(View.INVISIBLE);
                                            }
                                            else
                                            {
                                                commentSection.setVisibility(View.VISIBLE);
                                                commentTxt.setText(documentSnapshot.getString("comment"));
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
                                                StorageReference pathReference = storageRef.child("reports").child(reportForm.getReportID());
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
                                            Toast.makeText(user_search_report.this, "Report not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        loading.setVisibility(View.INVISIBLE);
                                        Toast.makeText(user_search_report.this, "Error fetching data.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(user_search_report.this, "Please enter the report number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        removedata();
        finish();
    }
    public  void removedata()
    {
        reportForm.setIncident("Not Selected");
        reportForm.setReportID(null);
        reportForm.setCity(null);
        reportForm.setAddress(null);
        reportForm.setLat(0);
        reportForm.setLon(0);
        reportForm.setDate(null);
        reportForm.setTime(null);
        reportForm.setUsercnic(userClass.getCnic());
        reportForm.setUsername(userClass.getFullname());
        reportForm.setUserphone(userClass.getPhone());
        reportForm.setDescription(null);
        reportForm.setStatus("Pending");
    }
}