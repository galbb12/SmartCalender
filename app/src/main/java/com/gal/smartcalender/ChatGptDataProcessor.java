package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// An helpful class for flowing the data through chatgpt
public class ChatGptDataProcessor extends DataProcessor {
    private final String GPT_MODEL = "gpt-3.5-turbo";
    private ChatGptApi _chatGptApi = null;

    ChatGptDataProcessor(Context context){
        super(context);
        this._chatGptApi = new ChatGptApi(com.gal.smartcalender.BuildConfig.CHATGPT_API_KEY, GPT_MODEL);
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

        String jsonObj = gson.toJson(noti_obj);
        Log.d("Json array", jsonObj);

        Object[] messages = new Object[]{
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", generate_system_message());
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", jsonObj);
                }}
        };

        this._chatGptApi.sendQuery(messages, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("Got response", response.body().string());
            }
        });


    }
}
