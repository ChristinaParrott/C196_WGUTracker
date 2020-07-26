package com.example.wgutracker.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wgutracker.utilities.SampleAssessData;
import com.example.wgutracker.utilities.SampleCourseData;
import com.example.wgutracker.utilities.SampleMentorData;
import com.example.wgutracker.utilities.SampleNoteData;
import com.example.wgutracker.utilities.SampleTermData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<TermEntity>> mterms;
    public LiveData<List<CourseEntity>> mcourses;
    public LiveData<List<AssessmentEntity>> massess;
    public LiveData<List<MentorEntity>> mmentors;
    public LiveData<List<NoteEntity>> mnotes;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance((context));
        mterms = getAllTerms();
        mcourses = getAllCourses();
        massess = getAllAssessments();
        mmentors = getAllMentors();
        mnotes = getAllNotes();
    }

    public void addSampleData() {
        executor.execute(() -> {
            mDb.termDAO().insertAll(SampleTermData.getTerms());
            mDb.mentorDAO().insertAll(SampleMentorData.getMentors());
            mDb.courseDAO().insertAll(SampleCourseData.getCourses());
            mDb.assessmentDAO().insertAll(SampleAssessData.getAssessments());
            mDb.noteDAO().insertAll(SampleNoteData.getNotes());
        });
    }

    private LiveData<List<TermEntity>> getAllTerms() {
        return mDb.termDAO().getAll();
    }

    private LiveData<List<CourseEntity>> getAllCourses() {
        return mDb.courseDAO().getAll();
    }

    private LiveData<List<AssessmentEntity>> getAllAssessments() {
        return mDb.assessmentDAO().getAll();
    }

    private LiveData<List<MentorEntity>> getAllMentors() {
        return mDb.mentorDAO().getAll();
    }

    private LiveData<List<NoteEntity>> getAllNotes() {
        return mDb.noteDAO().getAll();
    }

    public void deleteAllData() {
        executor.execute(() -> {
            mDb.noteDAO().deleteAll();
            mDb.assessmentDAO().deleteAll();
            mDb.mentorDAO().deleteAll();
            mDb.courseDAO().deleteAll();
            mDb.termDAO().deleteAll();
        });
    }

    public TermEntity getTermById(int termId) {
        return mDb.termDAO().getTermByID(termId);
    }

    public CourseEntity getCourseById(int courseId) {
        return mDb.courseDAO().getCourseByID(courseId);
    }

    public AssessmentEntity getAssessById(int assessId) {
        return mDb.assessmentDAO().getAssessmentByID(assessId);
    }

    public MentorEntity getMentorById(int mentorId) {
        return mDb.mentorDAO().getMentorByID(mentorId);
    }

    public NoteEntity getNoteById(int noteId) {
        return mDb.noteDAO().getNoteByID(noteId);
    }

    public void insertTerm(TermEntity term) {
        executor.execute(() -> {
            mDb.termDAO().insertTerm(term);
        });
    }

    public void insertCourse(CourseEntity course) {
        executor.execute(() -> {
            mDb.courseDAO().insertCourse(course);
        });
    }

    public void insertAssess(AssessmentEntity assess) {
        executor.execute(() -> {
            mDb.assessmentDAO().insertAssessment(assess);
        });
    }

    public void insertMentor(MentorEntity mentor) {
        executor.execute(() -> {
            mDb.mentorDAO().insertMentor(mentor);
        });
    }

    public void insertNote(NoteEntity note) {
        executor.execute(() -> {
            mDb.noteDAO().insertNote(note);
        });
    }

    public void deleteTerm(TermEntity term) {
        executor.execute(() -> {
            mDb.termDAO().deleteTerm(term);
        });
    }

    public void deleteCourse(CourseEntity course) {
        executor.execute(() -> {
            mDb.courseDAO().deleteCourse(course);
        });
    }

    public void deleteAssessment(AssessmentEntity assess) {
        executor.execute(() -> {
            mDb.assessmentDAO().deleteAssessment(assess);
        });
    }

    public void deleteMentor(MentorEntity mentor) {
        executor.execute(() -> {
            mDb.mentorDAO().deleteMentor(mentor);
        });
    }

    public void deleteNote(NoteEntity note) {
        executor.execute(() -> {
            mDb.noteDAO().deleteNote(note);
        });
    }

    public LiveData<List<CourseEntity>> getCoursesByTerm(int termID) {
            LiveData<List<CourseEntity>> courses = mDb.courseDAO().getCourseByTerm(termID);
            return courses;
    }

    public LiveData<List<AssessmentEntity>> getAssessByCourse(int courseID) {
        LiveData<List<AssessmentEntity>> assess = mDb.assessmentDAO().getAssessmentByCourse(courseID);
        return assess;
    }

    public LiveData<List<NoteEntity>> getNotesByCourse(int courseID) {
        LiveData<List<NoteEntity>> notes = mDb.noteDAO().getNoteByCourse(courseID);
        return notes;
    }

    public LiveData<List<AssessmentEntity>> getAssessSoon(Date today) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date weekFromToday = cal.getTime();
        return mDb.assessmentDAO().getAssessSoon(today, weekFromToday);
    }

    public LiveData<List<CourseEntity>> getCourseSoon(Date today) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MONTH, 1);
        Date monthFromToday = cal.getTime();
        return mDb.courseDAO().getCourseSoon(today, monthFromToday);
    }

    public LiveData<List<TermEntity>> getCurrentTerm(Date today) {
        return mDb.termDAO().getCurrentTerm(today);
    }
    public LiveData<Integer> getCourseCount(int termId) {
        return mDb.courseDAO().getCourseCount(termId);
    }

    public LiveData<Integer> getAssessCount(int courseId) {
        return mDb.assessmentDAO().getAssessCount(courseId);
    }
}
