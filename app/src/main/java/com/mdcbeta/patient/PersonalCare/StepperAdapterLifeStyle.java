package com.mdcbeta.patient.PersonalCare;

import android.content.Context;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.mdcbeta.patient.PersonalCare.LifeStyleMain.DietMainFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class StepperAdapterLifeStyle extends AbstractFragmentStepAdapter {

    public static final String  KEY_POSITION = "MyStepperAdapter.position";


    public StepperAdapterLifeStyle(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

         if(position == 1){
            DietMainFragment step1Fragment = new DietMainFragment();
            return step1Fragment;

        }
        else {
            final FragmentExercise step = new FragmentExercise();
            return step;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        String title = "Exercise";

        switch (position) {
            case 0:
                title = "Exercise";
                break;
            case 1:
                title = "Diet";
                break;

        }


        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }
}
