package com.gal.smartcalender;

import android.content.Context;

import androidx.room.Room;

public class DataBaseSingletone {
    static private AppDatabase _db = null;

    public static AppDatabase get_db(Context context){
        if(_db == null){
            _db= Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.db_name).build();
        }

        return _db;
    }
}
