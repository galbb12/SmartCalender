package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Pair;

import androidx.room.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DataProcessor {

    protected Context _context;
    protected AppDatabase _db;

    DataProcessor(Context context){
        _context = context;
        _db = Room.databaseBuilder(_context,
                AppDatabase.class, Constants.db_name).build();
    }

    protected String generate_system_message(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);


        return Constants.notification_process_sys_message.replace("<CURRENT_DATE_TIME>", formattedDateTime);
    }
    public abstract void processNotification(StatusBarNotification sbn); // First term is data source and second is the data

}
