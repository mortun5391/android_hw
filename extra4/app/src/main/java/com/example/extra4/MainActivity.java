package com.example.extra4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;

public class MainActivity extends AppCompatActivity {
    private final ArrayDeque<String> stack = new ArrayDeque<>();
    private final ArrayDeque<String> queue = new ArrayDeque<>();
    private WeakReference<byte[]> leakSafeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.input);
        TextView state = findViewById(R.id.state);

        Runnable render = () -> state.setText("Stack=" + stack + "\nQueue=" + queue);

        ((Button) findViewById(R.id.pushBtn)).setOnClickListener(v -> { stack.addLast(input.getText().toString()); render.run(); });
        ((Button) findViewById(R.id.enqueueBtn)).setOnClickListener(v -> { queue.addLast(input.getText().toString()); render.run(); });
        ((Button) findViewById(R.id.popBtn)).setOnClickListener(v -> { if (!stack.isEmpty()) stack.removeLast(); render.run(); });
        ((Button) findViewById(R.id.dequeueBtn)).setOnClickListener(v -> { if (!queue.isEmpty()) queue.removeFirst(); render.run(); });
        ((Button) findViewById(R.id.clearBtn)).setOnClickListener(v -> {
            stack.clear();
            queue.clear();
            leakSafeRef = new WeakReference<>(new byte[0]);
            Runtime.getRuntime().gc();
            render.run();
        });

        render.run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stack.clear();
        queue.clear();
        if (leakSafeRef != null) leakSafeRef.clear();
    }
}
