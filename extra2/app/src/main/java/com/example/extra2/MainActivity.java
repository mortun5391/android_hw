package com.example.extra2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView resultText = findViewById(R.id.resultText);
        EditText input = findViewById(R.id.inputText);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
            String result = res.getData() != null ? res.getData().getStringExtra("result") : "нет данных";
            resultText.setText("Результат: " + result);
        });

        findViewById(R.id.btnExplicit).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReceiverActivity.class);
            intent.putExtra("text", input.getText().toString());
            intent.putExtra("imageRes", android.R.drawable.ic_menu_gallery);
            launcher.launch(intent);
        });

        findViewById(R.id.btnImplicit).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, input.getText().toString());
            startActivity(Intent.createChooser(share, "Отправить текст"));
        });
    }
}
