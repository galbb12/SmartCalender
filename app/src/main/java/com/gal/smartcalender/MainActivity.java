package com.gal.smartcalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView = null;
    AppDatabase db = null;
    RecyclerViewEventsAdapter recyclerViewEventsAdapter = null;
    Toolbar toolbar = null;
    ImageButton moreButton = null;
    CheckBox selectAll = null;
    FloatingActionButton deleteButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.db_name).build();

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        selectAll = findViewById(R.id.select_all);
        moreButton = findViewById(R.id.more_button);
        setSupportActionBar(toolbar);

        // Setup more button click listener
        moreButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, com.gal.smartcalender.Settings.SettingsActivity.class)));

        // Initialize the adapter and observe LiveData
        db.EventsDao().getAllLive().observe(this, events -> {
            if (recyclerViewEventsAdapter == null) {
                recyclerViewEventsAdapter = new RecyclerViewEventsAdapter((ArrayList<Event>) events, findViewById(R.id.select_all));
                recyclerView.setAdapter(recyclerViewEventsAdapter);
            }
            recyclerViewEventsAdapter.set_localDataSet(new ArrayList<>(events)); // Update data without resetting the adapter
        });

        // Handle select all checkbox
        selectAll.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                recyclerViewEventsAdapter.selectAll();
            } else {
                recyclerViewEventsAdapter.clearSelection();
            }
        });

        // Setup delete button
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                for (Event event : recyclerViewEventsAdapter.get_checked_events()) {
                    db.EventsDao().delete(event); // Delete selected events from the database
                }
            });
        });
    }
}
