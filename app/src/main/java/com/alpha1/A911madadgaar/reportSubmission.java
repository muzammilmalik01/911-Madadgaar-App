package com.alpha1.A911madadgaar;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class reportSubmission extends AppCompatActivity implements OnMapReadyCallback {
    TextView addressTxt,date_txt,time_txt;
    Button collectLocation,collectDT;
    Spinner spinner;
    LocationRequest locationRequest;
    MapView mapView;
    GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission);


        /*Initializing All Variables of ReportForm Class Start*/
        reportForm.setIncident("Not Selected");
        reportForm.setReportID(null);
        reportForm.setCity(null);
        reportForm.setAddress(null);
        reportForm.setLat(0);
        reportForm.setLon(0);
        reportForm.setDate(null);
        reportForm.setTime(null);
        reportForm.setUsercnic(user.getCnic());
        reportForm.setUsername(user.getFullname());
        reportForm.setUserphone(user.getPhone());
        reportForm.setDescription(null);
        /* Initializing All Variables of ReportForm Class End */

        /*Assigning IDs to Variables Start*/
        addressTxt = findViewById(R.id.addressTxt);
        collectLocation = findViewById(R.id.getData);
        mapView = findViewById(R.id.mapView);
        spinner = findViewById(R.id.incident_spinner);
        collectDT = findViewById(R.id.setDTbtn);
        date_txt = findViewById(R.id.date_txt);
        time_txt = findViewById(R.id.time_txt);
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
                    reportForm.setIncident(parent.getItemAtPosition(position).toString()); //incident set.
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
        collectDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeDate();
            }
        }); //collect user's device Date & Time.
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
        date_txt.setText("Date: "+ currentDateString);// set on TextView

        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTimeString = dateFormat.format(calendar.getTime());
        reportForm.setTime(currentTimeString); //date set
        time_txt.setText("Time: "+ currentTimeString);// set on TextView

        Toast.makeText(reportSubmission.this, "Time & Date Successfully.", Toast.LENGTH_SHORT).show();
    } //function to set Time and Date.
    /* All Buttons End*/


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
}