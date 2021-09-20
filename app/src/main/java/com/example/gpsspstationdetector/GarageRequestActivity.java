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
import com.example.gpsspstationdetector.adapters.GarageRequestAdapter;
import com.example.gpsspstationdetector.common.Common;
import com.example.gpsspstationdetector.models.FuelRequest;
import com.example.gpsspstationdetector.models.GarageRequest;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class GarageRequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GarageRequestAdapter garageRequestAdapter;
    String stationID;
    FirebaseDatabase database;
    DatabaseReference products;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_request);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.str_customer_garage_service));
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

        recyclerView = findViewById(R.id.recyclerViewGarage);
        Paper.init(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        database = FirebaseDatabase.getInstance();
        products = database.getReference("GarageRequests");
        stationID = Paper.book().read(Common.USER_CURRENT_ID);

        Toast.makeText(this, "StationID: " + stationID, Toast.LENGTH_SHORT).show();

        loadGarageRequests(stationID);
    }

    private void loadGarageRequests(String stationId) {
        FirebaseRecyclerOptions<GarageRequest> options = new FirebaseRecyclerOptions.Builder<GarageRequest>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("GarageRequests").orderByChild("station_id").equalTo(stationId), GarageRequest.class)
                .build();
        garageRequestAdapter = new GarageRequestAdapter(getApplicationContext(),options);
        recyclerView.setAdapter(garageRequestAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        garageRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        garageRequestAdapter.stopListening();

    }
}