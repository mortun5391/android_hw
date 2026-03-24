package com.khalilbek.hw5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textStatus;
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private volatile boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        textStatus = findViewById(R.id.text_status);
        recyclerView = findViewById(R.id.recycler_view);

        dataAdapter = new DataAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);

        Button buttonLoad = findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(v -> loadDataAsync());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadDataAsync() {
        if (isLoading) {
            return;
        }

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        textStatus.setText("Загрузка данных...");
        dataAdapter.clear();

        new Thread(() -> {
            final int totalItems = 20;

            for (int i = 1; i <= totalItems; i++) {
                try {
                    Thread.sleep(180); // имитация задержки, как при запросе сети
                } catch (InterruptedException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        textStatus.setText("Загрузка прервана");
                        isLoading = false;
                    });
                    return;
                }

                final String item = "Пункт " + i + " (асинхронно)";
                final int loadedCount = i;

                runOnUiThread(() -> {
                    dataAdapter.addItem(item);
                    textStatus.setText("Загружено " + loadedCount + " из " + totalItems + " элементов...");
                });
            }

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                textStatus.setText("Данные полностью загружены: " + totalItems + " элементов");
                isLoading = false;
            });
        }).start();
    }

    // Весь входной список теперь рисуется через RecyclerView + DataAdapter
    private List<String> loadFakeItems() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            list.add("Пункт " + i + " (асинхронно)");
        }
        return list;
    }
}