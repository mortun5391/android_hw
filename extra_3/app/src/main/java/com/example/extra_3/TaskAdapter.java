package com.example.extra_3;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {
    public interface Listener {
        void onToggle(TaskEntity task);
        void onDelete(TaskEntity task);
        void onUpdate(TaskEntity task, String title);
    }

    private final List<TaskEntity> items = new ArrayList<>();
    private final Listener listener;

    public TaskAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<TaskEntity> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TaskEntity task = items.get(position);
        h.checkBox.setText(task.title);
        h.checkBox.setChecked(task.done);
        h.checkBox.setPaintFlags(task.done ? h.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : h.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        h.checkBox.setOnClickListener(v -> listener.onToggle(task));
        h.deleteButton.setOnClickListener(v -> listener.onDelete(task));
        h.editButton.setOnClickListener(v -> showEditDialog(h.itemView, task));
    }

    private void showEditDialog(View anchor, TaskEntity task) {
        TextInputEditText input = new TextInputEditText(anchor.getContext());
        input.setText(task.title);
        new AlertDialog.Builder(anchor.getContext())
                .setTitle("Изменить задачу")
                .setView(input)
                .setPositiveButton("Сохранить", (d, w) -> listener.onUpdate(task, String.valueOf(input.getText())))
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageButton editButton;
        ImageButton deleteButton;
        Holder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
