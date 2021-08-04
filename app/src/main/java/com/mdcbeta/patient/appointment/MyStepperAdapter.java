package com.mdcbeta.patient.appointment;

import android.content.Context;
import android.os.Bundle;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Shakil Karim on 4/1/17.
 */

// this file is of no use cc by kk 1/18/2020

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public static final String  KEY_POSITION = "MyStepperAdapter.position";


    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {


        if(position == 4){

            Step5Fragment step5Fragment = new Step5Fragment();
            return step5Fragment;
        }
        else if(position == 3){
            Step4Fragment step4Fragment = new Step4Fragment();
            return step4Fragment;

        }
        else if(position == 2){
            Step3Fragment step3Fragment = new Step3Fragment();
            return step3Fragment;

        }
        else {
            final StepFragment step = new StepFragment();
           // Bundle b = new Bundle();
//            b.putInt(KEY_POSITION, position);
//            step.setArguments(b);
           return step;
        }


    }

    @Override
    public int getCount() {
        return 5;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        String title = "Describe a problem";

        switch (position) {
            case 0:
                title = "Describe a problem";
                break;
            case 1:
                title = "Upload images";
                break;
            case 2:
                title = "Book an appointment";
                break;
            case 3:
                title = "Book slot(s)";
                break;
            case 4:
                title = "Payment";
                break;

        }


        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }


}
