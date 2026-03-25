package com.example.extra_3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskViewModel vm = new ViewModelProvider(this).get(TaskViewModel.class);
        TaskAdapter adapter = new TaskAdapter(new TaskAdapter.Listener() {
            @Override
            public void onToggle(TaskEntity task) { vm.toggleTask(task); }

            @Override
            public void onDelete(TaskEntity task) { vm.deleteTask(task); }

            @Override
            public void onUpdate(TaskEntity task, String title) { vm.updateTask(task, title); }
        });

        RecyclerView recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        EditText input = findViewById(R.id.taskInput);
        EditText filter = findViewById(R.id.filterInput);
        Button addButton = findViewById(R.id.addTaskButton);
        Switch sortSwitch = findViewById(R.id.sortSwitch);

        addButton.setOnClickListener(v -> {
            String title = input.getText().toString().trim();
            if (!title.isEmpty()) {
                vm.addTask(title);
                input.setText("");
            }
        });

        findViewById(R.id.applyFilterButton).setOnClickListener(v -> vm.setFilter(filter.getText().toString().trim()));
        sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> vm.setSortByTitle(isChecked));

        vm.getTasks().observe(this, adapter::submitList);
    }
}
