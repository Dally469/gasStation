package com.example.gpsspstationdetector.models;

import android.widget.EditText;

public class Users {
    private String names;
    private String phone;

    public Users() {
    }

    public Users(String names, String phone) {
        this.names = names;
        this.phone = phone;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
