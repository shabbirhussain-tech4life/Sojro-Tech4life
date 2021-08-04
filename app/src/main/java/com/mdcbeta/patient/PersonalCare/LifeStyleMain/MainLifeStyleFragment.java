package com.mdcbeta.patient.PersonalCare.LifeStyleMain;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.Personalcare;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.widget.ActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 05/04/2018.
 */

public class MainLifeStyleFragment extends BaseFragment {

    @BindView(R.id.btnexercise)
    Button btnexercise;
    @BindView(R.id.btndiet)
    Button btndiet;
    @BindView(R.id.btnbmi)
    Button btnbmi;


    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @Inject
    MDCRepository repository;

    public MainLifeStyleFragment() {
        // Required empty public constructor
    }

    public static MainLifeStyleFragment newInstance() {
        MainLifeStyleFragment fragment = new MainLifeStyleFragment();
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
        return R.layout.fragment_lifestyle_main;
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

    @OnClick({R.id.btnexercise, R.id.btndiet, R.id.btnbmi})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnexercise:
                getFragmentHandlingActivity().replaceFragment(Personalcare.newInstance());
                break;
            case R.id.btndiet:
                getFragmentHandlingActivity().replaceFragment(DietMainFragment.newInstance());
                break;
            case R.id.btnbmi:
                getFragmentHandlingActivity().replaceFragment(BMIMainFragment.newInstance());
                break;


        }
    }



}
