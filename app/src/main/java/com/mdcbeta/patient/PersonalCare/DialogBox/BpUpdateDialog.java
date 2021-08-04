package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.BloodPressureFragment;
import com.mdcbeta.patient.PersonalCare.dateshow;
import com.mdcbeta.patient.PersonalCare.timeshow;
import com.mdcbeta.util.RxUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 01/07/2018.
 */

public class BpUpdateDialog extends BaseDialogFragment {


    @BindView(R.id.bpdate)
    Button bpdate;

    @BindView(R.id.bptime)
    Button bptime;

    @BindView(R.id.bpok)
    Button bpok;

    @BindView(R.id.bpcancel)
    Button bpcancel;

    @BindView(R.id.BPsystolict)
    EditText BPsystolict;

    @BindView(R.id.BPdiastolic)
    EditText BPdiastolic;

    @BindView(R.id.BPresult)
    EditText BPresult;

    @BindView(R.id.txtbpcomments)
    EditText txtbpcomments;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;


    private String shareOption;


    public static BpUpdateDialog newInstance() {
        BpUpdateDialog frag = new BpUpdateDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.bpdailog;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addBloodPressureRecord(getUser().getId(), res,
                time, BPsystolict.getText().toString(),
                BPdiastolic.getText().toString(), BPresult.getText().toString(),
                txtbpcomments.getText().toString(), res+" "+time, res+" "+time)
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



    @OnClick({R.id.bpdate, R.id.bptime,R.id.bpok,R.id.bpcancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bpdate:
                d = new dateshow(bpdate, getActivity());
                String a = bpdate.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bptime:
                t = new timeshow(bptime, getActivity());
                String b = bptime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.bpok:

                // d = new dateshow(exdate, getActivity());
                String d = bpdate.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

                //    t = new timeshow(extime, getActivity());
                String g = bptime.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addBloodPressureRecord(getUser().getId(), res,
                        time, BPsystolict.getText().toString(),
                        BPdiastolic.getText().toString(), BPresult.getText().toString(),
                        txtbpcomments.getText().toString(),
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

                getFragmentHandlingActivity().replaceFragment(BloodPressureFragment.newInstance());


                break;

            case R.id.bpcancel:
                dismiss();
                break;
        }
    }

}
