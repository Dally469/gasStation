package com.example.gpsspstationdetector;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gpsspstationdetector.common.Common;
import com.example.gpsspstationdetector.preferences.PrefManager;

import java.util.Locale;

import io.paperdb.Paper;

public class SelectAccount extends AppCompatActivity {
    LinearLayout lnl_as_driver,lnl_as_station,lnl_change_lang;
    private Locale myLocale;
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
                Intent gotoHome = new Intent(this, DashboardStation.class);
                startActivity(gotoHome);
            }

        }else {
            Toast.makeText(this, "No one saved", Toast.LENGTH_SHORT).show();
        }

        lnl_as_driver = findViewById(R.id.lnl_as_driver);
        lnl_as_station = findViewById(R.id.lnl_as_station);
        lnl_change_lang = findViewById(R.id.lnl_change_language);


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
        lnl_change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SelectAccount.this, "Yess", Toast.LENGTH_SHORT).show();

                AlertDialog dialog;
                final LinearLayout eng,kinya,fre;
                final AlertDialog.Builder builder = new AlertDialog.Builder(SelectAccount.this);
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View viewDialog = layoutInflater.inflate(R.layout.change_language_layout, null);
                builder.setView(viewDialog);
                builder.setTitle(getString(R.string.str_change_lang));

                builder.setCancelable(false);
                builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent refresh = new Intent(SelectAccount.this, MainActivity.class);
                        startActivity(refresh);
                    }
                });
                eng = viewDialog.findViewById(R.id.lnl_english);
                kinya = viewDialog.findViewById(R.id.lnl_kinya);
                fre = viewDialog.findViewById(R.id.lnl_french);

                eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLanguage("fr");
                        Paper.book().write(Common.CURRENT_LANG,"fr");

                    }
                });

                kinya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLanguage("rw");
                        Paper.book().write(Common.CURRENT_LANG,"rw");

                    }
                });

                fre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLanguage("fr");
                        Paper.book().write(Common.CURRENT_LANG,"fr");

                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void setLanguage(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Toast.makeText(this, getString(R.string.str_lang_changed), Toast.LENGTH_SHORT).show();
        Intent refresh = new Intent(this, Launch.class);
        startActivity(refresh);


    }
}