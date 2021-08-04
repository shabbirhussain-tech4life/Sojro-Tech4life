package com.mdcbeta.patient.PersonalCare;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.MainMedicalRecord;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.widget.ActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 05/01/2018.
 */

public class PersonalCareFragment extends BaseFragment {

    @BindView(R.id.btnLifestyle)
    Button lifeStyle;
    @BindView(R.id.btnHealthmonitoring)
    Button healthMonitoring;
    @BindView(R.id.btnHmonitoringgraph)
    Button btnHmonitoringgraph;
    @BindView(R.id.btnMedicalRecords)
    Button btnMedicalRecords;

    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @Inject
    MDCRepository repository;

    public PersonalCareFragment() {
        // Required empty public constructor
    }

    public static PersonalCareFragment newInstance() {
        PersonalCareFragment fragment = new PersonalCareFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_personal_care;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }

    @OnClick({R.id.btnLifestyle, R.id.btnHealthmonitoring, R.id.btnHmonitoringgraph, R.id.btnMedicalRecords})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnLifestyle:
              //  getFragmentHandlingActivity().replaceFragment(MainLifeStyleFragment.newInstance());
                getFragmentHandlingActivity().replaceFragment(LifeStyleFragment.newInstance());
                break;
            case R.id.btnHealthmonitoring:
               // getFragmentHandlingActivity().replaceFragment(HealthMonitoringMain.newInstance());
                getFragmentHandlingActivity().replaceFragment(HealthCareMonitoringFragment.newInstance());
                break;
            case R.id.btnMedicalRecords:
                getFragmentHandlingActivity().replaceFragment(MedicalRecordsFragment.newInstance());
                break;
            case R.id.btnHmonitoringgraph:
                getFragmentHandlingActivity().replaceFragment(MainMedicalRecord.newInstance());
                break;


        }
    }



}
