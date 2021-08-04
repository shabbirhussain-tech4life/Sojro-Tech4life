package com.mdcbeta.patient.appointment;

import android.os.Bundle;


import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public class PatientAppointmentFragment extends BaseFragment {

    @BindView(R.id.stepperLayout)
    public StepperLayout stepperLayout;
    private static final String TAG = "PatientAppointmentFragm";

    public static PatientAppointmentFragment newInstance() {
        PatientAppointmentFragment fragment = new PatientAppointmentFragment();
        return fragment;
    }


    @Override
    public int getLayoutID() { return R.layout.fragment_appoinment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stepperLayout.setAdapter(
                new MyStepperAdapter(getChildFragmentManager(),getContext()));
        stepperLayout.setOffscreenPageLimit(4);

    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


}
