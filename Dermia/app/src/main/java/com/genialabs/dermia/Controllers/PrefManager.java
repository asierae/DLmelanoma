package com.genialabs.dermia.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Dermia";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_WATER_CHECKED = "IsWaterChecked";
    private static final String IS_SUN_CHECKED = "IsSunChecked";
    private static final String IS_WEEKLY_SCAN_REMINDER_CHECKED = "IsScanChecked";
    private static final String APIKEY = "apikey";
    private static final String USERNAME = "u";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setAPIkey(String apikey) {
        editor.putString(APIKEY, apikey);
        editor.commit();
    }
    public  String getAPIkey(){
        return pref.getString(APIKEY,"");
    }



    public  String getUsername(){
        return pref.getString(USERNAME,"");
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isSunChecked() {
        return pref.getBoolean(IS_SUN_CHECKED, false);
    }
    public boolean isWaterChecked() {
        return pref.getBoolean(IS_WATER_CHECKED, false);
    }

    public void setSunChecked(boolean ischecked) {
        editor.putBoolean(IS_SUN_CHECKED, ischecked);
        editor.commit();
    }
    public void setWaterChecked(boolean ischecked) {
        editor.putBoolean(IS_WATER_CHECKED, ischecked);
        editor.commit();
    }

    public void setWeeklyScanCheked(boolean ischecked) {
        editor.putBoolean(IS_WEEKLY_SCAN_REMINDER_CHECKED, ischecked);
        editor.commit();
    }

}
