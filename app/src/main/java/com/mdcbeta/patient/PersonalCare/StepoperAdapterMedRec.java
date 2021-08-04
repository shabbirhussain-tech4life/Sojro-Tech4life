package com.mdcbeta.patient.PersonalCare;

import android.content.Context;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.mdcbeta.patient.PersonalCare.MedicalRecords.OutpatientMainFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class StepoperAdapterMedRec extends AbstractFragmentStepAdapter {

    public static final String  KEY_POSITION = "MyStepperAdapter.position";


    public StepoperAdapterMedRec(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        if(position == 2){

            OutpatientMainFragment step2Fragment = new OutpatientMainFragment();
            return step2Fragment;

        }
        else if(position == 1){
            FragmentLaboratory step1Fragment = new FragmentLaboratory();
            return step1Fragment;

        }
        else {
            final FragmentRadiology step = new FragmentRadiology();
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
        String title = "Radiology";

        switch (position) {
            case 0:
                title = "Radiology";
                break;
            case 1:
                title = "Laboratory";
                break;
            case 2:
                title = "Out Patient Record";
                break;

        }


        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }
}
