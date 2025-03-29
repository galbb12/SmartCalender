package com.gal.smartcalender;

import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// An helpful class for flowing the data through chatgpt
public class ChatGptDataProcessor extends DataProcessor {
    private ChatGptApi _chatGptApi = null;

    ChatGptDataProcessor(){
        this._chatGptApi = new ChatGptApi(com.gal.smartcalender.BuildConfig.CHATGPT_API_KEY, "gpt-3.5-turbo");
    }

    //Decode the notification for chatgpt
    @Override
    public void processNotification(StatusBarNotification sbn) {
        Gson gson = new Gson();
        Map<String, Object> noti_obj = new HashMap<>();
        String packageName = sbn.getPackageName();
        Log.d("package name", packageName);
        noti_obj.put("package name", packageName);
        for (String key : sbn.getNotification().extras.keySet()) {
            Object value = sbn.getNotification().extras.get(key);
            if(value != null){
                Log.d(key, value.toString());
                noti_obj.put(key, value.toString());
            }

        }

        String jsonArray = gson.toJson(noti_obj);
        Log.d("Json array", jsonArray);


    }
}
