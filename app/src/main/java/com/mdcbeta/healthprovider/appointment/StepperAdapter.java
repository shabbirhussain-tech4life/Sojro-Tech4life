package com.mdcbeta.healthprovider.appointment;

import android.content.Context;
import android.os.Bundle;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.mdcbeta.patient.appointment.Step3Fragment;
import com.mdcbeta.patient.appointment.Step4Fragment;
import com.mdcbeta.patient.appointment.Step5Fragment;
import com.mdcbeta.patient.appointment.Step6Fragment;
import com.mdcbeta.patient.appointment.StepFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    public static final String KEY_POSITION = "MyStepperAdapter.position";


    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm,context);
    }

    @Override
    public Step createStep(int position) {


        if (position == 5) {

            AppointmentSubmitFragment appointmentSubmitFragment = new AppointmentSubmitFragment();
            return appointmentSubmitFragment;
        } else if (position == 4) {
            Step4Fragment step4Fragment = new Step4Fragment();
            return step4Fragment;

        } else if (position == 3) {
            Step3Fragment step3Fragment = new Step3Fragment();
            return step3Fragment;

        } else if (position == 2) {
//            final StepFragment step = new StepFragment();
//            Bundle b = new Bundle();
//            b.putInt(KEY_POSITION, position);
//            step.setArguments(b);
//            return step;
            ImageFragment imageFragment = new ImageFragment();
            return imageFragment;
        } else if (position == 1) {
            VitalSignFragment vitalSignFragment = new VitalSignFragment();
            return vitalSignFragment;

        } else {
            PatientDataFragment patientDataFragment = new PatientDataFragment();
            return patientDataFragment;

        }


    }

    @Override
    public int getCount() {
        return 6;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        String title = "Describe a problem";

        switch (position) {
            case 0:
                title = "Patient demography";
                break;
            case 1:
                title = "Current complaints";
                // title = "Vital Sign";
                break;
            case 2:
                title = "Upload images";
                break;
            case 3:
                title = "Select doctor";
                break;
            case 4:
                title = "Book slot(s)";
                break;
            case 5:
                title = "Book an appointment";
                break;

        }


        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }



}
