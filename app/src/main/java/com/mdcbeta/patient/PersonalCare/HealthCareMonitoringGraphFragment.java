package com.mdcbeta.patient.PersonalCare;

import android.os.Bundle;


import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;

/**
 * Created by MahaRani on 06/02/2018.
 */

public class HealthCareMonitoringGraphFragment extends BaseFragment {

    @BindView(R.id.stepperLayout)
    public StepperLayout stepperLayout;
    private static final String TAG = "PatientAppointmentFragm";

    public static HealthCareMonitoringFragment newInstance() {
        HealthCareMonitoringFragment fragment = new HealthCareMonitoringFragment();
        return fragment;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_healthmonitoring_graph;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stepperLayout.setAdapter(
                new StepperAdapterHealthMonGraph(getChildFragmentManager(),getContext()));
        stepperLayout.setOffscreenPageLimit(3);

    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


}
