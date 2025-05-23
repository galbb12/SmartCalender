package com.gal.smartcalender;

import static android.content.Context.MODE_PRIVATE;
import static com.gal.smartcalender.Constants.APP_PREF_NAME;
import static com.gal.smartcalender.Constants.DEFAULT_SYS_PROMPT;
import static com.gal.smartcalender.Constants.SYS_PROMPT_TXT_PREFERENCE_NAME;
import static com.gal.smartcalender.EditSystemPromptActivity.PREF_EMPTY;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.notification.StatusBarNotification;

public abstract class DataProcessor {

    protected Context _context;
    static protected AppDatabase _db = null;

    DataProcessor(Context context) {
        _context = context;
        if(_db == null){
            _db = DataBaseSingletone.get_db(context.getApplicationContext());
        }
    }


    public abstract void processNotification(StatusBarNotification sbn);

}
