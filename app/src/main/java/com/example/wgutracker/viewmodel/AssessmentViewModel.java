package com.example.wgutracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.AssessmentEntity;
import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.database.MentorEntity;
import com.example.wgutracker.database.NoteEntity;
import com.example.wgutracker.database.TermEntity;

import java.util.Date;
import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    public LiveData<List<TermEntity>> mterms;
    public LiveData<List<CourseEntity>> mcourses;
    public LiveData<List<AssessmentEntity>> massess;
    public LiveData<List<MentorEntity>> mmentor;
    public LiveData<List<NoteEntity>> mnotes;

    private AppRepository mRepository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mterms = mRepository.mterms;
        mcourses = mRepository.mcourses;
        massess = mRepository.massess;
        mmentor = mRepository.mmentors;
        mnotes = mRepository.mnotes;
    }

    public LiveData<List<AssessmentEntity>> getAssessmentsByCourse(int courseID) {
        return mRepository.getAssessByCourse(courseID);
    }

    public LiveData<List<AssessmentEntity>> getAssessSoon(Date today) {
        return mRepository.getAssessSoon(today);
    }
}
