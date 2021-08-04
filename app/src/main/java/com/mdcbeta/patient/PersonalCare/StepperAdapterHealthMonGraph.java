package com.mdcbeta.patient.PersonalCare;

import android.content.Context;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by MahaRani on 06/02/2018.
 */

class StepperAdapterHealthMonGraph extends AbstractFragmentStepAdapter {

    public static final String  KEY_POSITION = "MyStepperAdapter.position";


    public StepperAdapterHealthMonGraph(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        if(position == 2){

            FragmentCholesterol step2Fragment = new FragmentCholesterol();
            return step2Fragment;
        }
        else if(position == 1){
            FragmentGlucose step1Fragment = new FragmentGlucose();
            return step1Fragment;

        }
        else {
            final FragmentBloodPressure step = new FragmentBloodPressure();
            return step;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        String title = "Blood Pressure Graph";

        switch (position) {
            case 0:
                title = "Blood Pressure Graph";
                break;
            case 1:
                title = "Glucose Graph";
                break;
            case 2:
                title = "Cholesterol Graph";
                break;

        }


        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }
}
