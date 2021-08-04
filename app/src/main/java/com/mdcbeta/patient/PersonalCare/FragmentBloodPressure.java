package com.mdcbeta.patient.PersonalCare;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.BloodPressure;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.*;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by MahaRani on 06/01/2018.
 */

public class FragmentBloodPressure extends BaseFragment implements BlockingStep {

    Button bpdate, bptime, bpok, bpcancel;
    EditText bpsystolic, bpdiastolic, bpresult, txtbpcomments;
    dateshow d;
    timeshow t;
    String res;
    String time;

    @BindView(R.id.bloodpressurebtn)
    FloatingActionButton bloodpressurebtn;

    @BindView(R.id.main_list)
    RecyclerView bp_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private BloodpressureAdapter adapter;

    List<BloodPressure> data = new ArrayList<>();

    @Inject
    ApiFactory apiFactory;

    @Inject
    OkHttpClient okHttpClient;

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_bloodpressure;
    }

    public static FragmentBloodPressure newInstance() {
        FragmentBloodPressure fragment = new FragmentBloodPressure();
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initList();
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @OnClick(R.id.bloodpressurebtn)
    public void onViewClicked() {

        bloodpressuredialog();
    }


    public void bloodpressuredialog() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.bpdailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog bp_alert = alertDialogBuilder.create();
        bp_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        bpdate = (Button) promptsView.findViewById(R.id.bpdate);
        bptime = (Button) promptsView.findViewById(R.id.bptime);
        bpsystolic = (EditText) promptsView.findViewById(R.id.BPsystolict);
        bpdiastolic = (EditText) promptsView.findViewById(R.id.BPdiastolic);
        bpresult = (EditText) promptsView.findViewById(R.id.BPresult);
        txtbpcomments = (EditText) promptsView.findViewById(R.id.txtbpcomments);
        bpok = (Button) promptsView.findViewById(R.id.bpok);
        bpcancel = (Button) promptsView.findViewById(R.id.bpcancel);

        alertDialogBuilder.setTitle("Add Blood Pressure Record");

        bpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
              //  d =  new dateshow(bpdate,getActivity());
            }
        });

        bptime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  t =  new timeshow(bptime,getActivity());
            }

        });


        bpcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bp_alert.cancel();
            }
        });

        bpok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t== null || d==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }

                String a = bpdate.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                     res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String b = bptime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];

                String s = bpsystolic.getText().toString();

                String d = bpdiastolic.getText().toString();

                if(a== null || b==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }

                if(a.equals("CHOOSE DATE")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(b.equals("CHOOSE TIME")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(TextUtils.isEmpty(s)){
                    toast.settext(getActivity(),"Systolic is Empty");
                    return;
                }

                if(TextUtils.isEmpty(d)){
                    toast.settext(getActivity(),"Diastolic is Empty");
                    return;
                }

                if (Integer.valueOf(bpsystolic.getText().toString()) < 50 || Integer.valueOf(bpsystolic.getText().toString()) > 600) {
                    bpsystolic.setError("value must be between 50-500");
                    bpsystolic.requestFocus();
                    if (Integer.valueOf(bpdiastolic.getText().toString()) < 20 || Integer.valueOf(bpdiastolic.getText().toString()) > 400) {
                        bpdiastolic.setError("value must be between 50-500");
                        bpdiastolic.requestFocus();
                        return;
                    }
                    return;
                }


                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(s) && !TextUtils.isEmpty(d)) {


                 //   Toast.makeText(getContext(), res + ",  " +  time + ",  " + s + ",  " + d, Toast.LENGTH_LONG).show();

                    showProgress("Loading");

                    apiFactory.addBloodPressureRecord(getUser().getId(), res,
                            time, bpsystolic.getText().toString(),
                            bpdiastolic.getText().toString(), bpresult.getText().toString(),
                            txtbpcomments.getText().toString(),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    hideProgress();
                                 //   callback.onYesClick();
                                 //   dismiss();

                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));


                } else {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(), " Fields is Empty", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(Color.RED);
                    t.show();

                }

                bp_alert.cancel();

            }
        });


        bp_alert.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        getBloodpressure();


    }

    private void initList() {
        adapter = new BloodpressureAdapter(getContext());
        bp_list.setAdapter(adapter);
        bp_list.setHasFixedSize(true);
        bp_list.setItemAnimator(new DefaultItemAnimator());
        bp_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        bp_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(BpDetailFragment.newInstance(item.getId()));
                 //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.bpDelete(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    initList();
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;
                case R.id.btn_edit:

                    showProgress("Loading");
                bloodpressuredialog();



               apiFactory.getBloodpressureDetail(item.getId())
                        .compose(RxUtil.applySchedulers())
                        .subscribe(resource -> {

                            hideProgress();

                            if(resource.status) {


                                JsonObject data = resource.data.get("details").getAsJsonObject();


                                try {
                                    bpdate.setText(Utils.formatDate(data.get("date").getAsString()));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    bptime.setText(data.get("time").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                                try {
                                    bpsystolic.setText(data.get("systolic").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    bpdiastolic.setText(data.get("diastolic").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    bpresult.setText(data.get("result").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }



                                try {
                                    txtbpcomments.setText(data.get("comment").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }else {
                                showError(resource.getMessage());
                            }

                        },throwable -> {
                            showError(throwable.getMessage());

                        });

                bpok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        showProgress("Loading");

                        apiFactory.updateBpRecord(item.getId(),bpdate.getText().toString(),bptime.getText().toString(),
                                bpsystolic.getText().toString(),
                                bpdiastolic.getText().toString(), txtbpcomments.getText().toString()
                        )

                                .compose(RxUtil.applySchedulers())
                                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                                .subscribe(model -> {
                                    if(model.status){
                                        showSuccess("Record Updated");
                                    }
                                    else {
                                        showError(model.getMessage());
                                    }

                                },Throwable::printStackTrace);

                    }
                });

                break;
            }

        });

    }

    public void getBloodpressure()
    {
        showProgress("Loading");
        apiFactory.getBloodPressure(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 ->  {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                } , t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });

    }


    }



