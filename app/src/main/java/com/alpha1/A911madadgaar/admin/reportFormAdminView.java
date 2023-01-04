package com.alpha1.A911madadgaar.admin;

import androidx.appcompat.app.AppCompatActivity;

public class reportFormAdminView extends AppCompatActivity {
    public static String reportID,date,time,address,location,incident,description,city,status,comment,image;
    public static String usercnic;

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        reportFormAdminView.image = image;
    }

    public static String getComment() {
        return comment;
    }

    public static void setComment(String comment) {
        reportFormAdminView.comment = comment;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        reportFormAdminView.status = status;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        reportFormAdminView.city = city;
    }

    public static String getReportID() {
        return reportID;
    }

    public static void setReportID(String reportID) {
        reportFormAdminView.reportID = reportID;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        reportFormAdminView.date = date;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        reportFormAdminView.time = time;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        reportFormAdminView.address = address;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        reportFormAdminView.location = location;
    }

    public static String getIncident() {
        return incident;
    }

    public static void setIncident(String incident) {
        reportFormAdminView.incident = incident;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        reportFormAdminView.description = description;
    }

    public static String getUsercnic() {
        return usercnic;
    }

    public static void setUsercnic(String usercnic) {
        reportFormAdminView.usercnic = usercnic;
    }
}
