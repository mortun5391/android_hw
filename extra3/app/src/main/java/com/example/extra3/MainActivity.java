package com.example.extra3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extra3.db.AppDb;
import com.example.extra3.db.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean sortByTitle = false;
    private TaskAdapter adapter;
    private AppDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDb.get(this);
        EditText input = findViewById(R.id.inputTask);
        EditText filterInput = findViewById(R.id.filterInput);

        adapter = new TaskAdapter(task -> db.dao().delete(task), task -> showEditDialog(task));
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        ((Button) findViewById(R.id.addBtn)).setOnClickListener(v -> {
            String title = input.getText().toString().trim();
            if (!title.isEmpty()) {
                db.dao().insert(new TaskEntity(title, System.currentTimeMillis()));
                input.setText("");
            }
        });

        ((Button) findViewById(R.id.sortBtn)).setOnClickListener(v -> {
            sortByTitle = !sortByTitle;
            observe(filterInput.getText().toString());
        });

        filterInput.setOnEditorActionListener((v, actionId, event) -> {
            observe(filterInput.getText().toString());
            return false;
        });

        observe("");
    }

    private void showEditDialog(TaskEntity task) {
        EditText edit = new EditText(this);
        edit.setText(task.title);
        new AlertDialog.Builder(this)
                .setTitle("Обновить")
                .setView(edit)
                .setPositiveButton("OK", (d, w) -> {
                    task.title = edit.getText().toString();
                    db.dao().update(task);
                })
                .show();
    }

    private void observe(String filter) {
        LiveData<List<TaskEntity>> source = sortByTitle ? db.dao().getAllByTitle() : db.dao().getAllDesc();
        source.observe(this, list -> {
            List<TaskEntity> filtered = new ArrayList<>();
            for (TaskEntity t : list) {
                if (t.title != null && t.title.toLowerCase().contains(filter.toLowerCase())) filtered.add(t);
            }
            adapter.submit(filtered);
        });
    }
}
