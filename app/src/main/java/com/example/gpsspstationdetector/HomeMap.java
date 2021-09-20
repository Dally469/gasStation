package com.example.gpsspstationdetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;
import com.example.gpsspstationdetector.models.FuelRequest;
import com.example.gpsspstationdetector.models.GarageRequest;
import com.example.gpsspstationdetector.models.Users;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView name, phone, txt_fuel, txt_garage, txt_delivery;
    Marker marker;
    FirebaseDatabase database;
    DatabaseReference table, garageRequestTbl,fuelRequestTbl;
    LinearLayout lnl_setting, lnl_garage, lnl_fuel, lnl_delivery, lnl_close;
    FloatingActionButton fab;
    CardView service_option, garage_option, fuel_delivery_option;
    ImageView img_garage, img_fuel, img_delivery;
    //for garage
    EditText ed_name, ed_phone, ed_plate_no;
    EditText edf_name, edf_phone, edf_plate_no, edf_liter;
    Spinner edf_type;
    Button btn_garage, btn_fuel,anotherBtn;
    private View mapView;
    SweetAlertDialog sweetAlertDialog;
    private Date date;
    private String locationId;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private LocationManager locationManager;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private String latitude, longitude,currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeMap.this);
        Places.initialize(HomeMap.this, "AIzaSyAdP1jL-Q5rK7ocerTKqqzhYID4oJpgZ8g");
        placesClient = Places.createClient(HomeMap.this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        fab = findViewById(R.id.fab);
        name = findViewById(R.id.txt_name);
        phone = findViewById(R.id.txt_phone);
        lnl_setting = findViewById(R.id.setting_account);
        service_option = findViewById(R.id.card_service_options);
        lnl_delivery = findViewById(R.id.lnl_delivery);
        lnl_garage = findViewById(R.id.lnl_garage);
        lnl_fuel = findViewById(R.id.lnl_fuel);
        txt_delivery = findViewById(R.id.txt_delivery);
        txt_fuel = findViewById(R.id.txt_fuel);
        txt_garage = findViewById(R.id.txt_garage);
        lnl_close = findViewById(R.id.lnl_close);
        img_delivery = findViewById(R.id.img_delivery);
        img_fuel = findViewById(R.id.img_fuel);
        img_garage = findViewById(R.id.img_garage);
        garage_option = findViewById(R.id.garage_service_options);
        fuel_delivery_option = findViewById(R.id.delivery_service_options);
        //for garage
        ed_name = findViewById(R.id.driver_name);
        ed_phone = findViewById(R.id.phone_nbr);
        ed_plate_no = findViewById(R.id.plate_number);
        btn_garage = findViewById(R.id.btn_garage_option);

        edf_name = findViewById(R.id.fuel_driver_name);
        edf_phone = findViewById(R.id.fuel_phone);
        edf_plate_no = findViewById(R.id.fuel_plate_number);
        edf_liter = findViewById(R.id.fuel_liter);
        edf_type = findViewById(R.id.fuel_type);

        btn_fuel = findViewById(R.id.btn_fuel_request);
        anotherBtn = findViewById(R.id.another);
        anotherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });


        lnl_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_garage.setBackground(getResources().getDrawable(R.drawable.service_layout));
                txt_garage.setTextColor(getResources().getColor(R.color.green));
                img_garage.setColorFilter(getResources().getColor(R.color.green));
                txt_fuel.setTextColor(getResources().getColor(R.color.grey));
                txt_delivery.setTextColor(getResources().getColor(R.color.grey));
                lnl_fuel.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                img_fuel.setColorFilter(getResources().getColor(R.color.grey));
                lnl_delivery.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                img_delivery.setColorFilter(getResources().getColor(R.color.grey));

                //DATA LATER

                garage_option.setVisibility(View.VISIBLE);
                service_option.setVisibility(View.GONE);
            }
        });
        lnl_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_delivery.setBackground(getResources().getDrawable(R.drawable.service_layout));
                txt_delivery.setTextColor(getResources().getColor(R.color.green));
                img_delivery.setColorFilter(getResources().getColor(R.color.green));
                lnl_fuel.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                lnl_garage.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                txt_garage.setTextColor(getResources().getColor(R.color.grey));
                img_garage.setColorFilter(getResources().getColor(R.color.grey));
                txt_fuel.setTextColor(getResources().getColor(R.color.grey));
                img_fuel.setColorFilter(getResources().getColor(R.color.grey));

                //DATA LATER

                fuel_delivery_option.setVisibility(View.VISIBLE);
                garage_option.setVisibility(View.GONE);
                service_option.setVisibility(View.GONE);
            }
        });

        lnl_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_fuel.setBackground(getResources().getDrawable(R.drawable.service_layout));
                txt_fuel.setTextColor(getResources().getColor(R.color.green));
                img_fuel.setColorFilter(getResources().getColor(R.color.green));
                lnl_delivery.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                lnl_garage.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                txt_delivery.setTextColor(getResources().getColor(R.color.grey));
                img_delivery.setColorFilter(getResources().getColor(R.color.grey));
                txt_garage.setTextColor(getResources().getColor(R.color.grey));
                img_garage.setColorFilter(getResources().getColor(R.color.grey));

                //DATA LATER
                Intent intent = new Intent(getApplicationContext(), RequestService.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service_option.setVisibility(View.VISIBLE);
            }
        });

        date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDate = df.format(date);


        Paper.init(this);
        name.setText(Paper.book().read(Common.USERNAME_KEY));
        phone.setText("Phone: " + Paper.book().read(Common.PHONE_KEY));

        database = FirebaseDatabase.getInstance();
        table = database.getReference("Locations");
        garageRequestTbl = database.getReference("GarageRequests");
        fuelRequestTbl = database.getReference("FuelRequests");

        lnl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountSetting.class);
                startActivity(intent);
            }
        });
        lnl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_option.setVisibility(View.GONE);
            }
        });


        btn_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if the value is provided
                if (TextUtils.isEmpty(ed_name.getText().toString())) {
                    ed_name.setError("Name is required");
                    ed_name.requestFocus();
                } else if (TextUtils.isEmpty(ed_phone.getText().toString())) {
                    ed_phone.setError("Phone is required");
                    ed_phone.requestFocus();
                } else if (TextUtils.isEmpty(ed_plate_no.getText().toString())) {
                    ed_plate_no.setError("Plate number is required");
                    ed_plate_no.requestFocus();
                } else {

                    String id = garageRequestTbl.push().getKey();

                    GarageRequest artist = new GarageRequest(
                            locationId,
                            ed_name.getText().toString().trim(),
                            ed_phone.getText().toString().trim(),
                            ed_plate_no.getText().toString().trim(),
                            mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude(),
                            currentDate,
                            "0"
                    );

                    sweetAlertDialog = new SweetAlertDialog(HomeMap.this, SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.setTitle("Please wait..");
                    sweetAlertDialog.setContentText("While we requesting services");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            garage_option.setVisibility(View.GONE);
                            garageRequestTbl.child(id).setValue(artist);
                            Toast.makeText(HomeMap.this, "Garage Service Send SUCCESSFUL", Toast.LENGTH_LONG).show();
                            sweetAlertDialog.dismissWithAnimation();

                        }
                    }, 2000);




                }
            }
        });

        btn_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if the value is provided
                if (TextUtils.isEmpty(edf_name.getText().toString())) {
                    edf_name.setError("Name is required");
                    edf_name.requestFocus();
                } else if (TextUtils.isEmpty(edf_phone.getText().toString())) {
                    edf_phone.setError("Phone is required");
                    edf_phone.requestFocus();
                } else if (TextUtils.isEmpty(edf_plate_no.getText().toString())) {
                    edf_plate_no.setError("Plate number is required");
                    edf_plate_no.requestFocus();
                } else if (TextUtils.isEmpty(edf_liter.getText().toString())) {
                    edf_liter.setError("Fuel liter is required");
                    edf_liter.requestFocus();
                }  else {


                    String id = fuelRequestTbl.push().getKey();

                    FuelRequest fuelRequest = new FuelRequest(
                            locationId,
                            edf_name.getText().toString().trim(),
                            edf_phone.getText().toString().trim(),
                            edf_plate_no.getText().toString().trim(),
                            edf_type.getSelectedItem().toString(),
                            edf_liter.getText().toString().trim(),
                            mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude(),
                            currentDate,
                            "0"
                    );

                    sweetAlertDialog = new SweetAlertDialog(HomeMap.this, SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.setTitle("Please wait..");
                    sweetAlertDialog.setContentText("While we requesting services");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            garage_option.setVisibility(View.GONE);
                            fuelRequestTbl.child(id).setValue(fuelRequest);
                            Toast.makeText(HomeMap.this, "Fuel Service Send SUCCESSFUL", Toast.LENGTH_LONG).show();
                            sweetAlertDialog.dismissWithAnimation();

                        }
                    }, 2000);




                }
            }
        });

    }


    private BitmapDescriptor mapIcon(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);



        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check gps enabled

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(HomeMap.this);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(HomeMap.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getMyLocation();
            }
        });
        task.addOnFailureListener(HomeMap.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(HomeMap.this, 51);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });


        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String lat = child.child("latitude").getValue().toString();
                    String lng = child.child("longitude").getValue().toString();
                    boolean isGarage = child.child("garageExist").getValue(Boolean.class);

                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lng);
                    LatLng coordinate = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(coordinate)
                            .snippet(child.child("id").getValue().toString())
                            .title(child.child("name").getValue().toString())
//                            .snippet(isGarage == true ? getString(R.string.str_both) : getString(R.string.str_single))
                            .icon(isGarage == true ? mapIcon(getApplicationContext(), R.drawable.icon_map_both) : mapIcon(getApplicationContext(), R.drawable.icon_map))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        // adding on click listener to marker of google maps.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                String markerName = marker.getTitle();
                locationId = marker.getSnippet();
                service_option.setVisibility(View.VISIBLE);
                Toast.makeText(HomeMap.this, "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        service_option.setVisibility(View.GONE);
        if (name != null && phone != null) {
            Intent stay = new Intent(getApplicationContext(), HomeMap.class);
            startActivity(stay);
        } else {
            Toast.makeText(this, "No one saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getMyLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void getMyLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(
                                                mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()),
                                        14
                                ));
                            }else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null){
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(
                                                        mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()),
                                                14));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }else {
                            Toast.makeText(HomeMap.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



}