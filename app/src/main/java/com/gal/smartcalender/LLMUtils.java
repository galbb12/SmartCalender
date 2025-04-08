package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import androidx.room.Room;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LLMUtils {
    /**
     * Gets the current time zone in UTC offset
     */
    static private String get_curr_time_zone() {
        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);

        int offsetHours = zonedDateTime.getOffset().getTotalSeconds() / 3600;
        return offsetHours >= 0 ? "UTC+" + offsetHours : "UTC" + offsetHours;
    }

    /**
     * Generates System Message for LLM
     * */
    static protected String generate_system_message() {
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeCURR = ZonedDateTime.now();
        String iso8601LOCAL = zonedDateTimeCURR.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        String iso8601UTC = zonedDateTimeUTC.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return Constants.notification_process_sys_message.replace("<CURR_DATE_TIME_UTC>", iso8601UTC).replace("<CURR_TIME_ZONE>", get_curr_time_zone()).replace("<CURR_DATE_TIME>", iso8601LOCAL);
    }

}
