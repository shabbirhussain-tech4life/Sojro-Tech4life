package com.mdcbeta.patient.PersonalCare.MedicalRecords;

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
import com.mdcbeta.data.remote.model.Outpatient;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.OutpatientAdapter;
import com.mdcbeta.patient.PersonalCare.dateshow;
import com.mdcbeta.patient.PersonalCare.timeshow;
import com.mdcbeta.patient.PersonalCare.toast;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
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
 * Created by MahaRani on 28/06/2018.
 */

public class OutpatientMainFragment extends BaseFragment implements BlockingStep {

    Button op_date, op_fellowup, op_cancel, op_ok;
    EditText op_healthfac, op_healthpro, op_complaint, op_diagnosis, op_medication, op_advice, op_comment;
    Spinner spinner_type;
    dateshow d;
    timeshow t;
    String res,res1;

    @BindView(R.id.main_list)
    RecyclerView op_list;

    int listviewSpace;

    private OutpatientAdapter adapter;
    List<Outpatient> data = new ArrayList<>();

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
        outpatientdialog();
    }

    public void outpatientdialog()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.outpatientdialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        op_date = (Button) promptsView.findViewById(R.id.op_date);
        op_fellowup = (Button) promptsView.findViewById(R.id.op_fellowup);
        op_cancel = (Button) promptsView.findViewById(R.id.op_cancel);
        op_ok = (Button) promptsView.findViewById(R.id.op_ok);
        op_healthfac = (EditText) promptsView.findViewById(R.id.op_healthfac);
        op_healthpro = (EditText) promptsView.findViewById(R.id.op_healthpro);
        op_complaint = (EditText) promptsView.findViewById(R.id.op_complaint);
        op_diagnosis = (EditText) promptsView.findViewById(R.id.op_diagnosis);
        op_medication = (EditText) promptsView.findViewById(R.id.op_medication);
        op_advice = (EditText) promptsView.findViewById(R.id.op_advice);
        op_comment = (EditText) promptsView.findViewById(R.id.op_comment);
        spinner_type = (Spinner) promptsView.findViewById(R.id.spinner_type);

        ArrayAdapter adapte = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_item, android.R.layout.simple_spinner_item);
        adapte.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner_type.setAdapter(adapte);


        alertDialogBuilder.setTitle("Outpatient");

        op_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
             //   d =  new dateshow(op_date,getActivity());
            }
        });

        op_fellowup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               // d =  new dateshow(op_fellowup,getActivity());
            }
        });

        op_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        op_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(d== null ){
                    toast.settext(getActivity(),"Date is Empty");
                    return;
                }

                String a = op_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                String m = op_date.getText().toString();


                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    res1 = to.format(from.parse(m));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String s = op_healthfac.getText().toString();
                String d = op_healthpro.getText().toString();
                String b = op_complaint.getText().toString();
                String e = op_diagnosis.getText().toString();

                if(a== null){
                    toast.settext(getActivity(),"Date is Empty");
                    return;
                }

                if(a.equals("CHOOSE DATE")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(TextUtils.isEmpty(s)){
                    toast.settext(getActivity(),"Health facility is Empty");
                    return;
                }

                if(TextUtils.isEmpty(d)){
                    toast.settext(getActivity(),"Health provider is Empty");
                    return;
                }

                if(TextUtils.isEmpty(e)){
                    toast.settext(getActivity(),"Diagnosis is Empty");
                    return;
                }

                if(TextUtils.isEmpty(b)){
                    toast.settext(getActivity(),"Complaint is Empty");
                    return;
                }

                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(s) && !TextUtils.isEmpty(d)) {


                    //   Toast.makeText(getContext(), res + ",  " +  time + ",  " + s + ",  " + d, Toast.LENGTH_LONG).show();

                    showProgress("Loading");

                    apiFactory.addOutpatientRecord(getUser().getId(), res,
                            spinner_type.getSelectedItem().toString(), op_healthfac.getText().toString(),
                            op_healthpro.getText().toString(), op_complaint.getText().toString(),
                            op_diagnosis.getText().toString(), op_medication.getText().toString(),
                            op_advice.getText().toString(),res1,op_comment.getText().toString(),
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
        adapter = new OutpatientAdapter(getContext());
        op_list.setAdapter(adapter);
        op_list.setHasFixedSize(true);
        op_list.setItemAnimator(new DefaultItemAnimator());
        op_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        op_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(OutpatientDetailFragment.newInstance(item.getid()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.outpatientDelete(item.getid())
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

                    outpatientdialog();

                    apiFactory.getOutpatientDetail(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {


                                    JsonObject data = resource.data.get("details").getAsJsonObject();


                                    try {
                                        op_date.setText(com.mdcbeta.util.Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        op_healthfac.setText(data.get("healthfacility").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        op_healthpro.setText(data.get("healthprovider").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_complaint.setText(data.get("complaint").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_diagnosis.setText(data.get("diagnosis").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_medication.setText(data.get("medication").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_advice.setText(data.get("advice").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_fellowup.setText(data.get("fellowup").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        op_comment.setText(data.get("comment").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }else {
                                    showError(resource.getMessage());
                                }

                            },throwable -> {
                                showError(throwable.getMessage());

                            });

                    op_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            showProgress("Loading");

                            apiFactory.updateOutpatientRecord(item.getid(),op_date.getText().toString(),
                                    spinner_type.getSelectedItem().toString(),op_healthfac.getText().toString(),
                                    op_healthpro.getText().toString(),op_complaint.getText().toString(),
                                    op_diagnosis.getText().toString(),op_medication.getText().toString(),
                                    op_advice.getText().toString(),op_fellowup.getText().toString(),
                                    op_comment.getText().toString())

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

        getOutpatient();


    }


    public void getOutpatient()
    {
        apiFactory.getOutpatient(getUser().getId())
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
