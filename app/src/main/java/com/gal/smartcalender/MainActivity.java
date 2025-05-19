package com.gal.smartcalender;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    RecyclerView recyclerView = null;
    AppDatabase db = null;
    RecyclerViewEventsAdapter recyclerViewEventsAdapter = null;
    Toolbar toolbar = null;
    CheckBox selectAll = null;
    FloatingActionButton deleteButton = null;
    FloatingActionButton addButton = null;
    TextView emptyInstructionTextView = null;
    CalenderManager calenderManager = null;

    private static final int REQUEST_CALENDAR_PERMISSION = 100;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request notification access permission
        if (!isNotificationServiceEnabled()) {
            requestNotificationAccessPermission();
        }
        requestCalendarPermissions();

        calenderManager = new CalenderManager(this);

        // Initialize the database
        db = DataBaseSingletone.get_db(getApplicationContext());

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        selectAll = findViewById(R.id.select_all);
        emptyInstructionTextView = findViewById(R.id.no_events_text_view);

        // Init action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable the main toolbar title

        // Handle select all checkbox
        selectAll.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                recyclerViewEventsAdapter.selectAll();
            } else {
                recyclerViewEventsAdapter.clearSelection();
            }
            toggleFloatingButtonsVisibility();
        });

        // Setup floating buttons
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                for (Event event : recyclerViewEventsAdapter.get_checked_events()) {
                    db.EventsDao().delete(event); // Delete selected events from the database
                }
            });
        });
        addButton = findViewById(R.id.addToCalendarButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    for (Event event : recyclerViewEventsAdapter.get_checked_events()) {
                        calenderManager.addToSelectedCalenders(event);
                        db.EventsDao().delete(event); // Delete selected events from the database
                    }
                });
            }
        });

        db.EventsDao().getAllLive().observe(this, events -> {if(events.isEmpty()) emptyInstructionTextView.setVisibility(VISIBLE); else emptyInstructionTextView.setVisibility(GONE);});

        recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(db, this, selectAll, deleteButton, addButton);
        recyclerView.setAdapter(recyclerViewEventsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (flat != null && !flat.isEmpty()) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null && TextUtils.equals(pkgName, cn.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void requestNotificationAccessPermission() {
        Toast.makeText(this, "Please enable notification access for this app", Toast.LENGTH_LONG).show();
        try {
            ComponentName cn = new ComponentName(this, NotificationService.class);
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            intent.putExtra(":settings:fragment_args_key", cn.flattenToString());
            intent.putExtra(":settings:show_fragment_args", cn.flattenToString());
            intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fallback in case some OEMs override the settings screen
            try {
                Intent fallbackIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(fallbackIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Unable to open settings", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    }

    // Request calendar permissions
    public void requestCalendarPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                // Show an explanation if the user has previously denied the permission
                Toast.makeText(this, "Calendar permissions are needed to add events", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR
                    },
                    REQUEST_CALENDAR_PERMISSION
            );
        } else {

        }
    }

    // Permission result handler
    @Override
    public void onRequestPermissionsResult(int requestCode, @Nullable String[] permissions, @Nullable int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CALENDAR_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Calendar permissions denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_NOTIFICATION_PERMISSION:
                if (isNotificationServiceEnabled()) {
                    // Notification access granted
                    Toast.makeText(this, "Notification access granted", Toast.LENGTH_SHORT).show();
                } else {
                    // Notification access denied
                    Toast.makeText(this, "Notification access denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void toggleFloatingButtonsVisibility() {
        if(recyclerViewEventsAdapter.get_checked_events() != null){
            if (recyclerViewEventsAdapter.get_checked_events().size() == 0) {
                deleteButton.setVisibility(GONE);
                addButton.setVisibility(GONE);
            } else {
                deleteButton.setVisibility(VISIBLE);
               addButton.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    protected int getSelectedNavItemId() {
        return R.id.nav_home;
    }
}
