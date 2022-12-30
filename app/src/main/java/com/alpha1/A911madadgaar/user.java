package com.alpha1.A911madadgaar;

import androidx.appcompat.app.AppCompatActivity;

public class user extends AppCompatActivity {
    public  static  String cnic,fullname,phone,warning;

    public static String getWarning() {
        return warning;
    }

    public static void setWarning(String warning) {
        user.warning = warning;
    }

    public static String getCnic() {
        return cnic;
    }

    public static void setCnic(String cnic) {
        user.cnic = cnic;
    }

    public static String getFullname() {
        return fullname;
    }

    public static void setFullname(String fullname) {
        user.fullname = fullname;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        user.phone = phone;
    }
}
