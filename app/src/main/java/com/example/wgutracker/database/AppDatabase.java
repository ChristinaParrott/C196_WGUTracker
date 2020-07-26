package com.example.wgutracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {AssessmentEntity.class, CourseEntity.class, MentorEntity.class,
        NoteEntity.class, TermEntity.class}, version = 3)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "WGUTrackerDB.db";
    public static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract MentorDAO mentorDAO();
    public abstract NoteDAO noteDAO();
    public abstract TermDAO termDAO();

    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (LOCK) {
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
