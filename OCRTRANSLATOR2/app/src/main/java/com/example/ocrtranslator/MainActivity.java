package com.example.ocrtranslator;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button scan;
    public Button text_speech;
    public Button translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.scan);
        text_speech = findViewById(R.id.text_speech);
        translate = findViewById(R.id.translate);

        //ocr scanning button
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,scanner.class);
                startActivity(intent);
            }
        });

        //ocr translator button
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,translator.class);
                startActivity(intent);
            }
        });

        //text to speech button
        text_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,text_speech.class);
                startActivity(intent);
            }
        });

    }
}