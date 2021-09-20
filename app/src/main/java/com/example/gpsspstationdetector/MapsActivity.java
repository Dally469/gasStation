package com.example.gpsspstationdetector;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        getLocations();
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
        LatLng coordinate = new LatLng(-1.935114, 30.082111);
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Kigali Rwanda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,15.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Lat " + latLng.latitude + " " + "Long " + latLng.longitude, Toast.LENGTH_LONG).show();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("picked_point",latLng);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        Toast.makeText(this, "Please Tap to your location", Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("MissingPermission")
    private void getLocations() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            }else {
                getCurrentLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void OnGPS() {
        SweetAlertDialog pDialog = new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Enable GPS");
        pDialog.setContentText("Which location are you using the device");
        pDialog.setConfirmText("Enable");
        pDialog.getWindow().setLayout(800, 400);
        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                sDialog.dismissWithAnimation();
            }
        });
        pDialog.setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Toast.makeText(MapsActivity.this, "GPS IS NOT ENABLED ON DEVICE", Toast.LENGTH_SHORT).show();

                sDialog.dismissWithAnimation();
            }
        });

        pDialog.show();
    }
    private  void getCurrentLocation(){
        /* GETTING DEVICE GPS LOCATIONS SERVICES*/

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
                }else {
                    Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    if (locationGps != null){
                        double lat = locationGps.getLatitude();
                        double lng = locationGps.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lng);

                        Toast.makeText(MapsActivity.this, "Latitude : " + latitude + " , Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                    }else if (locationNetwork != null){
                        double lat = locationNetwork.getLatitude();
                        double lng = locationNetwork.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lng);

                        Toast.makeText(MapsActivity.this, "Latitude : " + latitude + " , Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                    }else if (locationPassive != null){
                        double lat = locationPassive.getLatitude();
                        double lng = locationPassive.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lng);

                        Toast.makeText(MapsActivity.this, "Latitude : " + latitude + " , Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(MapsActivity.this, "Can not", Toast.LENGTH_SHORT).show();
                    }
                }

    }
}