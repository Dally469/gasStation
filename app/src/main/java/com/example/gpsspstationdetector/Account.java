package com.example.gpsspstationdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;
import com.example.gpsspstationdetector.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

public class Account extends AppCompatActivity {
    private EditText name;
    private EditText phone;
    private EditText confirm_password, password;
    Button register;
    ProgressDialog progressDialog;
    CheckBox chk_remember;
    private FirebaseDatabase database;
    private DatabaseReference referenceTbl;
    SweetAlertDialog pDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        referenceTbl = database.getReference("Users");
        //initiation id
        name = findViewById(R.id.names);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        register = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(this);
        chk_remember = findViewById(R.id.check_me);
        chk_remember.isChecked();

        String name = Paper.book().read(Common.USERNAME_KEY);
        String phone = Paper.book().read(Common.PHONE_KEY);
        if (name != null && phone != null){
            ContinueToHome(name,phone);
        }else {
            Toast.makeText(this, "No one saved", Toast.LENGTH_SHORT).show();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountUser();
            }
        });
    }

    private void ContinueToHome(String name, String phone) {
        Toast.makeText(this, "Name : " +name + " and Phone =" + phone, Toast.LENGTH_SHORT).show();
        Intent gotoHome = new Intent(this, HomeMap.class);
        startActivity(gotoHome);
    }

    private void createAccountUser() {

        //checking if the value is provided
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Name is required");
            name.requestFocus();
        }else if (TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("Phone is required");
            phone.requestFocus();
        }else if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Password is required");
            password.requestFocus();
        }else if (!password.getText().toString().equals(confirm_password.getText().toString())){
            confirm_password.setError("Password not match");
            confirm_password.requestFocus();
        }else{

            Paper.book().write(Common.USERNAME_KEY, name.getText().toString().trim());
            Paper.book().write(Common.PHONE_KEY, phone.getText().toString().trim());
            Paper.book().write(Common.USER_TYPE_KEY, "1");
            String id = referenceTbl.push().getKey();

            Users artist = new Users(
                    name.getText().toString().trim(),
                    phone.getText().toString().trim(),
                    password.getText().toString().trim()
            );
            referenceTbl.child(id).setValue(artist);
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please creating account...");
            pDialog.setCancelable(false);
            pDialog.show();

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(Account.this, "Station Created Successful", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(Account.this,HomeMap.class);
                    startActivity(mainIntent);
                    pDialog.dismissWithAnimation();
                }
            }, 1800);

        }

    }



}