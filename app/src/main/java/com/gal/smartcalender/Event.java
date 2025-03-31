package com.gal.smartcalender;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.time.ZonedDateTime;
import java.util.List;

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    public List<Event> getAll();

    @Insert
    public void insertAll(Event... events);

    @Delete
    public void delete(Event event);
}

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int eventId; // Database eventId

    @ColumnInfo(name = "DataSource") // Notification, Gmail app, whatever...
    public String dataSource;

    @ColumnInfo(name = "Data") // The data to parse into the calender event
    public String data;

    @ColumnInfo(name = "EventDescription") // The Information to write into the calender app
    public String eventInfo;

    @ColumnInfo(name = "StartDate") // The start date of the event
    public ZonedDateTime startDate;

    @ColumnInfo(name = "EndDate") // The end date of the event
    public ZonedDateTime endDate;

    @ColumnInfo(name = "Urgency") // Score 0-1 on how urgent is the event
    public float urgency;

    @ColumnInfo(name = "Importance") // Score 0-1 on how important is the event
    public float importance;
}


