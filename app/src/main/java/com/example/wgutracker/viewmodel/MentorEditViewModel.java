package com.example.wgutracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.MentorEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorEditViewModel extends AndroidViewModel {
    public MutableLiveData<MentorEntity> mLiveMentor =
            new MutableLiveData<>();
    private AppRepository mRepository;

    private Executor executor = Executors.newSingleThreadExecutor();
    public MentorEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));

    }
    public void loadData(int mentorId) {
        executor.execute(() -> {
            MentorEntity mentor = mRepository.getMentorById(mentorId);
            mLiveMentor.postValue(mentor);
        });
    }
    public void saveMentor(String name, String phone, String email) {
        MentorEntity mentor = mLiveMentor.getValue();

        if (mentor == null) {
            if (TextUtils.isEmpty(name.trim()) ||
                    TextUtils.isEmpty(phone.trim()) ||
                    TextUtils.isEmpty(email.trim())) {
                return;
            }
           mentor = new MentorEntity(name.trim(), phone.trim(), email.trim());
        }
        else {

                mentor.setMentorName(name.trim());
                mentor.setMentorPhone(phone.trim());
                mentor.setMentorEmail(email.trim());
        }
        mRepository.insertMentor(mentor);
    }
    public void deleteMentor() {
        mRepository.deleteMentor(mLiveMentor.getValue());
    }
}
