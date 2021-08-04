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
 * Created by MahaRani on 28/06/2018.
 */

public class LaboratoryDialog extends BaseDialogFragment {


    @BindView(R.id.lab_date)
    Button lab_date;

    @BindView(R.id.lab_time)
    Button lab_time;

    @BindView(R.id.lab_ok)
    Button lab_ok;

    @BindView(R.id.lab_cancel)
    Button lab_cancel;

    @BindView(R.id.lab_result)
    EditText lab_result;

    @BindView(R.id.lab_comment)
    EditText lab_comment;

    @BindView(R.id.test_lab)
    Spinner test_lab;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String test;


    private String shareOption;


    public static LaboratoryDialog newInstance() {
        LaboratoryDialog frag = new LaboratoryDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.laboratorydialog;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addLaboratoriesRecord(getUser().getId(), res,
                time,test, lab_result.getText().toString(),
                lab_comment.getText().toString(),
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



    @OnClick({R.id.lab_date, R.id.lab_time,R.id.lab_ok,R.id.lab_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lab_date:
                d = new dateshow(lab_date, getActivity());
                String a = lab_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.lab_time:
                t = new timeshow(lab_time, getActivity());
                String b = lab_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.lab_ok:

                // d = new dateshow(exdate, getActivity());
                String d = lab_date.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

                //    t = new timeshow(extime, getActivity());
                String g = lab_time.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addLaboratoriesRecord(getUser().getId(), res,
                        time,test, lab_result.getText().toString(),
                        lab_comment.getText().toString(),
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

            case R.id.lab_cancel:
                dismiss();
                break;
        }
    }


    @OnItemSelected(R.id.test_lab) void onRealationselected(AdapterView<?> parent, int position) {
        test = (String) parent.getItemAtPosition(position);
    }


}
