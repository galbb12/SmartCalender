package com.gal.smartcalender;

import static com.gal.smartcalender.Constants.SELECTED_CALENDERS_PREFERENCE;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalenderManager {
    private Context _ctx;
    private SharedPreferences _sharedPreferences;

    public CalenderManager(Context ctx){
        _ctx = ctx;
        this._sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
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

    public void addToSpecificCalendar(Event event, long calendarId) {
        // Check for calendar permissions
        if (ContextCompat.checkSelfPermission(_ctx, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(_ctx, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(_ctx, "Calendar permissions are required to add events", Toast.LENGTH_LONG).show();
            return;
        }

        ContentResolver cr = _ctx.getContentResolver();

        // Insert event into the specified calendar
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, event.startDate.toInstant().toEpochMilli());
        values.put(CalendarContract.Events.DTEND, event.endDate.toInstant().toEpochMilli());
        values.put(CalendarContract.Events.TITLE, event.eventInfo);
        values.put(CalendarContract.Events.DESCRIPTION, event.eventInfo);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, event.startDate.getZone().getId());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

    }

    public void addToSelectedCalenders(Event event){
        Set<String> selectedCalenders = _sharedPreferences.getStringSet(SELECTED_CALENDERS_PREFERENCE, new HashSet<String>());
        for (String calId: selectedCalenders) {
            addToSpecificCalendar(event, Long.parseLong(calId));
        }

    }


    public List<Pair<Long, String>> getCalendarIdsAndNames() {
        List<Pair<Long, String>> calendars = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(_ctx, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(_ctx, "Calendar permission required", Toast.LENGTH_LONG).show();
            return calendars;
        }

        String[] projection = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        Cursor cursor = _ctx.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        if (cursor != null) {
            int idCol = cursor.getColumnIndex(CalendarContract.Calendars._ID);
            int nameCol = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idCol);
                String name = cursor.getString(nameCol);
                calendars.add(new Pair<>(id, name));
            }
            cursor.close();
        }
        return calendars;
    }
}
