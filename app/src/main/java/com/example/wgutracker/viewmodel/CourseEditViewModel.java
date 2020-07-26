package com.example.wgutracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.CourseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseEditViewModel extends AndroidViewModel {
    public MutableLiveData<CourseEntity> mLiveCourse =
            new MutableLiveData<>();
    private AppRepository mRepository;

    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));

    }

    public void loadData(int courseId) {
        executor.execute(() -> {
            CourseEntity course = mRepository.getCourseById(courseId);
            mLiveCourse.postValue(course);
        });
    }

    public void saveCourse(String courseName, String courseStart, String courseEnd, String courseStatus, int termId, int mentorId) {
        CourseEntity course = mLiveCourse.getValue();

        if (course == null) {
            if (TextUtils.isEmpty(courseName.trim()) ||
                    TextUtils.isEmpty(courseStart.trim()) ||
                    TextUtils.isEmpty(courseEnd.trim()) ||
                    TextUtils.isEmpty(courseStatus.trim()) ||
                    (termId == 0) || (mentorId == 0)) {
                return;
            }
            try {
                Date start = stringToDate(courseStart.trim());
                Date end = stringToDate(courseEnd.trim());
                course = new CourseEntity(courseName.trim(), start, end, courseStatus.trim(),
                        termId, mentorId);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

        } else {
            try {
                Date start = new SimpleDateFormat("MMMM dd, yyyy").parse(courseStart);
                Date end = new SimpleDateFormat("MMMM dd, yyyy").parse(courseEnd);
                course.setCourseName(courseName);
                course.setCourseStart(start);
                course.setCourseEnd(end);
                course.setCourseStatus(courseStatus);
                course.setTermID(termId);
                course.setMentorID(mentorId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mRepository.insertCourse(course);
    }

    public void deleteCourse() {
        mRepository.deleteCourse(mLiveCourse.getValue());
    }

    private Date stringToDate(String str) throws ParseException {
        Date date = (new SimpleDateFormat("MMMM dd, yyyy")).parse(str);
        return date;
    }

    public LiveData<Integer> getAssessCountForCourse(int courseId) {
        return mRepository.getAssessCount(courseId);
    }
}
