package com.gal.smartcalender;

import android.service.notification.StatusBarNotification;
import android.util.Pair;

public abstract class DataProcessor {
    public abstract void processNotification(StatusBarNotification sbn); // First term is data source and second is the data

}
