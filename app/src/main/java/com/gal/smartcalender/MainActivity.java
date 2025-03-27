package com.gal.smartcalender;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Button send_button = null ;
    EditText text_field_view = null;
    ChatGptApi chatGptApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        chatGptApi = new ChatGptApi(com.gal.smartcalender.BuildConfig.CHATGPT_API_KEY);
        text_field_view = findViewById(R.id.editTextText);
        send_button = findViewById(R.id.Send);

        Callback ok_http_callback =  new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();  // Handle network failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.err.println("Unexpected code " + response);
                    return;
                }

                String responseData = response.body().string();
                System.out.println("Response: " + responseData);

                // If updating UI, use runOnUiThread() inside an Activity
            }
        };

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatGptApi.sendQuery(text_field_view.getText().toString(), ok_http_callback);
            }
        });


    }


}


