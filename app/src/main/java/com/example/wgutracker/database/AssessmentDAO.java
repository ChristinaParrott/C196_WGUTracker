package com.example.wgutracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity AssessmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AssessmentEntity> assessments);

    @Delete
    void deleteAssessment(AssessmentEntity AssessmentEntity);

    @Query("SELECT * FROM assessments WHERE assessID = :id")
    AssessmentEntity getAssessmentByID(int id);

    @Query("SELECT * FROM assessments WHERE courseID = :id")
    LiveData<List<AssessmentEntity>> getAssessmentByCourse(int id);

    @Query("SELECT * FROM assessments ORDER BY assessDate ASC")
    LiveData<List<AssessmentEntity>> getAll();

    @Query("DELETE FROM assessments")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM assessments")
    int getCount();

    @Query("SELECT * FROM assessments WHERE assessDate BETWEEN :today AND :weekFromToday")
    LiveData<List<AssessmentEntity>> getAssessSoon(Date today, Date weekFromToday);

    @Query("SELECT COUNT(*) FROM assessments WHERE courseID = :courseId")
    LiveData<Integer> getAssessCount(int courseId);
}
