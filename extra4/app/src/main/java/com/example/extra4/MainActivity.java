package com.example.extra4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private final Stack<String> stack = new Stack<>();
    private final Queue<String> queue = new ArrayDeque<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final StateUpdater updater = new StateUpdater(this);

    private TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.input);
        state = findViewById(R.id.state);

        Button pushBtn = findViewById(R.id.pushBtn);
        Button popBtn = findViewById(R.id.popBtn);
        Button enqueueBtn = findViewById(R.id.enqueueBtn);
        Button dequeueBtn = findViewById(R.id.dequeueBtn);
        Button clearBtn = findViewById(R.id.clearBtn);

        pushBtn.setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (!value.isEmpty()) stack.push(value);
            render();
        });

        popBtn.setOnClickListener(v -> {
            if (!stack.isEmpty()) stack.pop();
            render();
        });

        enqueueBtn.setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (!value.isEmpty()) queue.offer(value);
            render();
        });

        dequeueBtn.setOnClickListener(v -> {
            queue.poll();
            render();
        });

        clearBtn.setOnClickListener(v -> clearMemory());

        handler.postDelayed(updater, 1000);
        render();
    }

    private void clearMemory() {
        stack.clear();
        queue.clear();
        handler.removeCallbacksAndMessages(null);
        render();
    }

    private void render() {
        state.setText("Stack: " + stack + "\nQueue: " + queue
                + "\n\nПроверка утечек: используйте Android Profiler > Memory и наблюдайте освобождение MainActivity после clear/onDestroy.");
    }

    @Override
    protected void onDestroy() {
        clearMemory();
        state = null;
        super.onDestroy();
    }

    private static class StateUpdater implements Runnable {
        private final WeakReference<MainActivity> ref;

        StateUpdater(MainActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            MainActivity activity = ref.get();
            if (activity != null && activity.state != null) {
                activity.render();
                activity.handler.postDelayed(this, 1000);
            }
        }
    }
}
