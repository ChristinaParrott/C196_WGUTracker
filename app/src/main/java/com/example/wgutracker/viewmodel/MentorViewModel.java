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

import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    public LiveData<List<TermEntity>> mterms;
    public LiveData<List<CourseEntity>> mcourses;
    public LiveData<List<AssessmentEntity>> massess;
    public LiveData<List<MentorEntity>> mmentor;
    public LiveData<List<NoteEntity>> mnotes;

    private AppRepository mRepository;

    public MentorViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mterms = mRepository.mterms;
        mcourses = mRepository.mcourses;
        massess = mRepository.massess;
        mmentor = mRepository.mmentors;
        mnotes = mRepository.mnotes;
    }
}
