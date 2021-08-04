package com.mdcbeta.healthprovider.appointment;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.StepperLayout;

import butterknife.BindView;

public class AppointmentFragment extends BaseFragment {

    @BindView(R.id.stepperLayout)
    public StepperLayout stepperLayout;
    private static final String TAG = "PatientAppointmentFragm";

    public static Fragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();


        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_appoinment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stepperLayout.setAdapter(
                new StepperAdapter(getChildFragmentManager(),getContext()));
        stepperLayout.setOffscreenPageLimit(6);

    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



}

