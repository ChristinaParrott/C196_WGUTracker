package com.example.wgutracker.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wgutracker.database.AppRepository;
import com.example.wgutracker.database.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermEditViewModel extends AndroidViewModel {
    public MutableLiveData<TermEntity> mLiveTerm =
            new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance((getApplication()));

    }

    public void loadData(int termId) {
        executor.execute(() -> {
            TermEntity term = mRepository.getTermById(termId);
            mLiveTerm.postValue(term);
        });
    }

    public void saveTerm(String termName, String termStart, String termEnd) {
        TermEntity term = mLiveTerm.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(termName.trim()) ||
                    TextUtils.isEmpty(termStart.trim()) ||
                    TextUtils.isEmpty(termEnd.trim())) {
                return;
            }
                try {
                    Date start = stringToDate(termStart.trim());
                    Date end = stringToDate(termEnd.trim());
                    term = new TermEntity(termName.trim(), start, end);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
        }
        else {
            try {
                Date start = stringToDate(termStart.trim());
                Date end = stringToDate(termEnd.trim());
                term.setTermName(termName.trim());
                term.setTermStart(start);
                term.setTermEnd(end);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        mRepository.insertTerm(term);
    }
    private Date stringToDate(String str) throws ParseException {
        Date date = (new SimpleDateFormat("MMMM dd, yyyy")).parse(str);
        return date;
    }

    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerm.getValue());
    }

    public LiveData<Integer> getCourseCountForTerm(int termId) {
        return mRepository.getCourseCount(termId);
    }
}


