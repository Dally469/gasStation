package com.example.gpsspstationdetector.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.gpsspstationdetector.MyRequest;
import com.example.gpsspstationdetector.R;
import com.example.gpsspstationdetector.models.FuelRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenRequest extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference requestFuel;
    public ListenRequest() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        requestFuel = db.getReference("FuelRequests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestFuel.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        FuelRequest fuelRequest = snapshot.getValue(FuelRequest.class);
        showNotification(snapshot.getKey(), fuelRequest);
    }

    private void showNotification(String key, FuelRequest fuelRequest) {
        Intent request = new Intent(getApplicationContext(), MyRequest.class);
        request.putExtra("userPhone", fuelRequest.getClient_phone());
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,request,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("REQUEST NOTI")
                .setContentInfo("Your request was updated")
                .setContentText("Successful approved or denied #"+key)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentInfo("info");

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}