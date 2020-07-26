package com.example.wgutracker.utilities;

import com.example.wgutracker.database.CourseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleCourseData {
    private static final String COURSE_NAME_1 = "Software Dev. 2";
    private static final String COURSE_NAME_2 = "Mobile App Dev.";
    private static final String COURSE_NAME_3 = "Software Dev. Capstone";

    private static final String COURSE_STATUS_1 = "In progress";
    private static final String COURSE_STATUS_2 = "Complete";
    private static final String COURSE_STATUS_3 = "Not started";


    private static Date getDate(int month, int day, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static List<CourseEntity> getCourses(){
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity(COURSE_NAME_1, getDate(5, 15, 2020), getDate(5,17,2020), COURSE_STATUS_1, 1, 1));
        courses.add(new CourseEntity(COURSE_NAME_2, getDate(7, 25, 2020), getDate(9,30,2020), COURSE_STATUS_2, 2, 2));
        courses.add(new CourseEntity(COURSE_NAME_3, getDate(1, 15, 2021), getDate(2,1,2021), COURSE_STATUS_3, 3, 3));
        return courses;
    }

}
