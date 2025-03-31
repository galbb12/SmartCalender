package com.gal.smartcalender;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

                Gson gson = new Gson();
                if(response.body() != null){
                    String resp = response.body().string();
                    Log.d("response:", resp);
                    JsonObject jsonObject = JsonParser.parseString(resp).getAsJsonObject();
                    JsonArray choices = jsonObject.getAsJsonArray("choices");
                    JsonObject firstChoice = choices.get(0).getAsJsonObject();
                    JsonObject message = firstChoice.getAsJsonObject("message");
                    String content = message.get("content").getAsString();
                    if(content.equals(Constants.no_event_ret)){
                        Log.d("ChatGpt saied:", "No event");
                        return;
                    }

                    JsonObject jsonContent = gson.fromJson(content, JsonObject.class);

                    Log.d("Json parsed description", jsonContent.get("description").getAsString());
                    String descriptionStr = jsonContent.get("description").getAsString();
                    String startDateStr = jsonContent.get("start_date").getAsString();
                    String endDateStr = jsonContent.get("end_date").getAsString();
                    String urgencyStr = jsonContent.get("urgency").getAsString();
                    String importanceStr = jsonContent.get("importance").getAsString();

                    Event event = new Event();
                    event.data = resp;
                    event.dataSource = "notification";
                    event.eventInfo = descriptionStr;
                    event.urgency = Float.parseFloat(urgencyStr);
                    event.importance = Float.parseFloat(importanceStr);
                    event.startDate = ZonedDateTime.parse(startDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    event.endDate = ZonedDateTime.parse(endDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    _db.EventsDao().insertAll(event);


                }
            }
        });


    }
}
