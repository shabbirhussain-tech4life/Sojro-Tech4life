package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.LaboratoryMainFragment;
import com.mdcbeta.patient.PersonalCare.dateshow;
import com.mdcbeta.patient.PersonalCare.timeshow;
import com.mdcbeta.util.RxUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by MahaRani on 29/06/2018.
 */

public class OutpatientDialog extends BaseDialogFragment {


    @BindView(R.id.op_date)
    Button op_date;

    @BindView(R.id.op_ok)
    Button op_ok;

    @BindView(R.id.op_cancel)
    Button op_cancel;

    @BindView(R.id.op_healthfac)
    EditText op_healthfac;

    @BindView(R.id.op_healthpro)
    EditText op_healthpro;

    @BindView(R.id.op_complaint)
    EditText op_complaint;

    @BindView(R.id.op_diagnosis)
    EditText op_diagnosis;

    @BindView(R.id.op_medication)
    EditText op_medication;

    @BindView(R.id.op_advice)
    EditText op_advice;

    @BindView(R.id.op_fellowup)
    Button op_fellowup;

    @BindView(R.id.op_comment)
    EditText op_comment;

    @BindView(R.id.spinner_type)
    Spinner spinner_type;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String test;


    private String shareOption;


    public static OutpatientDialog newInstance() {
        OutpatientDialog frag = new OutpatientDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.outpatientdialog;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addOutpatientRecord(getUser().getId(), res,
                test,op_healthfac.getText().toString(), op_healthpro.getText().toString(),
                op_complaint.getText().toString(), op_diagnosis.getText().toString(),
                op_medication.getText().toString(), op_advice.getText().toString(),
                op_fellowup.getText().toString(), op_comment.getText().toString(),
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

    }



    @OnClick({R.id.op_date, R.id.op_fellowup,R.id.op_ok,R.id.op_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.op_date:
                d = new dateshow(op_date, getActivity());
                String a = op_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.op_fellowup:
                t = new timeshow(op_fellowup, getActivity());
                String b = op_fellowup.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.op_ok:

                // d = new dateshow(exdate, getActivity());
                String d = op_date.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

                //    t = new timeshow(extime, getActivity());
                String g = op_fellowup.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addOutpatientRecord(getUser().getId(), res,
                        test,op_healthfac.getText().toString(), op_healthpro.getText().toString(),
                        op_complaint.getText().toString(), op_diagnosis.getText().toString(),
                        op_medication.getText().toString(), op_advice.getText().toString(),
                        op_fellowup.getText().toString(), op_comment.getText().toString(),
                        com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                        com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {
                            if(model.status) {
                                showSuccess("Record added successfully");
                                dismiss();


                            }else {
                                showError(model.getMessage());
                            }

                        },throwable -> showError(throwable.getMessage()));

                getFragmentHandlingActivity().replaceFragment(LaboratoryMainFragment.newInstance());


                break;

            case R.id.op_cancel:
                dismiss();
                break;
        }
    }


    @OnItemSelected(R.id.spinner_type) void onRealationselected(AdapterView<?> parent, int position) {
        test = (String) parent.getItemAtPosition(position);
    }


}

