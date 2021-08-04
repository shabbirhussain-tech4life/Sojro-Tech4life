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
import com.mdcbeta.data.remote.model.Cholesterol;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.CholesterolDetailFragment;
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

public class FragmentCholesterol extends BaseFragment implements BlockingStep {

    Button cho_date,cho_time,cho_ok;
    EditText choles_result, choles_comments;
    Spinner time_cholesterol, unit_cholesterol;
    dateshow d;
    timeshow t;
    String date, time;

    @BindView(R.id.cholesterolbtn)
    FloatingActionButton cholesterolbtn;

    @BindView(R.id.main_list)
    RecyclerView cholesterol_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private CholesterolAdapter adapter;

    @Inject
    ApiFactory apiFactory;

    List<Cholesterol> data = new ArrayList<>();

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_cholesterol;
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

    @OnClick(R.id.cholesterolbtn)
    public void onViewClicked() {
        cholesteroldialog();
    }

    public void cholesteroldialog()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.cholesteroldialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;
        cho_date = (Button) promptsView.findViewById(R.id.cho_date);
        cho_time = (Button) promptsView.findViewById(R.id.cho_time);
        choles_result = (EditText) promptsView.findViewById(R.id.choles_result);
        choles_comments = (EditText) promptsView.findViewById(R.id.choles_comments);
        time_cholesterol = (Spinner)promptsView.findViewById(R.id.time_cholesterol);
        unit_cholesterol = (Spinner) promptsView.findViewById(R.id.unit_cholesterol);
        cho_ok = (Button) promptsView.findViewById(R.id.cho_ok);

        final ArrayAdapter adapterddd = ArrayAdapter.createFromResource(
                getActivity(), R.array.TypeofTaste, android.R.layout.simple_spinner_item);
        adapterddd.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        time_cholesterol.setAdapter(adapterddd);

        final ArrayAdapter adapterunit = ArrayAdapter.createFromResource(
                getActivity(), R.array.Unit, android.R.layout.simple_spinner_item);
        adapterunit.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        unit_cholesterol.setAdapter(adapterunit);

        alertDialogBuilder.setTitle("Cholesterol");

        cho_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               // d =  new dateshow(cho_date,getActivity());
            }
        });

        cho_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             //   t =  new timeshow(cho_time,getActivity());
            }

        });

        promptsView.findViewById(R.id.cho_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        promptsView.findViewById(R.id.cho_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = cho_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    date = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String b = cho_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];

                String s = time_cholesterol.getSelectedItem().toString();

                String r = unit_cholesterol.getSelectedItem().toString();

                if(t== null || d==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }


                     if (!TextUtils.isEmpty(cho_date.getText().toString()) && !TextUtils.isEmpty(cho_time.getText().toString()) && !TextUtils.isEmpty(time_cholesterol.getSelectedItem().toString()) && !TextUtils.isEmpty(unit_cholesterol.getSelectedItem().toString()))
            {


                showProgress("Loading");

                apiFactory.addCholesterolRecord(getUser().getId(),
                        date, time, s, choles_result.getText().toString(),
                        r, choles_comments.getText().toString(),
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

                ex_alert.cancel();

        }
    });


        ex_alert.show();

}


    private void initList() {
        adapter = new CholesterolAdapter(getContext());
        cholesterol_list.setAdapter(adapter);
        cholesterol_list.setHasFixedSize(true);
        cholesterol_list.setItemAnimator(new DefaultItemAnimator());
        cholesterol_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        cholesterol_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(CholesterolDetailFragment.newInstance(item.getId()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.cholesterolDelete(item.getId())
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
                    cholesteroldialog();

                   apiFactory.getCholesterolDetail(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {


                                    JsonObject data = resource.data.get("details").getAsJsonObject();


                                    try {
                                        cho_date.setText(com.mdcbeta.util.Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        cho_time.setText(data.get("time").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        choles_result.setText(data.get("result").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }




                                    try {
                                        choles_comments.setText(data.get("comment").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }else {
                                    showError(resource.getMessage());
                                }

                            },throwable -> {
                                showError(throwable.getMessage());

                            });

                     cho_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                            SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);



                            showProgress("Loading");

                            apiFactory.updateCholesterolRecord(item.getId(),cho_date.getText().toString(),
                                    cho_time.getText().toString(),time_cholesterol.getSelectedItem().toString(),
                                    choles_result.getText().toString(),
                                    unit_cholesterol.getSelectedItem().toString(), choles_comments.getText().toString()
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

        //    initList();


        showProgress("Loading");
        apiFactory.getCholesterol(getUser().getId())
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
