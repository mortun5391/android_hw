package com.khalilbek.hw9;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khalilbek.hw9.adapter.UserAdapter;
import com.khalilbek.hw9.api.RetrofitClient;
import com.khalilbek.hw9.api.UserApi;
import com.khalilbek.hw9.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private UserAdapter userAdapter;
    private Call<List<User>> currentCall;
    private Thread loadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        loadUsers();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new java.util.ArrayList<>());
        recyclerView.setAdapter(userAdapter);
    }

    private void loadUsers() {
        // Загружаем данные в фоновом потоке
        loadThread = new Thread(() -> {
            try {
                UserApi userApi = RetrofitClient.getUserApi();
                currentCall = userApi.getUsers();

                // Синхронный вызов в фоновом потоке
                Response<List<User>> response = currentCall.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    // Обновляем UI в главном потоке
                    new Handler(Looper.getMainLooper()).post(() -> {
                        showUsers(users);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        showError("Failed to load users: " + response.code());
                    });
                }
            } catch (Exception e) {
                // Проверяем, что Activity ещё активен
                if (!isDestroyed() && !isFinishing()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        showError("Error: " + e.getMessage());
                    });
                }
            }
        });

        loadThread.start();
    }

    private void showUsers(List<User> users) {
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        userAdapter.updateUsers(users);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Отменяем запрос при закрытии Activity
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
        // Прерываем поток загрузки
        if (loadThread != null && loadThread.isAlive()) {
            loadThread.interrupt();
        }
    }
}
