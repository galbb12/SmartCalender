package com.gal.smartcalender;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Converters {

    private static final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDateTime) : null;
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, formatterLocalDateTime) : null;
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String dateTimeString) {
        return dateTimeString != null ? OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    @TypeConverter
    public static String fromZonedDateTime(ZonedDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    @TypeConverter
    public static ZonedDateTime toZoneDateTime(String dateTimeString) {
        return dateTimeString != null ? ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }
}
