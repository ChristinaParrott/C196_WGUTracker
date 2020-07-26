package com.example.wgutracker;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wgutracker.database.AppDatabase;
import com.example.wgutracker.database.AssessmentDAO;
import com.example.wgutracker.database.AssessmentEntity;
import com.example.wgutracker.database.CourseDAO;
import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.database.MentorDAO;
import com.example.wgutracker.database.NoteDAO;
import com.example.wgutracker.database.NoteEntity;
import com.example.wgutracker.database.TermDAO;
import com.example.wgutracker.database.TermEntity;
import com.example.wgutracker.utilities.SampleAssessData;
import com.example.wgutracker.utilities.SampleCourseData;
import com.example.wgutracker.utilities.SampleMentorData;
import com.example.wgutracker.utilities.SampleNoteData;
import com.example.wgutracker.utilities.SampleTermData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG = "Junit";
    private AppDatabase mDB;
    private AssessmentDAO aDAO;
    private CourseDAO cDAO;
    private MentorDAO mDAO;
    private NoteDAO nDAO;
    private TermDAO tDAO;

    @Before
    public void createDB() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDB = Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class).build();
        aDAO = mDB.assessmentDAO();
        cDAO = mDB.courseDAO();
        mDAO = mDB.mentorDAO();
        nDAO = mDB.noteDAO();
        tDAO = mDB.termDAO();
        Log.i(TAG,"created DB");
    }

    @After
    public void closeDB(){
        mDB.close();
        Log.i(TAG,"closed DB");
    }
    @Test
    public void createAndRetrieveTerms(){
        tDAO.insertAll(SampleTermData.getTerms());
        int count = tDAO.getCount();
        Log.i(TAG, "createAndRetrieveTerms: count=" + count);
        assertEquals(SampleTermData.getTerms().size(), count);
    }
    @Test
    public void printTerms() {
        tDAO.insertAll(SampleTermData.getTerms());
        for (int i = 1; i <= tDAO.getCount(); i++) {
            Log.i(TAG, "termID: " + (tDAO.getTermByID(i)).getTermName());
        }
    }
    @Test
    public void compareTerms(){
        tDAO.insertAll(SampleTermData.getTerms());
        TermEntity original = SampleTermData.getTerms().get(0);
        TermEntity fromDB = tDAO.getTermByID(1);
        Log.i(TAG, "compareStrings: termName=" + fromDB.getTermName());
        assertEquals(original.getTermName(),fromDB.getTermName());
        assertEquals(1, fromDB.getTermID());
    }
    @Test
    public void createAndRetrieveCourses(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        int count = cDAO.getCount();
        Log.i(TAG, "createAndRetrieveCourses: count=" + count);
        assertEquals(SampleCourseData.getCourses().size(), count);
    }
    @Test
    public void compareCourses(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        CourseEntity original = SampleCourseData.getCourses().get(0);
        CourseEntity fromDB = cDAO.getCourseByID(1);
        Log.i(TAG, "compareStrings: courseName=" + fromDB.getCourseName());
        assertEquals(original.getCourseName(),fromDB.getCourseName());
        assertEquals(1, fromDB.getCourseID());
    }
    @Test
    public void createAndRetrieveAssess(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        aDAO.insertAll(SampleAssessData.getAssessments());
        int count = aDAO.getCount();
        Log.i(TAG, "createAndRetrieveAssess: count=" + count);
        assertEquals(SampleAssessData.getAssessments().size(), count);
    }
    @Test
    public void compareAssess(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        aDAO.insertAll(SampleAssessData.getAssessments());
        AssessmentEntity original = SampleAssessData.getAssessments().get(0);
        AssessmentEntity fromDB = aDAO.getAssessmentByID(1);
        Log.i(TAG, "compareStrings: assessmentName=" + fromDB.getAssessName());
        assertEquals(original.getAssessName(),fromDB.getAssessName());
        assertEquals(1, fromDB.getAssessID());
    }
    @Test
    public void createAndRetrieveNotes(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        aDAO.insertAll(SampleAssessData.getAssessments());
        nDAO.insertAll(SampleNoteData.getNotes());
        int count = nDAO.getCount();
        Log.i(TAG, "createAndRetrieveNotes: count=" + count);
        assertEquals(SampleNoteData.getNotes().size(), count);
    }
    @Test
    public void compareNotes(){
        tDAO.insertAll(SampleTermData.getTerms());
        mDAO.insertAll(SampleMentorData.getMentors());
        cDAO.insertAll(SampleCourseData.getCourses());
        aDAO.insertAll(SampleAssessData.getAssessments());
        nDAO.insertAll(SampleNoteData.getNotes());
        NoteEntity original = SampleNoteData.getNotes().get(0);
        NoteEntity fromDB = nDAO.getNoteByID(1);
        Log.i(TAG, "compareStrings: noteName=" + fromDB.getNoteName());
        assertEquals(original.getNoteName(),fromDB.getNoteName());
        assertEquals(1, fromDB.getNoteID());
    }
}
