package com.gal.smartcalender;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Database(entities = {Event.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao EventsDao();
}
