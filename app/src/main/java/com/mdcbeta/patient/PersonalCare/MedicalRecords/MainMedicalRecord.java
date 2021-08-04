package com.mdcbeta.patient.PersonalCare.MedicalRecords;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.FragmentBloodPressureGraph;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.widget.ActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 28/06/2018.
 */

public class MainMedicalRecord extends BaseFragment {

    @BindView(R.id.btnradiology)
    Button btnradiology;
    @BindView(R.id.btnlaboratory)
    Button btnlaboratory;
    @BindView(R.id.btnoutpatient)
    Button btnoutpatient;


    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @Inject
    MDCRepository repository;

    public MainMedicalRecord() {
        // Required empty public constructor
    }

    public static MainMedicalRecord newInstance() {
        MainMedicalRecord fragment = new MainMedicalRecord();
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
        return R.layout.fragment_medical_record_main;
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

    @OnClick({R.id.btnradiology, R.id.btnlaboratory, R.id.btnoutpatient})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnradiology:
                getFragmentHandlingActivity().replaceFragment(FragmentBloodPressureGraph.newInstance());
                break;
            case R.id.btnlaboratory:
                getFragmentHandlingActivity().replaceFragment(LaboratoryMainFragment.newInstance());
                break;
            case R.id.btnoutpatient:
               // getFragmentHandlingActivity().replaceFragment(OutpatientMainFragment.newInstance());
                break;


        }
    }



}

