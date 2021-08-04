package com.mdcbeta.patient;

import android.app.Application;


import androidx.lifecycle.AndroidViewModel;

import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.model.MainAppointment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Shakil Karim on 7/20/17.
 */

public class PatientViewModel extends AndroidViewModel {

    @Inject
    MDCRepository repository;

    private Flowable<List<MainAppointment>> flowable;

    public PatientViewModel(Application application) {
        super(application);
        ((MyApplication)application).getAppComponent().inject(this);
    }






    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
