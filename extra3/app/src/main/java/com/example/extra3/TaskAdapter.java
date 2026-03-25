package com.example.extra3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extra3.db.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    public interface Action { void run(TaskEntity task); }

    private final List<TaskEntity> items = new ArrayList<>();
    private final Action onDelete;
    private final Action onEdit;

    public TaskAdapter(Action onDelete, Action onEdit) {
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    public void submit(List<TaskEntity> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton edit, delete;
        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            edit = itemView.findViewById(R.id.editBtn);
            delete = itemView.findViewById(R.id.deleteBtn);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }
    @Override public int getItemCount() { return items.size(); }

    @Override public void onBindViewHolder(@NonNull VH holder, int position) {
        TaskEntity task = items.get(position);
        holder.title.setText(task.title);
        holder.edit.setOnClickListener(v -> onEdit.run(task));
        holder.delete.setOnClickListener(v -> onDelete.run(task));
    }
}
