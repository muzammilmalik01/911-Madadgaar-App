package com.alpha1.A911madadgaar.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.alpha1.A911madadgaar.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//Todo: BUG = Multiple Markers when user clicks Location Button multiple times.
public class reportSubmission extends AppCompatActivity implements OnMapReadyCallback {
    TextView addressTxt,date_txt,time_txt, selectIncidentTxt;
    Button collectLocation,collectDT,selectImage,submitBtn;
    Spinner spinner;
    LocationRequest locationRequest;
    MapView mapView;
    GoogleMap map;
    String image;
    Uri IMAGE;
    ImageView proof;
    EditText descriptionEdit;
    FirebaseFirestore Database;
    LottieAnimationView loadinAnim;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission);

        Database = FirebaseFirestore.getInstance();

        /* Initializing All Variables of ReportForm Class Start */
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
        image = null;
        /* Initializing All Variables of ReportForm Class End */

        /*Assigning IDs to Variables Start*/
        addressTxt = findViewById(R.id.addressTxt);
        collectLocation = findViewById(R.id.getData);
        mapView = findViewById(R.id.mapView);
        spinner = findViewById(R.id.incident_spinner);
//        collectDT = findViewById(R.id.setDTbtn);
//        date_txt = findViewById(R.id.date_txt);
//        time_txt = findViewById(R.id.time_txt);
        selectIncidentTxt = findViewById(R.id.selectIncidentTxt);
        selectImage = findViewById(R.id.imageSelectButton);
        proof = findViewById(R.id.proofImageView);
        submitBtn = findViewById(R.id.submitBtn);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        loadinAnim = findViewById(R.id.loadinAnim);
        loadinAnim.setVisibility(View.INVISIBLE);
        /* Assigning IDs to Variables End */

        /* Stuff related to Location Start */
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        /* Stuff related to Location End */

        /* Map View Setup Start*/
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                // Do any additional setup here, such as adding markers or setting the camera position
            }
        });
        /* Map View Setup Start*/

        /* Spinner Setup Start*/
        String[] items = getResources().getStringArray(R.array.incidents_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_options_layout,R.id.textView, items);
        adapter.setDropDownViewResource(R.layout.spinner_options_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                reportForm.setIncident(parent.getItemAtPosition(position).toString());
                selectIncidentTxt.setText("Select Incident: "+ reportForm.getIncident()); //incident set.
                // Do something with the selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                reportForm.setIncident(null);
                Toast.makeText(reportSubmission.this, reportForm.getIncident(), Toast.LENGTH_SHORT).show();
            }
        });
        /* Spinner Setup End*/

        /* All Buttons Start*/
        collectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ContextCompat.checkSelfPermission(reportSubmission.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    // Location permission is not granted
                    // You may need to request the permission from the user
                    if(ContextCompat.checkSelfPermission(reportSubmission.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        //Requesting the Location Access.
                        Toast.makeText(reportSubmission.this, "Please Grant the Request.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(reportSubmission.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        // GPS is not enabled, show a dialog to request the user to enable it
                        showGpsRequestDialog(); //requesting for the GPS permission.
                    }
                    else
                    {
                        Toast.makeText(reportSubmission.this, "Handle the abnormality.", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    // Location permission is already granted
                    // You can proceed with accessing the location data
                    LocationServices.getFusedLocationProviderClient(reportSubmission.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(reportSubmission.this)
                                            .removeLocationUpdates(this);

                                    if(locationResult!=null && locationResult.getLocations().size()>0)
                                    {
                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude =  locationResult.getLocations().get(index).getLatitude(); //getting user's latitude
                                        double longitude =  locationResult.getLocations().get(index).getLongitude(); //getting user's longitude
                                        reportForm.setLon(longitude); //setting Longitude in reportForm class
                                        reportForm.setLat(latitude); //setting Latitude in reportForm class

                                        //setting Location on the Map View.

                                        LatLng location = new LatLng(latitude, longitude);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17)); //moving camera to the location
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        //can edit marker over here.
                                        markerOptions.position(location);
                                        map.addMarker(markerOptions);//setting marker to the location.



                                        Geocoder geocoder = new Geocoder(reportSubmission.this);//getting address & city of the user's current location using coordinates.
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                            if (addresses.size() > 0) {
                                                Address address = addresses.get(0);

                                                // Get the city, country, and address
                                                String city = address.getLocality();
                                                String addressLine = address.getAddressLine(0);
                                                addressTxt.setText(addressLine);
                                                reportForm.setAddress(addressLine); //address set.
                                                reportForm.setCity(city); //city set.


                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(reportSubmission.this, "Location Update Successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, Looper.getMainLooper());

                }
            }

        }); //collect User's Location.
//        collectDT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setTimeDate();
//            }
//        }); //collect user's device Date & Time.
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(reportSubmission.this)
                        .start();
            }
        }); //select Image from Gallery or Camera
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadinAnim.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
                setTimeDate();
                reportForm.setDescription(descriptionEdit.getText().toString().trim());
                String description = descriptionEdit.getText().toString().trim();
                if(reportForm.getLat()==0 || reportForm.getLon()==0 || description.length()<=0 || reportForm.getDescription()== null || reportForm.getTime() == null || reportForm.getDate() == null || reportForm.getIncident().equals("Not Selected"))
                {
                    loadinAnim.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    if(reportForm.getLat()==0 || reportForm.getLon()==0 || reportForm.getAddress() == null)
                    {
                        Toast.makeText(reportSubmission.this, "Please set your location!", Toast.LENGTH_SHORT).show();
                    }
                    else if(description.length()<=0)
                    {
                        Toast.makeText(reportSubmission.this, "Please fill the description!", Toast.LENGTH_SHORT).show();
                    }
                    else if(reportForm.getIncident().equals("Not Selected"))
                    {
                        Toast.makeText(reportSubmission.this, "Please select an Incident!", Toast.LENGTH_SHORT).show();
                    }
                    else if(reportForm.getTime() == null || reportForm.getDate() == null)
                    {
                        Toast.makeText(reportSubmission.this, "Please set Date & Time!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Database.collection("reportnumber")
                            .document("reportnumber")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if(documentSnapshot.exists())
                                        {
                                            String reportnumber = documentSnapshot.getString("current");
                                            reportForm.setReportID(documentSnapshot.getString("current"));
                                            int incrementreportnumber = Integer.valueOf(reportnumber);
                                            incrementreportnumber++;

                                            reportnumber = String.valueOf(incrementreportnumber);
                                            FirebaseFirestore updatern;
                                            updatern = FirebaseFirestore.getInstance();
                                            Map<String, Object> reportnumberupdate = new HashMap<>();
                                            reportnumberupdate.put("current", reportnumber);
                                            updatern.collection("reportnumber")
                                                    .document("reportnumber")
                                                    .set(reportnumberupdate);

                                            /* Report Submission Start */
                                            Map<String, Object> report = new HashMap<>();
                                            report.put("usercnic", reportForm.getUsercnic());
                                            report.put("userphone", reportForm.getUserphone());
                                            report.put("username", reportForm.getUsername());
                                            report.put("address", reportForm.getAddress());
                                            String lat = String.valueOf(reportForm.getLat());
                                            String lon = String.valueOf(reportForm.getLon());
                                            report.put("lat", lat);
                                            report.put("lon", lon);
                                            report.put("incident", reportForm.getIncident());
                                            report.put("description", reportForm.getDescription());
                                            report.put("time", reportForm.getTime());
                                            report.put("date", reportForm.getDate());
                                            report.put("status", reportForm.getStatus());
                                            report.put("reportno", reportForm.getReportID());
                                            report.put("city", reportForm.getCity());
                                            if(image!=null)
                                            {
                                                report.put("image", "code1");// code1 == Image Attached
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageRef = storage.getReference();
                                                StorageReference mountainsRef = storageRef.child("reports/"+ reportForm.getReportID()+"/");
                                                mountainsRef.putFile(IMAGE);
                                            }
                                            else
                                            {
                                                report.put("image", "code0");// code1 == Image NOT Attached
                                            }
                                            //Writing the Report.
                                            FirebaseFirestore writeReport;
                                            writeReport = FirebaseFirestore.getInstance();
                                            writeReport.collection("reports")
                                                    .document(reportForm.getReportID())
                                                    .set(report)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            image = null;

                                                            Toast.makeText(reportSubmission.this, "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                            //confirmation Screen goes here.
                                                            Intent gotoConfirmationScreen = new Intent(reportSubmission.this, confirmation_screen.class);
                                                            startActivity(gotoConfirmationScreen);
                                                            finish();
                                                            loadinAnim.setVisibility(View.INVISIBLE);
                                                            submitBtn.setVisibility(View.VISIBLE);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(reportSubmission.this, "Error While submitting report.", Toast.LENGTH_SHORT).show();
                                                            loadinAnim.setVisibility(View.INVISIBLE);
                                                            submitBtn.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                /* Report Submission End */

                                        }
                                    }
                                }
                            });
                }
            }
        });
        /* All Buttons Start*/
    }
    /* All Functions Start*/
    private void showGpsRequestDialog() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(reportSubmission.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(reportSubmission.this,2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }
    private void setTimeDate(){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDateString = dateFormat.format(calendar.getTime());
        reportForm.setDate(currentDateString); //date set
        //date_txt.setText("Date: "+ currentDateString);// set on TextView

        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTimeString = dateFormat.format(calendar.getTime());
        reportForm.setTime(currentTimeString); //date set
        //time_txt.setText("Time: "+ currentTimeString);// set on TextView

        //Toast.makeText(reportSubmission.this, "Time & Date Successfully.", Toast.LENGTH_SHORT).show();
    } //function to set Time and Date.
    /* All Buttons End*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri  = data.getData();

            image = uri.toString();
            IMAGE = uri;
            proof.setImageURI(IMAGE);
            selectImage.setText("Select Image ✔️");
            Toast.makeText(this, "Image Selected Successfully.", Toast.LENGTH_SHORT).show();
            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            image = null;
        } else {
            proof.setImageResource(R.drawable.picture);
            IMAGE = null;
            image = null;
            selectImage.setText("Select Image ❌");
            Toast.makeText(this, "No Image Selected / Selected Image Unselected", Toast.LENGTH_SHORT).show();
        }
    } //Image Selection.

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onBackPressed()
    {
        Intent gotoConfirmationScreen = new Intent(reportSubmission.this, HomeScreen.class);
        startActivity(gotoConfirmationScreen);
        finish();
    }
}