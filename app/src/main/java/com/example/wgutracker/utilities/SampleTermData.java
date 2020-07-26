package com.example.wgutracker.utilities;

import com.example.wgutracker.database.TermEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleTermData {

    private static final String TERM_NAME_1 = "Summer 2020";
    private static final String TERM_NAME_2 = "Fall 2020";
    private static final String TERM_NAME_3 = "Spring 2021";

    private static Date getDate(int month, int day, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static List<TermEntity> getTerms(){
        List<TermEntity> terms = new ArrayList<>();
        terms.add(new TermEntity(TERM_NAME_1, getDate(5,1,2020), getDate(7, 1, 2020)));
        terms.add(new TermEntity(TERM_NAME_2, getDate(7,2,2020), getDate(12, 31, 2020)));
        terms.add(new TermEntity(TERM_NAME_3, getDate(1,1,2021), getDate(4, 29, 2021)));
        return terms;
    }

}
