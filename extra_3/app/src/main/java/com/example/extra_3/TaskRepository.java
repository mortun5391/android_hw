package com.example.extra_3;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public TaskRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "todo.db").build();
        dao = db.taskDao();
    }

    public LiveData<List<TaskEntity>> tasks(boolean sortByTitle, String filter) {
        if (filter != null && !filter.isEmpty()) return dao.filterByQuery(filter);
        return sortByTitle ? dao.allByTitleAsc() : dao.allByDateDesc();
    }

    public void add(String title) {
        io.execute(() -> dao.insert(new TaskEntity(title, false, System.currentTimeMillis())));
    }

    public void toggle(TaskEntity t) {
        io.execute(() -> {
            t.done = !t.done;
            dao.update(t);
        });
    }

    public void updateTitle(TaskEntity t, String title) {
        io.execute(() -> {
            t.title = title;
            dao.update(t);
        });
    }

    public void delete(TaskEntity t) {
        io.execute(() -> dao.delete(t));
    }
}
