package com.gal.smartcalender;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    List<Event> getAll();

    @Query("SELECT * FROM Event where handled=0")
    List<Event> getAllNothandled();

    @Query("SELECT * FROM Event where handled=0")
    List<Event> getAllHandled();

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);
}

@Entity
public class Event {
    @PrimaryKey
    public int eventId;

    @ColumnInfo(name = "dataSource") // The app's name
    public String dataSource;

    @ColumnInfo(name = "data") // The data to parse into the calender event
    public String data;

    @ColumnInfo(name = "handled")
    public Boolean handled;

    @ColumnInfo(name = "eventInfo") // The Information to write into the calender app
    public String eventInfo;

    @ColumnInfo(name = "startDate") // The start date of the event
    public LocalDateTime startDate;

    @ColumnInfo(name = "endDate") // The end date of the event
    public LocalDateTime endDate;
}


