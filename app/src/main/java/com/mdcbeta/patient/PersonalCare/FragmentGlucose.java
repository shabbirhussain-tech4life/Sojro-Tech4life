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
import com.mdcbeta.data.remote.model.Glucose;
import com.mdcbeta.di.AppComponent;
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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 06/01/2018.
 */

public class FragmentGlucose extends BaseFragment implements BlockingStep {

    Button glo_Date,glo_Time,glo_ok;
    EditText glo_Result, txtglucomments;
    Spinner fasting,fasting_type_of_taste, fasting_unit;
    dateshow d;
    timeshow t;
    String date;
    String time;

    @BindView(R.id.glucosebtn)
    FloatingActionButton glucosebtn;

    @BindView(R.id.main_list)
    RecyclerView glucose_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private GlucoseAdapter adapter;




    @Inject
    ApiFactory apiFactory;

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_glucose;
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

    @OnClick(R.id.glucosebtn)
    public void onViewClicked() {
        glucosedialog();
    }


    public void glucosedialog() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.glocusedialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);


        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;
        glo_Date = (Button) promptsView.findViewById(R.id.glo_date);
        glo_Time = (Button) promptsView.findViewById(R.id.glo_time);
        glo_Result = (EditText) promptsView.findViewById(R.id.glouse_result);
        fasting = (Spinner)promptsView.findViewById(R.id.fasting_glocuse);
        fasting_type_of_taste = (Spinner)promptsView.findViewById(R.id.fasting_type_of_taste);
        fasting_unit = (Spinner) promptsView.findViewById(R.id.fasting_unit);
        txtglucomments = (EditText) promptsView.findViewById(R.id.txtglucomments);
        glo_ok = (Button) promptsView.findViewById(R.id.glo_ok);

        final ArrayAdapter adapterddd = ArrayAdapter.createFromResource(
                getActivity(), R.array.Fasting, android.R.layout.simple_spinner_item);
        adapterddd.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        fasting.setAdapter(adapterddd);

        final ArrayAdapter adapterddd2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.TypeofTaste, android.R.layout.simple_spinner_item);
        adapterddd2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        fasting_type_of_taste.setAdapter(adapterddd2);

        final ArrayAdapter adapterunit = ArrayAdapter.createFromResource(
                getActivity(), R.array.Unit, android.R.layout.simple_spinner_item);
        adapterunit.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        fasting_unit.setAdapter(adapterunit);

        alertDialogBuilder.setTitle("Glucose");

        glo_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            //    d =  new dateshow(glo_Date,getActivity());
            }
        });

        glo_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  t =  new timeshow(glo_Time,getActivity());
            }

        });


        promptsView.findViewById(R.id.glo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        promptsView.findViewById(R.id.glo_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t== null || d==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }

                String a = glo_Date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    date = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String b = glo_Time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                String s = fasting.getSelectedItem().toString();
                String m = fasting_type_of_taste.getSelectedItem().toString();
                String c = fasting_unit.getSelectedItem().toString();
                String r = glo_Result.getText().toString();

                if(a.equals("CHOOSE DATE")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(b.equals("CHOOSE TIME")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }


                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(s) && !TextUtils.isEmpty(m) && !TextUtils.isEmpty(c))
                {

                    showProgress("Loading");

                    apiFactory.addGlucoseRecord(getUser().getId(),
                            date, time, s, m, glo_Result.getText().toString(),
                            c, txtglucomments.getText().toString(), date+" "+time, date+" "+time)
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

                ex_alert.cancel();

            }
        });


        ex_alert.show();


    }


    @Override
    public void onStart() {
        super.onStart();

    //    initList();

        List<Glucose> data = new ArrayList<>();

        showProgress("Loading");
        apiFactory.getGlucose(getUser().getId())
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


    private void initList() {
        adapter = new GlucoseAdapter(getContext());
        glucose_list.setAdapter(adapter);
        glucose_list.setHasFixedSize(true);
        glucose_list.setItemAnimator(new DefaultItemAnimator());
        glucose_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        glucose_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(GlucoseDetailFragment.newInstance(item.getId()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.glucoseDelete(item.getId())
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
                    glucosedialog();

                    apiFactory.getGlucoseDetail(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {


                                    JsonObject data = resource.data.get("details").getAsJsonObject();


                                    try {
                                        glo_Date.setText(com.mdcbeta.util.Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        glo_Time.setText(data.get("time").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        glo_Result.setText(data.get("result").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        txtglucomments.setText(data.get("comment").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }else {
                                    showError(resource.getMessage());
                                }

                            },throwable -> {
                                showError(throwable.getMessage());

                            });

                    glo_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            showProgress("Loading");

                            apiFactory.updateGlucoseRecord(item.getId(),glo_Date.getText().toString(),glo_Time.getText().toString(),
                                    fasting.getSelectedItem().toString(),fasting_type_of_taste.getSelectedItem().toString(),
                                    glo_Result.getText().toString(), fasting_unit.getSelectedItem().toString(),txtglucomments.getText().toString()
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

}
