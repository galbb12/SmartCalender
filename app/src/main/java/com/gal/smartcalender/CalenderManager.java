package com.gal.smartcalender;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class CalenderManager {
    private Context _ctx;

    CalenderManager(Context ctx){
        _ctx = ctx;
    }
    public void addToAllCalendars(Event event) {
        // Check for calendar permissions
        if (ContextCompat.checkSelfPermission(_ctx, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(_ctx, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, show a toast
            Toast.makeText(_ctx, "Calendar permissions are required to add events", Toast.LENGTH_LONG).show();
            return; // Exit the method if permissions are not granted
        }

        ContentResolver cr = _ctx.getContentResolver();

        // Query available calendars
        Uri calendarsUri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        Cursor cursor = cr.query(calendarsUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long calID = cursor.getLong(0); // Get calendar ID

                // Insert event into this calendar
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, event.startDate.toInstant().toEpochMilli());
                values.put(CalendarContract.Events.DTEND, event.endDate.toInstant().toEpochMilli());
                values.put(CalendarContract.Events.TITLE, event.eventInfo);
                values.put(CalendarContract.Events.DESCRIPTION, event.eventInfo);
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, event.startDate.getZone().getId());

                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
