package com.khalilbek.hw3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private EditText editTitle, editContent;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private NoteViewModel noteViewModel;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        buttonAdd = findViewById(R.id.button_add);
        buttonUpdate = findViewById(R.id.button_update);
        buttonDelete = findViewById(R.id.button_delete);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new NoteAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Observe notes using LiveData
        noteViewModel.getAllNotesLiveData().observe(this, notes -> adapter.setNotes(notes));

        // Alternatively, using RxJava Flowable (commented out)
        // disposable.add(noteViewModel.getAllNotes()
        //         .subscribeOn(Schedulers.io())
        //         .observeOn(AndroidSchedulers.mainThread())
        //         .subscribe(notes -> adapter.setNotes(notes)));

        buttonAdd.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();
            if (!title.isEmpty() && !content.isEmpty()) {
                Note note = new Note(title, content);
                noteViewModel.insert(note);
                clearFields();
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpdate.setOnClickListener(v -> {
            if (selectedNote != null) {
                selectedNote.setTitle(editTitle.getText().toString().trim());
                selectedNote.setContent(editContent.getText().toString().trim());
                if (!selectedNote.getTitle().isEmpty() && !selectedNote.getContent().isEmpty()) {
                    noteViewModel.update(selectedNote);
                    clearFields();
                    selectedNote = null;
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select a note to update", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (selectedNote != null) {
                noteViewModel.delete(selectedNote);
                clearFields();
                selectedNote = null;
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Select a note to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNoteClick(Note note) {
        selectedNote = note;
        editTitle.setText(note.getTitle());
        editContent.setText(note.getContent());
    }

    private void clearFields() {
        editTitle.setText("");
        editContent.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}