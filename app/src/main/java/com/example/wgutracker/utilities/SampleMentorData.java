package com.example.wgutracker.utilities;

import com.example.wgutracker.database.MentorEntity;

import java.util.ArrayList;
import java.util.List;

public class SampleMentorData {
    private static final String MENTOR_NAME_1 = "Bryan Chun";
    private static final String MENTOR_NAME_2 = "Erik Anderson";
    private static final String MENTOR_NAME_3 = "Test Mentor";

    private static final String PHONE_1 = "123-456-7890";
    private static final String PHONE_2 = "987-654-3210";
    private static final String PHONE_3 = "567-890-1234";

    private static final String EMAIL_1 = "bchun@wgu.edu";
    private static final String EMAIL_2 = "eanderson@wgu.edu";
    private static final String EMAIL_3 = "tmentor@wgu.edu";

    public static List<MentorEntity> getMentors(){
        List<MentorEntity> mentors = new ArrayList<>();
        mentors.add(new MentorEntity(MENTOR_NAME_1,PHONE_1,EMAIL_1));
        mentors.add(new MentorEntity(MENTOR_NAME_2,PHONE_2,EMAIL_2));
        mentors.add(new MentorEntity(MENTOR_NAME_3,PHONE_3,EMAIL_3));
        return mentors;
    }
}
