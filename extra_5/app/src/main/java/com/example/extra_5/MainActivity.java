package com.example.extra_5;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsViewModel vm = new ViewModelProvider(this).get(NewsViewModel.class);
        NewsAdapter adapter = new NewsAdapter();
        recyclerView = findViewById(R.id.newsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView error = findViewById(R.id.errorText);
        findViewById(R.id.refreshButton).setOnClickListener(v -> vm.refresh());

        vm.news().observe(this, adapter::submitList);
        vm.error().observe(this, msg -> {
            if (msg != null) error.setText(msg);
        });

        vm.refresh();
    }

    @Override
    protected void onDestroy() {
        if (recyclerView != null) recyclerView.setAdapter(null);
        super.onDestroy();
    }
}
