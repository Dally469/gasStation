package com.example.gpsspstationdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;

import io.paperdb.Paper;

public class SelectAccount extends AppCompatActivity {
    LinearLayout lnl_as_driver,lnl_as_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Paper.init(this);

        String name = Paper.book().read(Common.USERNAME_KEY);
        String phone = Paper.book().read(Common.PHONE_KEY);
        String type = Paper.book().read(Common.USER_TYPE_KEY);
        if (name != null && phone != null && type != null){

            if (type.equals("1")){
                Intent gotoHome = new Intent(this, HomeMap.class);
                startActivity(gotoHome);
            }else {
                Toast.makeText(this, "got to station homepage", Toast.LENGTH_SHORT).show();
                Intent gotoHome = new Intent(this, HomeMap.class);
                startActivity(gotoHome);
            }

        }else {
            Toast.makeText(this, "No one saved", Toast.LENGTH_SHORT).show();
        }

        lnl_as_driver = findViewById(R.id.lnl_as_driver);
        lnl_as_station = findViewById(R.id.lnl_as_station);

        lnl_as_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_as_station.setBackground(getResources().getDrawable(R.drawable.service_layout));
                lnl_as_driver.setBackground(getResources().getDrawable(R.drawable.service_layout_default));

                Intent goTo = new Intent(SelectAccount.this, CreateStation.class);
                startActivity(goTo);
            }
        });

        lnl_as_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_as_driver.setBackground(getResources().getDrawable(R.drawable.service_layout));
                lnl_as_station.setBackground(getResources().getDrawable(R.drawable.service_layout_default));
                Intent goTo = new Intent(SelectAccount.this, Account.class);
                startActivity(goTo);
            }
        });


    }
}