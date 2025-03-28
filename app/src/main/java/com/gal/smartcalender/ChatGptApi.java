package com.gal.smartcalender;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatGptApi {

    private String _api_key;
    private String _model;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    ChatGptApi(String api_key, String model){
        this._api_key = api_key;
        this._model = model;
    }

    public void sendQuery(Object[] messages , Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", _model);

        requestBody.put("messages", messages);

        Gson gson = new Gson();
        String json = gson.toJson(requestBody);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + _api_key)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json; charset=utf-8")))
                .build();


        client.newCall(request).enqueue(callback);
    }

}
