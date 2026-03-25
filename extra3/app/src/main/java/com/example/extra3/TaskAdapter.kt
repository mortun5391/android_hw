package com.example.extra3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.extra3.db.TaskEntity

class TaskAdapter(private val onDelete: (TaskEntity) -> Unit, private val onEdit: (TaskEntity) -> Unit) : RecyclerView.Adapter<TaskAdapter.VH>() {
    private val items = mutableListOf<TaskEntity>()
    fun submit(list: List<TaskEntity>) { items.clear(); items.addAll(list); notifyDataSetChanged() }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val edit: ImageButton = itemView.findViewById(R.id.editBtn)
        val delete: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val task = items[position]
        holder.title.text = task.title
        holder.edit.setOnClickListener { onEdit(task) }
        holder.delete.setOnClickListener { onDelete(task) }
    }
}
