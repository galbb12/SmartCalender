package com.gal.smartcalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView = null;
    AppDatabase db = null;
    RecyclerViewEventsAdapter recyclerViewEventsAdapter = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.db_name).build();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Executors.newSingleThreadExecutor().execute(() -> {
            Event[] events = db.EventsDao().getAll().toArray(new Event[0]);
            runOnUiThread(() -> {
                recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(events);
                recyclerView.setAdapter(recyclerViewEventsAdapter);
            });
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu); // Add settings and other stuff to toolbar
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Call settings activity
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, com.gal.smartcalender.Settings.SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


