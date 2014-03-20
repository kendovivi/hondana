
package com.example.hondana.activity;

import android.content.Intent;

import com.example.hondana.R;

import android.os.Bundle;

import android.preference.PreferenceActivity;

;

public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.pref_setting);
    }
}
