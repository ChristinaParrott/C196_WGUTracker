package com.example.wgutracker.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = CourseEntity.class,
                parentColumns = "courseID", childColumns = "courseID",
                onDelete = ForeignKey.CASCADE))

public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int noteID;
    private String noteName;
    private String noteText;
    private int courseID;

    @Ignore
    public NoteEntity(){
    }

    public NoteEntity(int noteID, String noteName, String noteText, int courseID) {
        this.noteID = noteID;
        this.noteName = noteName;
        this.noteText = noteText;
        this.courseID = courseID;
    }

    @Ignore
    public NoteEntity(String noteName, String noteText, int courseID) {
        this.noteName = noteName;
        this.noteText = noteText;
        this.courseID = courseID;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
