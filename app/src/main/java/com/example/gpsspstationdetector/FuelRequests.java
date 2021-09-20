package com.example.gpsspstationdetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gpsspstationdetector.adapters.FuelRequestAdapter;
import com.example.gpsspstationdetector.common.Common;
import com.example.gpsspstationdetector.models.FuelRequest;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class FuelRequests extends AppCompatActivity {

    FuelRequestAdapter fuelRequestAdapter;
    String stationID;
    FirebaseDatabase database;
    DatabaseReference fuelRequestTbl;
    Toolbar toolbar;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_request);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.str_customer_fuel_service));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_left_black_48dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Paper.init(this);
        stationID = Paper.book().read(Common.USER_CURRENT_ID);
        Toast.makeText(this, "StationID "+stationID, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerViewFuel);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        database = FirebaseDatabase.getInstance();
        fuelRequestTbl = database.getReference("FuelRequests");

        loadFuelRequests(stationID);


    }

    private void loadFuelRequests(String stationId) {
        FirebaseRecyclerOptions<FuelRequest> options = new FirebaseRecyclerOptions.Builder<FuelRequest>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("FuelRequests").orderByChild("station_id").equalTo(stationId), FuelRequest.class)
                .build();
        fuelRequestAdapter = new FuelRequestAdapter(getApplicationContext(),options);
        recyclerView.setAdapter(fuelRequestAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fuelRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fuelRequestAdapter.stopListening();

    }
}