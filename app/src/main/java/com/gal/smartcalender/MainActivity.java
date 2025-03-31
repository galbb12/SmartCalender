package com.gal.smartcalender;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    Button send_button = null ;
    EditText text_field_view = null;
    ChatGptApi chatGptApi = null;
    RecyclerView recyclerView = null;
    AppDatabase db = null;
    RecyclerViewEventsAdapter recyclerViewEventsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.db_name).build();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Executors.newSingleThreadExecutor().execute(() -> {
            Event[] events = db.EventsDao().getAll().toArray(new Event[0]);
            runOnUiThread(() -> {
                recyclerViewEventsAdapter = new RecyclerViewEventsAdapter(events);
                recyclerView.setAdapter(recyclerViewEventsAdapter);
            });
        });


    }


}


