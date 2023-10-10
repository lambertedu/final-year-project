package com.example.ocrtranslator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class text_speech extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 1;


    private TextToSpeech TTS;
    private EditText editText;
    private SeekBar pitchbar;
    private SeekBar speedbar;
    private ImageButton ButtonSpeak;
   private Button copy;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_speech);

        editText = findViewById(R.id.ed_text);
        pitchbar = findViewById(R.id.bar_pitch);
        speedbar = findViewById(R.id.bar_speed);
        copy = findViewById(R.id.copy);
        ButtonSpeak= findViewById(R.id.speak_button);

        //text to speech/
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = TTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","Language not supported");
                    }else {
                        ButtonSpeak.setEnabled(true);
                    }
                }else {
                    Log.e("TTS", "Initialization failed");
                }

            }
        });

        ButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });




        //copy button
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                copyToClipBoard(text);
            }
        });
    }

    //for the pitch and speed bar
    private void speak() {
        String text = editText.getText().toString();
        Float pitch = (float) pitchbar.getProgress() /50;
        if (pitch < 0.1) pitch = 0.1f;
        Float speed = (float) speedbar.getProgress() /50;
        if (speed < 0.1) speed = 0.1f;
        TTS.setPitch(pitch);
        TTS.setSpeechRate(speed);

        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    // function to stop the speech
    @Override
    protected void onDestroy() {
        if (TTS != null){
            TTS.stop();
            TTS.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data!= null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(text.get(0));

                }
                break;
        }
    }

    // copy text to clipboard
    private void copyToClipBoard(String text){
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied text",text);
        clipBoard.setPrimaryClip(clip);
        Toast.makeText(text_speech.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }
}

