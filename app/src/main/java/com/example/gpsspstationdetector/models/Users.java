package com.example.gpsspstationdetector.models;

import android.widget.EditText;

public class Users {
    private String names;
    private String phone;
    private String password;

    public Users() {
    }

    public Users(String names, String phone, String password) {
        this.names = names;
        this.phone = phone;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
