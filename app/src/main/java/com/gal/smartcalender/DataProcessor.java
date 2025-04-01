package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import androidx.room.Room;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DataProcessor {

    protected Context _context;
    static protected AppDatabase _db = null;

    DataProcessor(Context context) {
        _context = context;
        if(_db == null){
            _db = Room.databaseBuilder(_context, AppDatabase.class, Constants.db_name).build();
        }
    }

    public abstract void processNotification(StatusBarNotification sbn);

}
