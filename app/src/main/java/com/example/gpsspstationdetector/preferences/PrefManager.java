package com.example.gpsspstationdetector.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "genesisApp";

    private static final String IS_LANGUAGE_CHANGE      = "language";
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putInt("id",0);
        editor.apply();
        editor.commit();
    }



    public boolean getIsLanguageChange(){
        return pref.getBoolean(IS_LANGUAGE_CHANGE,false);
    }
    public void setIsLanguageChange(boolean isLanguageChange){
        editor.putBoolean(IS_LANGUAGE_CHANGE,isLanguageChange);
        editor.commit();
    }

}
