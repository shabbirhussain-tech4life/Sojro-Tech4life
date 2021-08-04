package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.Personalcare;
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
 * Created by MahaRani on 02/04/2018.
 */

public class ExerciseDialog extends BaseDialogFragment {


    @BindView(R.id.exdate)
    Button exdate;

    @BindView(R.id.extime)
    Button extime;

    @BindView(R.id.ex_ok)
    Button ex_ok;

    @BindView(R.id.ex_cancel)
    Button ex_cancel;

    @BindView(R.id.durationsss)
    EditText durationsss;

    @BindView(R.id.txtexecomments)
    EditText txtexecomments;

    @BindView(R.id.typeofactivity)
    Spinner typeofactivity;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;


    private String shareOption;


    public static ExerciseDialog newInstance() {
        ExerciseDialog frag = new ExerciseDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.exercisedialog;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addExerciseRecord(getUser().getId(), res,
                time, durationsss.getText().toString(),
                "parent", txtexecomments.getText().toString(),
                com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {

                        showSuccess("Caregiver added successfully");
                        dismiss();

                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));


    }



    @OnClick({R.id.exdate, R.id.extime,R.id.ex_ok,R.id.ex_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exdate:
                d = new dateshow(exdate, getActivity());
                String a = exdate.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.extime:
                t = new timeshow(extime, getActivity());
                String b = extime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.ex_ok:

               // d = new dateshow(exdate, getActivity());
                String d = exdate.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
           //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

            //    t = new timeshow(extime, getActivity());
                String g = extime.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

             //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
               showProgress("Loading");

                apiFactory.addExerciseRecord(getUser().getId(), res,
                        time, durationsss.getText().toString(),
                        "parent", txtexecomments.getText().toString(),
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

                getFragmentHandlingActivity().replaceFragment(Personalcare.newInstance());


                break;

            case R.id.ex_cancel:
                dismiss();
                break;
        }
    }

}
