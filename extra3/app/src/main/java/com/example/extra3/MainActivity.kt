package com.example.extra3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extra3.db.AppDb
import com.example.extra3.db.TaskEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TaskAdapter
    private var sortByTitle = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = AppDb.get(this)
        val input = findViewById<EditText>(R.id.inputTask)
        val filterInput = findViewById<EditText>(R.id.filterInput)

        adapter = TaskAdapter(onDelete = { lifecycleScope.launch { db.dao().delete(it) } }, onEdit = { task ->
            val edit = EditText(this).apply { setText(task.title) }
            AlertDialog.Builder(this).setTitle("Обновить")
                .setView(edit)
                .setPositiveButton("OK") { _, _ -> lifecycleScope.launch { db.dao().update(task.copy(title = edit.text.toString())) } }
                .show()
        })
        findViewById<RecyclerView>(R.id.recycler).apply { layoutManager = LinearLayoutManager(this@MainActivity); adapter = this@MainActivity.adapter }

        findViewById<Button>(R.id.addBtn).setOnClickListener {
            lifecycleScope.launch { db.dao().insert(TaskEntity(title = input.text.toString())); input.text.clear() }
        }
        findViewById<Button>(R.id.sortBtn).setOnClickListener { sortByTitle = !sortByTitle; observe(db, filterInput.text.toString()) }
        filterInput.setOnEditorActionListener { _, _, _ -> observe(db, filterInput.text.toString()); false }

        observe(db, "")
    }

    private fun observe(db: AppDb, filter: String) {
        lifecycleScope.launch {
            (if (sortByTitle) db.dao().getAllByTitle() else db.dao().getAllDesc()).collectLatest {
                adapter.submit(it.filter { t -> t.title.contains(filter, true) })
            }
        }
    }
}
