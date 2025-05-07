package com.gal.smartcalender.Settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.gal.smartcalender.BaseActivity;
import com.gal.smartcalender.R;

// Just here so the fragment could be called as an activity
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }

    @Override
    protected int getSelectedNavItemId() {
        return R.id.nav_settings;
    }
}
