package com.gal.smartcalender.Settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Just here so the fragment could be called as an activity
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
