package com.example.extra_3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    private final MutableLiveData<Boolean> sortByTitle = new MutableLiveData<>(false);
    private final MutableLiveData<String> filter = new MutableLiveData<>("");
    private final MediatorLiveData<List<TaskEntity>> tasks = new MediatorLiveData<>();
    private LiveData<List<TaskEntity>> currentSource;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        refresh();
    }

    public LiveData<List<TaskEntity>> getTasks() { return tasks; }

    public void addTask(String title) { repository.add(title); }
    public void toggleTask(TaskEntity task) { repository.toggle(task); }
    public void updateTask(TaskEntity task, String title) { repository.updateTitle(task, title); }
    public void deleteTask(TaskEntity task) { repository.delete(task); }

    public void setSortByTitle(boolean enabled) {
        sortByTitle.setValue(enabled);
        refresh();
    }

    public void setFilter(String value) {
        filter.setValue(value);
        refresh();
    }

    private void refresh() {
        if (currentSource != null) tasks.removeSource(currentSource);
        currentSource = repository.tasks(Boolean.TRUE.equals(sortByTitle.getValue()), filter.getValue());
        tasks.addSource(currentSource, tasks::setValue);
    }
}
