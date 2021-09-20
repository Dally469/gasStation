package com.example.gpsspstationdetector.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsspstationdetector.R;
import com.example.gpsspstationdetector.StationMapsActivity;
import com.example.gpsspstationdetector.models.FuelRequest;
import com.example.gpsspstationdetector.models.GarageRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class GarageRequestAdapter extends FirebaseRecyclerAdapter<GarageRequest, GarageRequestAdapter.MyViewHolder>
        implements View.OnCreateContextMenuListener {
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, phone,status;
        public ImageView photo;
        public CardView cardViewMember;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.client_name);
            phone = view.findViewById(R.id.client_phone);
            status = view.findViewById(R.id.request_status);
            cardViewMember = view.findViewById(R.id.cardViewHolder);
            linearLayout = view.findViewById(R.id.lnlNavigate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                }
            });


        }
    }

    public GarageRequestAdapter(Context context, FirebaseRecyclerOptions<GarageRequest> options) {
        super(options);
        this.context = context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_garage_request_layout, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull GarageRequest model) {

        holder.name.setText(model.getClient_name());
        holder.phone.setText(model.getClient_phone());
        if (model.getStatus().equals("0")){
            holder.status.setText(context.getString(R.string.str_pending));
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }else {
            holder.status.setText(context.getString(R.string.str_accepted));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.cardViewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMap = new Intent(context, StationMapsActivity.class);
                gotoMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                gotoMap.putExtra("customer", model.getClient_name());
                gotoMap.putExtra("phone", model.getClient_phone());
                gotoMap.putExtra("plate_number", model.getClient_plate_nbr());
                gotoMap.putExtra("latitude", model.getClient_latitude());
                gotoMap.putExtra("longitude", model.getClient_longitude());
                context.startActivity(gotoMap);
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Navigate Customer", Toast.LENGTH_SHORT).show();
                Intent gotoMap = new Intent(context, StationMapsActivity.class);
                gotoMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                gotoMap.putExtra("customer", model.getClient_name());
                gotoMap.putExtra("phone", model.getClient_phone());
                gotoMap.putExtra("plate_number", model.getClient_plate_nbr());
                gotoMap.putExtra("latitude", model.getClient_latitude());
                gotoMap.putExtra("longitude", model.getClient_longitude());
                context.startActivity(gotoMap);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        menu.add(0, 1, 0, "DELETE");
        menu.add(0, 2, 0,"UPDATE");
    }

}