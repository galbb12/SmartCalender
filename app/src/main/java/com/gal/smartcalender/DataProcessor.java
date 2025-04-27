package com.gal.smartcalender;

import android.content.Context;
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
