package com.mdcbeta.patient.PersonalCare;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.mdcbeta.data.remote.model.Laboratory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.LaboratoryDetailFragment;
import com.mdcbeta.util.*;
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

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class FragmentLaboratory extends BaseFragment implements BlockingStep {

    Button lab_Date, lab_Time,lab_ok;
    EditText lab_result, lab_comment;
    Spinner test_lab;
    dateshow d;
    timeshow t;
    String res,time;

    @BindView(R.id.main_list)
    RecyclerView exercise_list;

    int listviewSpace;

    private LaboratoryAdapter adapter;
    List<Laboratory> data = new ArrayList<>();

    @BindView(R.id.laboratorybtn)
    FloatingActionButton laboratorybtn;

    @Inject
    ApiFactory apiFactory;

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_laboratory;
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

    @OnClick(R.id.laboratorybtn)
    public void onViewClicked() {
        laboratorydialog();
    }

    public void laboratorydialog()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.laboratorydialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;
        lab_Date = (Button) promptsView.findViewById(R.id.lab_date);
        lab_Time = (Button) promptsView.findViewById(R.id.lab_time);
        test_lab = (Spinner)promptsView.findViewById(R.id.test_lab);
        lab_result = (EditText) promptsView.findViewById(R.id.lab_result);
        lab_comment = (EditText) promptsView.findViewById(R.id.lab_comment);
        lab_ok = (Button) promptsView.findViewById(R.id.lab_ok);

        final ArrayAdapter adapterddd = ArrayAdapter.createFromResource(
                getActivity(), R.array.Test, android.R.layout.simple_spinner_item);
        adapterddd.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        test_lab.setAdapter(adapterddd);


        alertDialogBuilder.setTitle("Laboratory");

        lab_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
             //   d =  new dateshow(lab_Date,getActivity());
            }
        });

        lab_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // t =  new timeshow(lab_Time,getActivity());
            }

        });

        promptsView.findViewById(R.id.lab_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });


        promptsView.findViewById(R.id.lab_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = lab_Date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String b = lab_Time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];

                String s = test_lab.getSelectedItem().toString();

                String r = lab_comment.getText().toString();


                if(t== null || d==null){
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


                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(s))
                {

                    showProgress("Loading");

                    apiFactory.addLaboratoriesRecord(getUser().getId(), res,
                            time, test_lab.getSelectedItem().toString(),
                            lab_result.getText().toString(),
                            lab_comment.getText().toString(),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    showSuccess("Record added successfully");
                                    hideProgress();


                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));



                } else {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(), " Fields is Empty", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(Color.RED);
                    t.show();

                }

                ex_alert.cancel();

            }
        });


        ex_alert.show();

    }

    private void initList() {
        adapter = new LaboratoryAdapter(getContext());
        exercise_list.setAdapter(adapter);
        exercise_list.setHasFixedSize(true);
        exercise_list.setItemAnimator(new DefaultItemAnimator());
        exercise_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        exercise_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(LaboratoryDetailFragment.newInstance(item.getid()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.laboratoriesDelete(item.getid())
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

                    laboratorydialog();

                    apiFactory.getLaboratoriesDetail(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {


                                    JsonObject data = resource.data.get("details").getAsJsonObject();


                                    try {
                                        lab_Date.setText(com.mdcbeta.util.Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        lab_Time.setText(data.get("time").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        lab_result.setText(data.get("result").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        lab_comment.setText(data.get("comment").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }else {
                                    showError(resource.getMessage());
                                }

                            },throwable -> {
                                showError(throwable.getMessage());

                            });


                    lab_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            showProgress("Loading");

                            apiFactory.updateLaboratoryRecord(item.getid(),lab_Date.getText().toString(),
                                    lab_Time.getText().toString(),test_lab.getSelectedItem().toString(),
                                    lab_result.getText().toString(),
                                    lab_comment.getText().toString()
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


    @Override
    public void onStart() {
        super.onStart();

        getLaboratory();


    }


    public void getLaboratory()
    {
        apiFactory.getLaboratories(getUser().getId())
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
