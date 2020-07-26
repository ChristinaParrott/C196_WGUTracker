package com.example.wgutracker.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "terms")
public class TermEntity {
    @PrimaryKey(autoGenerate = true)
    private int termID;
    private String termName;
    private Date termStart;
    private Date termEnd;

    @Ignore
    public TermEntity() {
    }

    public TermEntity(int termID, String termName, Date termStart, Date termEnd){
        this.termID = termID;
        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    @Ignore
    public TermEntity(String name, Date start, Date end){
        this.termName = name;
        this.termStart = start;
        this.termEnd = end;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Date getTermStart() {
        return termStart;
    }

    public void setTermStart(Date termStart) {
        this.termStart = termStart;
    }

    public Date getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(Date termEnd) {
        this.termEnd = termEnd;
    }

    @Override
    public String toString() {
        return termName;
    }
}
