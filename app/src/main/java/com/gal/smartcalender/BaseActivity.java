package com.gal.smartcalender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gal.smartcalender.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNav;

    @Override
    public void setContentView(int layoutResID) {
        // Inflate the base layout
        LinearLayout baseLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.activity_base, null);
        // Find the content container
        FrameLayout contentFrame = baseLayout.findViewById(R.id.base_content);
        // Inflate the child layout into the container
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        // Set the composed layout as the activity content
        super.setContentView(baseLayout);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNav = findViewById(R.id.base_bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Class<?> targetActivity = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                targetActivity = MainActivity.class;
            } else if (itemId == R.id.nav_settings) {
                targetActivity = SettingsActivity.class;
            } else if (itemId == R.id.nav_about) {
                targetActivity = AboutActivity.class;
            }

            if (!this.getClass().equals(targetActivity)) {
                Intent intent = new Intent(this, targetActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return true;
        });
    }

    protected abstract int getSelectedNavItemId();

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(getSelectedNavItemId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(getSelectedNavItemId());
        }
    }

}