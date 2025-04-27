package com.gal.smartcalender;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private DataProcessor _dataProcessor = null;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Called when a notification is posted
        if (_dataProcessor == null) {
            _dataProcessor = new ChatGptDataProcessor(getApplicationContext());
        }
        _dataProcessor.processNotification(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Called when a notification is removed
        Log.d(TAG, "Notification Removed: " + sbn.getPackageName());
    }
}
