package com.gregapp.medcount;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.util.Map;

/**
 * Created by Kangtle_R on 12/30/2017.
 */

public class MyPreference {
    private static final String mypreference = "MEDCOUNT_PREFS";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String IS_ACCEPTED_TERMS = "IS_ACCEPTED_TERMS";
    private static final String PATTERN = "PATTERN";
    private static final String LAST_REMIND_TIME = "LAST_REMIND_TIME";
    private static final String NEXT_REMIND_TIME = "NEXT_REMIND_TIME";

    private Context context;

    public MyPreference(Context context){
        this.context = context;
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sp.edit();
    }

    public boolean isAcceptedTerms(){
        return this.sp.getBoolean(IS_ACCEPTED_TERMS, false);
    }

    public void setIsAcceptedTerms(boolean isAcceptedTerms){
        this.editor.putBoolean(IS_ACCEPTED_TERMS, isAcceptedTerms).commit();
    }

    public String getPattern(){
        return sp.getString(PATTERN, "");
    }

    public void setPattern(String pattern){
        editor.putString(PATTERN, pattern).commit();
    }

    public int getAge(){
        return sp.getInt(context.getResources().getString(R.string.pref_key_age), 30);
    }

    public String getGender(){
//        String[] genderList = context.getResources().getStringArray(R.array.gender);
        return sp.getString(context.getResources().getString(R.string.pref_key_gender), "Male");
    }

    public String getMedicName(){
        return sp.getString(context.getResources().getString(R.string.pref_key_medic_name), "");
    }

    public String getDoctorEmail(){
        return sp.getString(context.getResources().getString(R.string.pref_key_doctor_email), "");
    }

    public Map<String, ?> getAll(){
        return sp.getAll();
    }

    public long getLastRemindTime(){
        return sp.getLong(LAST_REMIND_TIME, 0);
    }

    public void setLastRemindTime(){
        editor.putLong(LAST_REMIND_TIME, System.currentTimeMillis()).commit();
    }

    public long getNextRemindtime(){
        return sp.getLong(NEXT_REMIND_TIME, 0);
    }

    public void setNextRemindTime(long randomPeriod){
        editor.putLong(NEXT_REMIND_TIME, System.currentTimeMillis()+randomPeriod).commit();
    }

}
