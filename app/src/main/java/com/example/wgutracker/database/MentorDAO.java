package com.example.wgutracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MentorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(MentorEntity MentorEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MentorEntity> mentors);

    @Delete
    void deleteMentor(MentorEntity MentorEntity);

    @Query("SELECT * FROM mentors WHERE mentorID = :id")
    MentorEntity getMentorByID(int id);

    @Query("DELETE FROM mentors")
    int deleteAll();

    @Query("SELECT * FROM mentors ORDER BY mentorName ASC")
    LiveData<List<MentorEntity>> getAll();
}
