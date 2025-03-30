package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Pair;

import androidx.room.Room;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DataProcessor {

    protected Context _context;
    protected AppDatabase _db;

    DataProcessor(Context context) {
        _context = context;
        _db = Room.databaseBuilder(_context,
                AppDatabase.class, Constants.db_name).build();
    }

    private String get_curr_time_zone() {
        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);

        int offsetHours = zonedDateTime.getOffset().getTotalSeconds() / 3600;
        return offsetHours >= 0 ? "UTC+" + offsetHours : "UTC" + offsetHours;
    }

    protected String generate_system_message() {
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.now(ZoneOffset.UTC);
        String iso8601UTC = zonedDateTimeUTC.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return Constants.notification_process_sys_message.replace("<CURR_DATE_TIME>", iso8601UTC).replace("<CURR_TIME_ZONE>", get_curr_time_zone());
    }
    public abstract void processNotification(StatusBarNotification sbn); // First term is data source and second is the data

}
