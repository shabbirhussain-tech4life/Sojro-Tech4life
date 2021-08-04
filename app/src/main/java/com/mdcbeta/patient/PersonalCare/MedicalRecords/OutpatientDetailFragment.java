package com.mdcbeta.patient.PersonalCare.MedicalRecords;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
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

public class OutpatientDetailFragment extends BaseFragment {

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
    @BindView(R.id.txt_medication)
    TextView txtmedication;
    @BindView(R.id.txt_advice)
    TextView txtadvice;
    @BindView(R.id.txt_fellowup)
    TextView txtfellowup;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.txt_one)
    TextView txt_one;
    @BindView(R.id.txt_two)
    TextView txt_two;
    @BindView(R.id.txt_three)
    TextView txt_three;
    @BindView(R.id.txt_four)
    TextView txt_four;
    @BindView(R.id.txt_five)
    TextView txt_five;
    @BindView(R.id.txt_six)
    TextView txt_six;
    @BindView(R.id.txt_seven)
    TextView txt_seven;
    @BindView(R.id.txt_eight)
    TextView txt_eight;
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

    public static OutpatientDetailFragment newInstance(String id) {
        OutpatientDetailFragment fragment = new OutpatientDetailFragment();
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

        txttime.setText("Type");
        txt_one.setText("Health Facility");
        txt_two.setText("Health Provider");
        txt_three.setVisibility(View.VISIBLE);
        txt_three.setText("Complaint");
        txt_four.setText("Diagnosis");
        txt_four.setVisibility(View.VISIBLE);
        txt_five.setVisibility(View.VISIBLE);
        txt_six.setVisibility(View.VISIBLE);
        txt_seven.setVisibility(View.VISIBLE);
        txt_eight.setVisibility(View.VISIBLE);
        txt_result.setVisibility(View.VISIBLE);
        txtfellowup.setVisibility(View.VISIBLE);
        txtmedication.setVisibility(View.VISIBLE);
        txtadvice.setVisibility(View.VISIBLE);
        txt_unit.setVisibility(View.VISIBLE);

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


        apiFactory.getOutpatientDetail(mId)
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
                            txtTime.setText(data.get("type").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            txtActivity.setText(data.get("healthfacility").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txtDuration.setText(data.get("healthprovider").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txt_result.setText(data.get("complaint").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txt_unit.setText(data.get("diagnosis").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txtmedication.setText(data.get("medication").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            txtadvice.setText(data.get("advice").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            txtfellowup.setText(data.get("fellowup").getAsString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

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
