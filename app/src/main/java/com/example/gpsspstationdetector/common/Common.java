package com.example.gpsspstationdetector.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.gpsspstationdetector.models.Users;

public class Common {
    public static Users currentUser;
    public static final String USERNAME_KEY = "Name";
    public static final String PHONE_KEY = "Phone";
    public static final String USER_TYPE_KEY = "UserType";
    public static final String USER_CURRENT_ID = "UserId";


    public static boolean IsNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
