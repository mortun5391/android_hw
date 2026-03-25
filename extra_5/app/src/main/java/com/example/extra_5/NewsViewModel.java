package com.example.extra_5;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(application);
    }

    public LiveData<List<NewsEntity>> news() { return repository.observeNews(); }
    public LiveData<String> error() { return error; }

    public void refresh() {
        repository.refreshNews(error::postValue);
    }
}
