package com.example.namayesh.models;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class User {

    public String id;
    public String name;
    public String email;
    public String phone;
    public String password;
    public Long accunt;


    public User(String id, String name, String email, String phone, String password, Long accunt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accunt = accunt;
    }

    @BindingAdapter("SetUserAccunt")
    public static void SetUserAccunt(TextView textView, Long accunt) {
        if (accunt > 0) {
            Long minute = accunt / 60;
            Long hour = minute / 60;
            Long day = hour / 24;

            textView.setText(day + " Days\n" + hour % 24 + " Hour\n" + minute % 60 + " Minute");
        } else {
            textView.setText("0 Days");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getAccunt() {
        return accunt;
    }

    public void setAccunt(Long accunt) {
        this.accunt = accunt;
    }
}
