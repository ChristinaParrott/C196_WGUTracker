package com.example.wgutracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.AssessmentEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessEditViewModel extends AndroidViewModel {
    public MutableLiveData<AssessmentEntity> mLiveAssess =
            new MutableLiveData<>();
    private AppRepository mRepository;

    private Executor executor = Executors.newSingleThreadExecutor();
    public AssessEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));
    }
    public void loadData(int assessId) {
        executor.execute(() -> {
            AssessmentEntity assess = mRepository.getAssessById(assessId);
            mLiveAssess.postValue(assess);
        });
    }
    public void saveAssess(String assessName, String assessType, String assessDate, int courseID) {
        AssessmentEntity assess = mLiveAssess.getValue();

        if (assess == null){
            if (TextUtils.isEmpty(assessName.trim()) ||
                    TextUtils.isEmpty(assessType.trim()) ||
                    TextUtils.isEmpty(assessDate.trim())) {
                return;
            }
            try {
                Date date = stringToDate(assessDate.trim());
                assess = new AssessmentEntity(assessName.trim(), assessType.trim(), date, courseID);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        else {
            try {
                Date aDate = stringToDate(assessDate.trim());
                assess.setAssessName(assessName.trim());
                assess.setAssessType(assessType.trim());
                assess.setAssessDate(aDate);
                assess.setCourseID(courseID);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        mRepository.insertAssess(assess);
    }

    private Date stringToDate(String str) throws ParseException {
        Date date = (new SimpleDateFormat("MMMM dd, yyyy")).parse(str);
        return date;
    }

    public void deleteAssess() {
        mRepository.deleteAssessment(mLiveAssess.getValue());
    }
}
