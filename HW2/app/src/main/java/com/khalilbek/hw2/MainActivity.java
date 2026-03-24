package com.khalilbek.hw2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnExplicit, btnBrowser, btnDial, btnEmail, btnMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnExplicit = findViewById(R.id.btnExplicit);
        btnBrowser = findViewById(R.id.btnBrowser);
        btnDial = findViewById(R.id.btnDial);
        btnEmail = findViewById(R.id.btnEmail);
        btnMap = findViewById(R.id.btnMap);

        // Explicit Intent
        btnExplicit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        // Implicit Intent - Browser
        btnBrowser.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"));
            startActivity(intent);
        });

        // Implicit Intent - Dialer
        btnDial.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+123456789"));
            startActivity(intent);
        });

        // Implicit Intent - Email
        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:test@example.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Тестовое письмо");
            intent.putExtra(Intent.EXTRA_TEXT, "Здравствуйте! Это сообщение из приложения.");
            startActivity(intent);
        });

        // Implicit Intent - Map
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:55.7558,37.6173?q=Москва"));
            startActivity(intent);
        });
    }
}