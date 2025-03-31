package com.gal.smartcalender;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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


