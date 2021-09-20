package com.example.gpsspstationdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gpsspstationdetector.common.Common;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    TextView name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        name = findViewById(R.id.txt_name);
        phone = findViewById(R.id.txt_phone);
        Paper.init(this);
        name.setText(" Name : " + Paper.book().read(Common.USERNAME_KEY));
        phone.setText(" Phone : " +Paper.book().read(Common.PHONE_KEY));
    }
}