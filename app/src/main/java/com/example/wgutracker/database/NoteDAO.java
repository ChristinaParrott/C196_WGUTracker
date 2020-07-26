package com.example.wgutracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity NoteEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NoteEntity> notes);

    @Delete
    void deleteNote(NoteEntity NoteEntity);

    @Query("SELECT * FROM notes WHERE noteID = :id")
    NoteEntity getNoteByID(int id);

    @Query("SELECT * FROM notes WHERE courseID = :id")
    LiveData<List<NoteEntity>> getNoteByCourse(int id);

    @Query("DELETE FROM notes")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM notes")
    int getCount();

    @Query("SELECT * FROM notes ORDER BY noteName ASC")
    LiveData<List<NoteEntity>> getAll();
}
