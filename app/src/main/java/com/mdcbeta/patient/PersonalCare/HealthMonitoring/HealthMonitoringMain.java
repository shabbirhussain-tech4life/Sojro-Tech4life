package com.mdcbeta.patient.PersonalCare.HealthMonitoring;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.widget.ActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 04/04/2018.
 */

public class HealthMonitoringMain extends BaseFragment {

    @BindView(R.id.btnBloodpreesure)
    Button btnBloodpreesure;
    @BindView(R.id.btnGlucose)
    Button btnGlucose;
    @BindView(R.id.btnCholesterol)
    Button btnCholesterol;


    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @Inject
    MDCRepository repository;

    public HealthMonitoringMain() {
        // Required empty public constructor
    }

    public static HealthMonitoringMain newInstance() {
        HealthMonitoringMain fragment = new HealthMonitoringMain();
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
        return R.layout.fragment_health_monitoring;
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

    @OnClick({R.id.btnBloodpreesure, R.id.btnGlucose, R.id.btnCholesterol})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnBloodpreesure:
                getFragmentHandlingActivity().replaceFragment(BloodPressureFragment.newInstance());
                break;
            case R.id.btnGlucose:
                getFragmentHandlingActivity().replaceFragment(GlucoseFragment.newInstance());
                break;
            case R.id.btnCholesterol:
                getFragmentHandlingActivity().replaceFragment(CholesterolFragment.newInstance());
                break;

        }
    }



}
