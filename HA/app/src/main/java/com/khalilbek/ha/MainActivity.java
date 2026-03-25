package com.khalilbek.ha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int DAILY_GOAL = 8;
    private static final String PREFS_NAME = "water_tracker_prefs";
    private static final String KEY_GLASSES = "glasses_count";

    private int glassesCount = 0;

    private TextView progressText;
    private TextView statusText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressText = findViewById(R.id.progressText);
        statusText = findViewById(R.id.statusText);
        progressBar = findViewById(R.id.progressBar);
        Button addButton = findViewById(R.id.addButton);
        Button removeButton = findViewById(R.id.removeButton);
        Button resetButton = findViewById(R.id.resetButton);

        progressBar.setMax(DAILY_GOAL);
        loadProgress();
        updateUi();

        addButton.setOnClickListener(v -> {
            if (glassesCount < DAILY_GOAL) {
                glassesCount++;
                saveProgress();
                updateUi();
            }
        });

        removeButton.setOnClickListener(v -> {
            if (glassesCount > 0) {
                glassesCount--;
                saveProgress();
                updateUi();
            }
        });

        resetButton.setOnClickListener(v -> {
            glassesCount = 0;
            saveProgress();
            updateUi();
        });
    }

    private void updateUi() {
        progressText.setText(getString(R.string.progress_format, glassesCount, DAILY_GOAL));
        progressBar.setProgress(glassesCount);

        if (glassesCount >= DAILY_GOAL) {
            statusText.setText(R.string.goal_done);
        } else if (glassesCount >= DAILY_GOAL / 2) {
            statusText.setText(R.string.keep_going);
        } else {
            statusText.setText(R.string.start_message);
        }
    }

    private void loadProgress() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        glassesCount = preferences.getInt(KEY_GLASSES, 0);
    }

    private void saveProgress() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        preferences.edit().putInt(KEY_GLASSES, glassesCount).apply();
    }
}
