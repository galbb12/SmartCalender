package com.gal.smartcalender;

import static com.gal.smartcalender.Constants.APP_PREF_NAME;
import static com.gal.smartcalender.Constants.DEFAULT_SYS_PROMPT;
import static com.gal.smartcalender.Constants.SYS_PROMPT_TXT_PREFERENCE_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditSystemPromptActivity extends AppCompatActivity {

    EditText editText = null;
    Button save_btn = null;
    Button cancel_btn = null;
    static final String PREF_EMPTY = "PREF_EMPTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_system_prompt);
        editText = findViewById(R.id.promptEditText);
        editText.setText(LLMUtils.get_sys_message_template(getApplicationContext()));
        save_btn = findViewById(R.id.saveButton);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences(APP_PREF_NAME, MODE_PRIVATE);
                prefs.edit().putString(SYS_PROMPT_TXT_PREFERENCE_NAME, editText.getText().toString()).apply();
            }
        });
        cancel_btn = findViewById(R.id.cancelButton);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







    }
}