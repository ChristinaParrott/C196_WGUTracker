package com.example.wgutracker.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courses",
    foreignKeys = {
        @ForeignKey(entity = TermEntity.class,
        parentColumns = "termID", childColumns = "termID",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = MentorEntity.class,
        parentColumns = "mentorID", childColumns = "mentorID",
        onDelete = ForeignKey.CASCADE)}
)

public class CourseEntity {
    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private String courseName;
    private Date courseStart;
    private Date courseEnd;
    private String courseStatus;
    private int termID;
    private int mentorID;

    @Ignore
    public CourseEntity(){
    }

    public CourseEntity(int courseID, String courseName, Date courseStart,
                        Date courseEnd, String courseStatus, int termID, int mentorID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.termID = termID;
        this.mentorID = mentorID;
    }

    @Ignore
    public CourseEntity(String courseName, Date courseStart, Date courseEnd,
                        String courseStatus, int termID, int mentorID) {
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.termID = termID;
        this.mentorID = mentorID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(Date courseStart) {
        this.courseStart = courseStart;
    }

    public Date getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(Date courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public int getMentorID() {
        return mentorID;
    }

    public void setMentorID(int mentorID) {
        this.mentorID = mentorID;
    }
    @Override
    public String toString(){
        return courseName;
    }
}

