package com.example.gpsspstationdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class AccountSetting extends AppCompatActivity {
    private TextView name, phone;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab_assign_fingerprint;
    LinearLayout lnl_change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        name = findViewById(R.id.user_profile_name);
        phone = findViewById(R.id.user_profile_phone);
        lnl_change = findViewById(R.id.lnl_change);

        Paper.init(this);
        name.setText(Paper.book().read(Common.USERNAME_KEY));
        phone.setText("Phone: "+Paper.book().read(Common.PHONE_KEY));


        fab_assign_fingerprint = findViewById(R.id.assign_biometric_auth);

        fab_assign_fingerprint.setOnClickListener(view ->{
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        });

        lnl_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
            }
        });

    }
}