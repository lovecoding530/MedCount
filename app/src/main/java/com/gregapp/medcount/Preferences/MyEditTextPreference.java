package com.gregapp.medcount.Preferences;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by Kangtle_R on 12/30/2017.
 */

public class MyEditTextPreference extends EditTextPreference implements Preference.OnPreferenceChangeListener {
    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(this);
    }

    @Override
    public CharSequence getSummary() {
        return this.getText();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(getSummary());
        return true;
    }
}
