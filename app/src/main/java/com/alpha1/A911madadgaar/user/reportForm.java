package com.alpha1.A911madadgaar.user;

import androidx.appcompat.app.AppCompatActivity;

public class reportForm extends AppCompatActivity {
    public static String reportID,date,time,address,location,incident,description,city,status;
    public static String usercnic,username,userphone;
    public static double lon,lat;


    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        reportForm.status = status;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        reportForm.city = city;
    }

    public static double getLon() {
        return lon;
    }

    public static void setLon(double lon) {
        reportForm.lon = lon;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        reportForm.lat = lat;
    }

    public static String getReportID() {
        return reportID;
    }

    public static void setReportID(String reportID) {
        reportForm.reportID = reportID;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        reportForm.date = date;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        reportForm.time = time;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        reportForm.address = address;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        reportForm.location = location;
    }

    public static String getIncident() {
        return incident;
    }

    public static void setIncident(String incident) {
        reportForm.incident = incident;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        reportForm.description = description;
    }

    public static String getUsercnic() {
        return usercnic;
    }

    public static void setUsercnic(String usercnic) {
        reportForm.usercnic = usercnic;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        reportForm.username = username;
    }

    public static String getUserphone() {
        return userphone;
    }

    public static void setUserphone(String userphone) {
        reportForm.userphone = userphone;
    }
}
