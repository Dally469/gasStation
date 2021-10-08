package com.example.gpsspstationdetector;

import androidx.annotation.Nullable;
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
import com.example.gpsspstationdetector.models.Location;
import com.example.gpsspstationdetector.models.Users;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

public class CreateStation extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private EditText location;
    private EditText editTextLong;
    private EditText editTextLat;
    private boolean hasServices;
    SweetAlertDialog pDialog;
    Button register,btnGetCoordinate;
    ProgressDialog progressDialog;
    CheckBox chk_remember;
    private FirebaseDatabase database;
    private DatabaseReference referenceTbl;
    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_station);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        referenceTbl = database.getReference("Locations");
        //initiation id
        name = findViewById(R.id.names);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        register = findViewById(R.id.btn_register);
        editTextLat = findViewById(R.id.station_latitude);
        editTextLong = findViewById(R.id.station_longitude);
        progressDialog = new ProgressDialog(this);
        chk_remember = findViewById(R.id.check_has_services);
        btnGetCoordinate = findViewById(R.id.getMapCoordinate);

        if (chk_remember.isChecked()){
            hasServices = true;
        }else{
            hasServices = false;
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountUser();
            }
        });
        btnGetCoordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPointIntent = new Intent(CreateStation.this, MapsActivity.class);
                startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
            }
        });


    }

    private void createAccountUser() {

        //checking if the value is provided
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Name is required");
            name.requestFocus();
        }else if (TextUtils.isEmpty(location.getText().toString())) {
            location.setError("Address location is required");
            location.requestFocus();
        }else if (TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("Phone is required");
            phone.requestFocus();
        }else{

            Paper.book().write(Common.USERNAME_KEY, name.getText().toString().trim());
            Paper.book().write(Common.PHONE_KEY, phone.getText().toString().trim());
            Paper.book().write(Common.USER_TYPE_KEY, "2");
            String id = referenceTbl.push().getKey();

            Location artist = new Location(
                    id,
                    name.getText().toString().trim(),
                    phone.getText().toString().trim(),
                    hasServices,
                    "Descreption",
                    editTextLat.getText().toString().trim(),
                    editTextLong.getText().toString().trim()
            );
            Paper.book().write(Common.USER_CURRENT_ID, artist.getId().trim());


            referenceTbl.child(id).setValue(artist);
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Please creating account...");
            pDialog.setCancelable(false);
            pDialog.show();

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(CreateStation.this, "Station Created Successful", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(CreateStation.this,DashboardStation.class);
                    startActivity(mainIntent);
                    pDialog.dismissWithAnimation();
                }
            }, 1800);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MAP_POINT_REQUEST  && resultCode == RESULT_OK ) {
            // Make sure the request was successful
            LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
            Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            editTextLat.setText(latLng.latitude+"");
            editTextLong.setText(latLng.longitude+"");
        }
    }
}

