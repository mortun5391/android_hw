package com.khalilbek.hw3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteViewModel extends AndroidViewModel {
    private NoteDao noteDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        noteDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotesLiveData() {
        return noteDao.getAllNotesLiveData();
    }

    public Flowable<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public void insert(Note note) {
        disposable.add(noteDao.insert(note)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void update(Note note) {
        disposable.add(noteDao.update(note)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void delete(Note note) {
        disposable.add(noteDao.delete(note)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}