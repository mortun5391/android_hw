package com.example.extra_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "extra_text";
    public static final String EXTRA_IMAGE_URI = "extra_image_uri";
    public static final String EXTRA_RESULT = "extra_result";

    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ((ImageView) findViewById(R.id.previewImage)).setImageURI(uri);
                }
            });

    private final ActivityResultLauncher<Intent> secondActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String value = result.getData().getStringExtra(EXTRA_RESULT);
                    ((TextView) findViewById(R.id.resultText)).setText("Возврат: " + value);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pickImage = findViewById(R.id.pickImageButton);
        Button explicitSend = findViewById(R.id.explicitButton);
        Button implicitSend = findViewById(R.id.implicitButton);

        pickImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        explicitSend.setOnClickListener(v -> sendToSecond(true));
        implicitSend.setOnClickListener(v -> sendToSecond(false));
    }

    private void sendToSecond(boolean explicit) {
        String text = ((EditText) findViewById(R.id.inputText)).getText().toString();
        Intent intent;
        if (explicit) {
            intent = new Intent(this, SecondActivity.class);
        } else {
            intent = new Intent("com.example.extra_2.ACTION_SHOW_DATA");
            intent.setPackage(getPackageName());
        }
        intent.putExtra(EXTRA_TEXT, text);
        intent.putExtra(EXTRA_IMAGE_URI, selectedImageUri != null ? selectedImageUri.toString() : "");
        secondActivityLauncher.launch(intent);
    }
}
