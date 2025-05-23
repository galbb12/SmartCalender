package com.gal.smartcalender;

import static android.content.Context.MODE_PRIVATE;
import static com.gal.smartcalender.Constants.APP_PREF_NAME;
import static com.gal.smartcalender.Constants.DEFAULT_SYS_PROMPT;
import static com.gal.smartcalender.Constants.SYS_PROMPT_TXT_PREFERENCE_NAME;
import static com.gal.smartcalender.EditSystemPromptActivity.PREF_EMPTY;

import android.content.Context;
import android.content.SharedPreferences;
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

    static protected String get_sys_message_template(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APP_PREF_NAME, MODE_PRIVATE);
        String curr_prompt = prefs.getString(SYS_PROMPT_TXT_PREFERENCE_NAME, PREF_EMPTY);

        if(curr_prompt.equals(PREF_EMPTY)){
            prefs.edit().putString(SYS_PROMPT_TXT_PREFERENCE_NAME, DEFAULT_SYS_PROMPT).apply();
            curr_prompt = DEFAULT_SYS_PROMPT;
        }

        return curr_prompt;
    }

    /**
     * Generates System Message for LLM
     * */
    static protected String generate_system_message(Context context) {

        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeCURR = ZonedDateTime.now();
        String iso8601LOCAL = zonedDateTimeCURR.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        String iso8601UTC = zonedDateTimeUTC.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return get_sys_message_template(context).replace("<CURR_DATE_TIME_UTC>", iso8601UTC).replace("<CURR_TIME_ZONE>", get_curr_time_zone()).replace("<CURR_DATE_TIME>", iso8601LOCAL);
    }

}
