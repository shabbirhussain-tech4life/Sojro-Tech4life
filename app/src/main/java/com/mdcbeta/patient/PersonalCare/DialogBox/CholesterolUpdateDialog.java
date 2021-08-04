package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.CholesterolFragment;
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
 * Created by MahaRani on 01/07/2018.
 */

public class CholesterolUpdateDialog extends BaseDialogFragment {


    @BindView(R.id.cho_date)
    Button cho_date;

    @BindView(R.id.cho_time)
    Button cho_time;

    @BindView(R.id.cho_ok)
    Button cho_ok;

    @BindView(R.id.cho_cancel)
    Button cho_cancel;

    @BindView(R.id.choles_result)
    EditText choles_result;

    @BindView(R.id.choles_comments)
    EditText choles_comments;

    @BindView(R.id.time_cholesterol)
    Spinner time_cholesterol;

    @BindView(R.id.unit_cholesterol)
    Spinner unit_cholesterol;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String unit,test;
    static String mId;


    private String shareOption;


    public static CholesterolUpdateDialog newInstance(String id) {
        CholesterolUpdateDialog frag = new CholesterolUpdateDialog();
        mId = id;
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.cholesteroldialog;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

      showProgress("Loading...");


        apiFactory.getCholesterolDetail(mId)
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







                    }else {
                        showError(resource.getMessage());
                    }

                },throwable -> {
                    showError(throwable.getMessage());

                });

    }




    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addCholesterolRecord(getUser().getId(),
                res, time, time_cholesterol.getSelectedItem().toString(),
                choles_result.getText().toString(),
                unit_cholesterol.getSelectedItem().toString(), choles_comments.getText().toString(),
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



    @OnClick({R.id.cho_date, R.id.cho_time,R.id.cho_ok,R.id.cho_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cho_date:
                d = new dateshow(cho_date, getActivity());
                String a = cho_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cho_time:
                t = new timeshow(cho_time, getActivity());
                String b = cho_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.cho_ok:

                // d = new dateshow(exdate, getActivity());
                String d = cho_date.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

                //    t = new timeshow(extime, getActivity());
                String g = cho_time.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];



                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addCholesterolRecord(getUser().getId(),
                        res, time, unit, choles_result.getText().toString(),
                        test, choles_comments.getText().toString(),
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

                getFragmentHandlingActivity().replaceFragment(CholesterolFragment.newInstance());


                break;

            case R.id.cho_cancel:
                dismiss();
                break;
        }
    }

    @OnItemSelected(R.id.time_cholesterol) void onRealationselected(AdapterView<?> parent, int position) {
        test = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.unit_cholesterol) void onTest(AdapterView<?> parent, int position) {
        unit = (String) parent.getItemAtPosition(position);
    }



}

