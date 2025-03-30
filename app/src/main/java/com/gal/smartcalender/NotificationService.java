package com.gal.smartcalender;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Objects;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private DataProcessor dataProcessor = null;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Called when a notification is posted
        if (dataProcessor == null) {
            dataProcessor = new ChatGptDataProcessor(getApplicationContext());
        }
        dataProcessor.processNotification(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Called when a notification is removed
        Log.d(TAG, "Notification Removed: " + sbn.getPackageName());
    }
}
