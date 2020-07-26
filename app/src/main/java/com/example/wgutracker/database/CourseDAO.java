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
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseEntity> courses);

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM courses WHERE courseID = :id")
    CourseEntity getCourseByID(int id);

    @Query("SELECT * FROM courses WHERE termID = :id")
    LiveData<List<CourseEntity>> getCourseByTerm(int id);

    @Query("SELECT * FROM courses ORDER BY courseStart ASC")
    LiveData<List<CourseEntity>> getAll();

    @Query("DELETE FROM courses")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM courses")
    int getCount();

    @Query("SELECT * FROM courses WHERE courseEnd BETWEEN :today AND :monthFromToday")
    LiveData<List<CourseEntity>> getCourseSoon(Date today, Date monthFromToday);

    @Query("SELECT COUNT(*) FROM courses WHERE termID = :id")
    LiveData<Integer> getCourseCount(int id);
}