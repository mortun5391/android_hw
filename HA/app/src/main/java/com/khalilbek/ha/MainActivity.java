package com.khalilbek.ha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_GOAL_ML = 2000;
    private static final int STEP_SMALL = 250;
    private static final int STEP_BIG = 500;

    private static final String PREFS_NAME = "water_tracker_prefs";
    private static final String KEY_CURRENT_ML = "current_ml";
    private static final String KEY_GOAL_ML = "goal_ml";

    private static final String[] TIPS = {
            "Держите бутылку воды на рабочем столе 💧",
            "Пейте немного воды перед каждым приёмом пищи 🍽️",
            "Добавьте лимон или мяту для вкуса 🍋",
            "Ставьте напоминания каждые 2 часа ⏰",
            "После тренировки выпейте минимум 300 мл 🏃"
    };

    private int currentMl = 0;
    private int goalMl = DEFAULT_GOAL_ML;

    private EditText goalInput;
    private TextView progressText;
    private TextView percentText;
    private TextView statusText;
    private TextView tipText;
    private ImageView statusImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalInput = findViewById(R.id.goalInput);
        progressText = findViewById(R.id.progressText);
        percentText = findViewById(R.id.percentText);
        statusText = findViewById(R.id.statusText);
        tipText = findViewById(R.id.tipText);
        statusImage = findViewById(R.id.statusImage);
        progressBar = findViewById(R.id.progressBar);

        Button setGoalButton = findViewById(R.id.setGoalButton);
        Button add250Button = findViewById(R.id.add250Button);
        Button add500Button = findViewById(R.id.add500Button);
        Button minus250Button = findViewById(R.id.minus250Button);
        Button randomTipButton = findViewById(R.id.randomTipButton);
        Button resetButton = findViewById(R.id.resetButton);

        loadProgress();
        goalInput.setText(String.valueOf(goalMl));
        updateUi();

        setGoalButton.setOnClickListener(v -> applyGoalFromInput());

        add250Button.setOnClickListener(v -> {
            currentMl += STEP_SMALL;
            saveProgress();
            updateUi();
        });

        add500Button.setOnClickListener(v -> {
            currentMl += STEP_BIG;
            saveProgress();
            updateUi();
        });

        minus250Button.setOnClickListener(v -> {
            currentMl = Math.max(0, currentMl - STEP_SMALL);
            saveProgress();
            updateUi();
        });

        randomTipButton.setOnClickListener(v -> {
            int i = new Random().nextInt(TIPS.length);
            tipText.setText(TIPS[i]);
        });

        resetButton.setOnClickListener(v -> {
            currentMl = 0;
            saveProgress();
            updateUi();
        });
    }

    private void applyGoalFromInput() {
        String input = goalInput.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, R.string.goal_input_error, Toast.LENGTH_SHORT).show();
            return;
        }

        int parsed;
        try {
            parsed = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.goal_input_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (parsed < 500 || parsed > 6000) {
            Toast.makeText(this, R.string.goal_range_error, Toast.LENGTH_SHORT).show();
            return;
        }

        goalMl = parsed;
        if (currentMl > goalMl) {
            currentMl = goalMl;
        }
        saveProgress();
        updateUi();
        Toast.makeText(this, R.string.goal_saved, Toast.LENGTH_SHORT).show();
    }

    private void updateUi() {
        progressBar.setMax(goalMl);
        progressBar.setProgress(Math.min(currentMl, goalMl));

        int percent = (int) ((currentMl * 100f) / goalMl);
        progressText.setText(getString(R.string.progress_ml_format, currentMl, goalMl));
        percentText.setText(getString(R.string.progress_percent_format, percent));

        if (currentMl >= goalMl) {
            statusText.setText(R.string.goal_done);
            statusImage.setImageResource(R.drawable.ic_goal_badge);
        } else if (percent >= 50) {
            statusText.setText(R.string.keep_going);
            statusImage.setImageResource(R.drawable.ic_water_drop);
        } else {
            statusText.setText(R.string.start_message);
            statusImage.setImageResource(R.drawable.ic_water_drop);
        }

        if (tipText.getText().toString().isEmpty()) {
            tipText.setText(String.format(Locale.getDefault(), getString(R.string.tip_default), TIPS[0]));
        }
    }

    private void loadProgress() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentMl = preferences.getInt(KEY_CURRENT_ML, 0);
        goalMl = preferences.getInt(KEY_GOAL_ML, DEFAULT_GOAL_ML);
        if (goalMl < 500) {
            goalMl = DEFAULT_GOAL_ML;
        }
    }

    private void saveProgress() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        preferences.edit()
                .putInt(KEY_CURRENT_ML, currentMl)
                .putInt(KEY_GOAL_ML, goalMl)
                .apply();
    }
}
