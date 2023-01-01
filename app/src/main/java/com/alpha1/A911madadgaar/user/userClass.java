package com.alpha1.A911madadgaar.user;

import androidx.appcompat.app.AppCompatActivity;

public class userClass extends AppCompatActivity {
    public  static  String cnic,fullname,phone,warning;

    public static String getWarning() {
        return warning;
    }

    public static void setWarning(String warning) {
        userClass.warning = warning;
    }

    public static String getCnic() {
        return cnic;
    }

    public static void setCnic(String cnic) {
        userClass.cnic = cnic;
    }

    public static String getFullname() {
        return fullname;
    }

    public static void setFullname(String fullname) {
        userClass.fullname = fullname;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        userClass.phone = phone;
    }
}
