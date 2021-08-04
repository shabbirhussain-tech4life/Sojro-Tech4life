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
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.BMIMainFragment;
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
 * Created by MahaRani on 09/04/2018.
 */

public class BMIDialog extends BaseDialogFragment {


    @BindView(R.id.bmidate)
    Button bmidate;

    @BindView(R.id.bmitime)
    Button bmitime;

    @BindView(R.id.bmiok)
    Button bmiok;

    @BindView(R.id.bmicancel)
    Button bmicancel;

    @BindView(R.id.bmicalculate)
    Button bmicalculate;

    @BindView(R.id.bmiweight)
    EditText bmiweight;

    @BindView(R.id.txt_feet)
    EditText txt_feet;

    @BindView(R.id.txt_inches)
    EditText txt_inches;

    @BindView(R.id.resultbmi)
    EditText resultbmi;

    @BindView(R.id.comment_bmi)
    EditText comment_bmi;

    @BindView(R.id.weight_unit)
    Spinner weight_unit;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String weightunit;


    private String shareOption;
    int feet,inches,weight;


    public static BMIDialog newInstance() {
        BMIDialog frag = new BMIDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.bmidailog;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addBMIRecord(getUser().getId(), res,
                time, txt_feet.getText().toString(),
                txt_inches.getText().toString(), bmiweight.getText().toString(),
                weightunit, resultbmi.getText().toString(), comment_bmi.getText().toString(),
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



    @OnClick({R.id.bmidate, R.id.bmitime,R.id.bmiok,R.id.bmicancel,R.id.bmicalculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bmidate:
                d = new dateshow(bmidate, getActivity());
                String a = bmidate.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bmitime:
                t = new timeshow(bmitime, getActivity());
                String b = bmitime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.bmicalculate:
                int result=0;
                feet = Integer.parseInt(txt_feet.getText().toString());
                inches = Integer.parseInt(txt_inches.getText().toString());
                weight = Integer.parseInt(bmiweight.getText().toString());

                result = weight/(feet*inches);

                resultbmi.setText(String.valueOf(result));

                break;
            case R.id.bmiok:

                String d = bmidate.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String g = bmitime.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                showProgress("Loading");

                apiFactory.addBMIRecord(getUser().getId(), res,
                        time, txt_feet.getText().toString(),
                        txt_inches.getText().toString(), bmiweight.getText().toString(),
                        "abc", resultbmi.getText().toString(), comment_bmi.getText().toString(),
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

                getFragmentHandlingActivity().replaceFragment(BMIMainFragment.newInstance());

                break;

            case R.id.bmicancel:
                dismiss();
                break;
        }
    }


    @OnItemSelected(R.id.weight_unit) void onRealationselected(AdapterView<?> parent, int position) {
        weightunit = (String) parent.getItemAtPosition(position);
    }

}

