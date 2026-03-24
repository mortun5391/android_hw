package com.khalilbek.hw3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NoteDao {
    @Insert
    Completable insert(Note note);

    @Update
    Completable update(Note note);

    @Delete
    Completable delete(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<Note>> getAllNotesLiveData();

    @Query("SELECT * FROM notes ORDER BY id DESC")
    Flowable<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    Single<Note> getNoteById(int id);
}