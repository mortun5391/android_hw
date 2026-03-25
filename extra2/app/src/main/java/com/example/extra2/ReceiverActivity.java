package com.example.extra2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        String text = getIntent().getStringExtra("text");
        int imageRes = getIntent().getIntExtra("imageRes", android.R.drawable.ic_menu_gallery);

        ((TextView) findViewById(R.id.receivedText)).setText(text);
        ((ImageView) findViewById(R.id.receivedImage)).setImageResource(imageRes);

        ((Button) findViewById(R.id.btnBackResult)).setOnClickListener(v -> {
            Intent out = new Intent().putExtra("result", "Получено: " + text);
            setResult(Activity.RESULT_OK, out);
            finish();
        });
    }
}
