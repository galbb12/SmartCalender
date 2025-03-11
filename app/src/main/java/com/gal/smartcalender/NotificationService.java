package com.gal.smartcalender;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Objects;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Called when a notification is posted
        String packageName = sbn.getPackageName();
        String title = Objects.requireNonNull(sbn.getNotification().extras.getCharSequence("android.title")).toString();
        String text = Objects.requireNonNull(sbn.getNotification().extras.getCharSequence("android.text")).toString();
        Log.d(TAG, "Notification Posted: " + packageName + " Title: " + title + " Text: " + text);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Called when a notification is removed
        Log.d(TAG, "Notification Removed: " + sbn.getPackageName());
    }
}
