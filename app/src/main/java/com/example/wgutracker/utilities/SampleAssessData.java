package com.example.wgutracker.utilities;

import com.example.wgutracker.database.AssessmentEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleAssessData {
    private static final String ASSESS_NAME_1 = "Scheduler App";
    private static final String ASSESS_NAME_2 = "Mobile App";
    private static final String ASSESS_NAME_3 = "Capstone";

    private static final String ASSESS_TYPE_1 = "Performance Assessment";
    private static final String ASSESS_TYPE_2 = "Objective Assessment";

    private static Date getDate(int month, int day, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static List<AssessmentEntity> getAssessments(){
        List<AssessmentEntity> assessments = new ArrayList<>();
        assessments.add(new AssessmentEntity(ASSESS_NAME_1, ASSESS_TYPE_1, getDate(5, 29, 2020), 1));
        assessments.add(new AssessmentEntity(ASSESS_NAME_2, ASSESS_TYPE_2, getDate(11, 2, 2020), 2));
        assessments.add(new AssessmentEntity(ASSESS_NAME_3, ASSESS_TYPE_2, getDate(3, 2, 2021), 3));
        return assessments;
    }
}
