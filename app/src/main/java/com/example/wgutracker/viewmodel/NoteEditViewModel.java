package com.example.wgutracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.NoteEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteEditViewModel extends AndroidViewModel {
    public MutableLiveData<NoteEntity> mLiveNote =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    public NoteEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));

    }
    public void loadData(int noteId) {
        executor.execute(() -> {
            NoteEntity note = mRepository.getNoteById(noteId);
            mLiveNote.postValue(note);
        });
    }
    public void saveNote(String name, String text, Integer courseID) {
        NoteEntity note = mLiveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(name.trim()) ||
                    TextUtils.isEmpty(text.trim()) ||
                    courseID == 0) {
                return;
            }
            note = new NoteEntity(name.trim(), text.trim(), courseID);
        }
        else {
            note.setNoteName(name.trim());
            note.setNoteText(text.trim());
            note.setCourseID(courseID);
        }
        mRepository.insertNote(note);
    }
    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}

