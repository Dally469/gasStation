package com.example.gpsspstationdetector;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String name,phone,plate_nbr;
    private TextView txtname,txtphone,txtplate_nbr;
    private Double latitude,longitude;
    private LinearLayout linearLayoutConfirm;
    private SweetAlertDialog sweetAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_maps);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtname = findViewById(R.id.txt_name);
        txtphone = findViewById(R.id.txt_phone);
        txtplate_nbr = findViewById(R.id.txt_plate);
        linearLayoutConfirm = findViewById(R.id.confirm_request);

        name = getIntent().getStringExtra("customer");
        phone = getIntent().getStringExtra("phone");
        plate_nbr = getIntent().getStringExtra("plate_number");
        latitude = getIntent().getDoubleExtra("latitude",1);
        longitude = getIntent().getDoubleExtra("longitude", 1);

        txtname.setText("" + name);
        txtphone.setText("Phone :" + phone);
        txtphone.setText("CAR Plate Number :" + plate_nbr);
        Toast.makeText(this, latitude+" and "+longitude, Toast.LENGTH_SHORT).show();

        linearLayoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog = new SweetAlertDialog(StationMapsActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitle("Confirm Request..");
                sweetAlertDialog.setContentText("Are you sure to confirm this client request");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmText("Yes");
                sweetAlertDialog.setCancelText("Cancel");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StationMapsActivity.this, "Accepted SUCCESSFUL", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),DashboardStation.class));
                                        sDialog.dismissWithAnimation();



                                    }
                                }, 2000);
                            }
                        });
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StationMapsActivity.this, "Canceled SUCCESSFUL", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),DashboardStation.class));

                                sDialog.dismissWithAnimation();

                            }
                        }, 2000);
                    }
                });
                sweetAlertDialog.show();


            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng coordinate = new LatLng(latitude,longitude
        );
        mMap.addMarker(
                new MarkerOptions()
                        .position(coordinate)
                        .title(name)
                        .snippet(phone + " Car plate number : " + plate_nbr)
                        .icon(mapIcon(getApplicationContext(), R.drawable.carmaker))
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
    }

    private BitmapDescriptor mapIcon(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }

}