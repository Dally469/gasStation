package com.example.gpsspstationdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class DashboardStation extends AppCompatActivity {
    private TextView name, phone;
    FirebaseDatabase database;
    DatabaseReference garageRequestTbl;
    LinearLayout lnlGarage,lnlFuel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_station);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        name = findViewById(R.id.txt_name);
        phone = findViewById(R.id.txt_phone);
        lnlGarage = findViewById(R.id.lnl_garage_request);
        lnlFuel = findViewById(R.id.lnl_fuel_request);

        Paper.init(this);
        name.setText(Paper.book().read(Common.USERNAME_KEY));
        phone.setText("Phone: " + Paper.book().read(Common.PHONE_KEY));

        database = FirebaseDatabase.getInstance();
        garageRequestTbl = database.getReference("GarageRequests");


        lnlGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stay = new Intent(getApplicationContext(), GarageRequestActivity.class);
                startActivity(stay);
            }
        });
        lnlFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stay = new Intent(getApplicationContext(), FuelRequests.class);
                startActivity(stay);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (name != null && phone != null) {
            Intent stay = new Intent(getApplicationContext(), DashboardStation.class);
            startActivity(stay);
        } else {
            Toast.makeText(this, "No one saved", Toast.LENGTH_SHORT).show();
        }
    }
}