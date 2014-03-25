
package com.example.hondana.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.example.hondana.R;

public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.pref_setting);
    }

}
