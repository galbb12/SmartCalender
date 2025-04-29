package com.gal.smartcalender;

import static android.app.Notification.FLAG_GROUP_SUMMARY;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private DataProcessor _dataProcessor = null;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Called when a notification is posted

        if ((sbn.getNotification().flags & FLAG_GROUP_SUMMARY) != 0) {
            //Ignore the notification if its a FLAG_GROUP_SUMMARY
            return;
        }

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
