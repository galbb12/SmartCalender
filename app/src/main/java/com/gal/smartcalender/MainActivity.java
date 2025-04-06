package com.gal.smartcalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

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

    ImageButton moreButton = null;

    CheckBox selectAll = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.db_name).build();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        selectAll = findViewById(R.id.select_all);
        moreButton = findViewById(R.id.more_button);
        setSupportActionBar(toolbar);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.gal.smartcalender.Settings.SettingsActivity.class));
            }
        });
        selectAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if(recyclerViewEventsAdapter != null){
                if(b){
                    recyclerViewEventsAdapter.selectAll();
                }else{
                    recyclerViewEventsAdapter.clearSelection();
                }
            }
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            Event[] events = db.EventsDao().getAll().toArray(new Event[0]);
            runOnUiThread(() -> {
                recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(events);
                recyclerView.setAdapter(recyclerViewEventsAdapter);
            });
        });
    }


}


