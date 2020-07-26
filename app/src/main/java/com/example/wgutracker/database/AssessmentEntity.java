package com.example.wgutracker.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "assessments",
    foreignKeys = @ForeignKey(entity = CourseEntity.class,
    parentColumns = "courseID",
    childColumns = "courseID",
    onDelete = ForeignKey.CASCADE))

public class AssessmentEntity {
    @PrimaryKey(autoGenerate = true)
    private int assessID;
    private String assessName;
    private String assessType;
    private Date assessDate;
    private int courseID;

    @Ignore
    public AssessmentEntity(){
    }

    public AssessmentEntity(int assessID, String assessName,
                            String assessType, Date assessDate, int courseID) {
        this.assessID = assessID;
        this.assessName = assessName;
        this.assessType = assessType;
        this.assessDate = assessDate;
        this.courseID = courseID;
    }

    @Ignore
    public AssessmentEntity(String assessName, String assessType, Date assessDate, int courseID) {
        this.assessName = assessName;
        this.assessType = assessType;
        this.assessDate = assessDate;
        this.courseID = courseID;
    }

    public int getAssessID() {
        return assessID;
    }

    public void setAssessID(int assessID) {
        this.assessID = assessID;
    }

    public String getAssessName() {
        return assessName;
    }

    public void setAssessName(String assessName) {
        this.assessName = assessName;
    }

    public String getAssessType() {
        return assessType;
    }

    public void setAssessType(String assessType) {
        this.assessType = assessType;
    }

    public Date getAssessDate() {
        return assessDate;
    }

    public void setAssessDate(Date assessDate) {
        this.assessDate = assessDate;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
