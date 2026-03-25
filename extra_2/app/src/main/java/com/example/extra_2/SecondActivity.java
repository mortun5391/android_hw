package com.example.extra_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String text = getIntent().getStringExtra(MainActivity.EXTRA_TEXT);
        String imageUri = getIntent().getStringExtra(MainActivity.EXTRA_IMAGE_URI);

        TextView receivedText = findViewById(R.id.receivedText);
        ImageView receivedImage = findViewById(R.id.receivedImage);
        EditText resultInput = findViewById(R.id.resultInput);
        Button returnButton = findViewById(R.id.returnButton);

        receivedText.setText(text == null ? "" : text);
        if (imageUri != null && !imageUri.isEmpty()) {
            receivedImage.setImageURI(Uri.parse(imageUri));
        }

        returnButton.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra(MainActivity.EXTRA_RESULT, resultInput.getText().toString());
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
