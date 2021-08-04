package com.mdcbeta.patient.PersonalCare.MedicalRecords;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.HealthProviderViewModel;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by MahaRani on 29/06/2018.
 */

public class LaboratoryDetailFragment extends BaseFragment {

    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_duration)
    TextView txtDuration;
    @BindView(R.id.txt_activity)
    TextView txtActivity;
    @BindView(R.id.txt_comment)
    TextView txtComment;
    @BindView(R.id.txt_one)
    TextView txt_one;
    @BindView(R.id.txt_two)
    TextView txt_two;
    @BindView(R.id.txt_three)
    TextView txt_three;
    @BindView(R.id.txt_four)
    TextView txt_four;
    @BindView(R.id.txt_result)
    TextView txt_result;
    @BindView(R.id.txt_unit)
    TextView txt_unit;

    @Inject
    MDCRepository mdcRepository;


    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;

    static String mId;
    private CustomSpinnerAdapter<UserNames> adapter;
    ArrayList<String> images;

    private static final String TAG = "CaseDetailFragment";
    private HealthProviderViewModel healthProviderViewModel;
    private LayoutInflater inflater;

    String url;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_record_details;
    }

    public static LaboratoryDetailFragment newInstance(String id) {
        LaboratoryDetailFragment fragment = new LaboratoryDetailFragment();
        mId = id;
        return fragment;
    }


    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        healthProviderViewModel = ViewModelProviders.of(getActivity()).get(HealthProviderViewModel.class);
        inflater = LayoutInflater.from(getContext());
        getData();

        txt_one.setText("Test");
        txt_two.setText("Result");

    }

    private void showHideUi() {

        txtDate.setText("");
        txtTime.setText("");
        txtActivity.setText("");
        txtComment.setText("");

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



    public void getData(){


        showProgress("Loading...");


        apiFactory.getLaboratoriesDetail(mId)
                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {

                    hideProgress();

                    if(resource.status) {


                        JsonObject data = resource.data.get("details").getAsJsonObject();


                        try {
                            txtDate.setText(com.mdcbeta.util.Utils.formatDate(data.get("date").getAsString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            txtTime.setText(data.get("time").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            txtActivity.setText(data.get("result").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txtDuration.setText(data.get("test").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                   /*     try {
                            txt_unit.setText(data.get("unit").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }*/


                        try {
                            txtComment.setText(data.get("comment").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }else {
                        showError(resource.getMessage());
                    }

                },throwable -> {
                    showError(throwable.getMessage());

                });

    }


}
