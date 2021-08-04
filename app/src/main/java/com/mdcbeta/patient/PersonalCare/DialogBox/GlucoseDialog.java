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
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.GlucoseFragment;
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

public class GlucoseDialog extends BaseDialogFragment {


    @BindView(R.id.glo_date)
    Button glo_date;

    @BindView(R.id.glo_time)
    Button glo_time;

    @BindView(R.id.glo_ok)
    Button glo_ok;

    @BindView(R.id.glo_cancel)
    Button glo_cancel;

    @BindView(R.id.glouse_result)
    EditText glouse_result;

    @BindView(R.id.txtglucomments)
    EditText txtglucomments;

    @BindView(R.id.fasting_unit)
    Spinner fasting_unit;

    @BindView(R.id.fasting_type_of_taste)
    Spinner fasting_type_of_taste;

    @BindView(R.id.fasting_glocuse)
    Spinner fasting_glocuse;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String unit,test,device;


    private String shareOption;


    public static GlucoseDialog newInstance() {
        GlucoseDialog frag = new GlucoseDialog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.glocusedialog;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addGlucoseRecord(getUser().getId(),
                res, time, fasting_unit.getSelectedItem().toString(),
                fasting_type_of_taste.getSelectedItem().toString(), glouse_result.getText().toString(),
                fasting_glocuse.getSelectedItem().toString(), txtglucomments.getText().toString(),
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



    @OnClick({R.id.glo_date, R.id.glo_time,R.id.glo_ok,R.id.glo_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.glo_date:
                d = new dateshow(glo_date, getActivity());
                String a = glo_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.glo_time:
                t = new timeshow(glo_time, getActivity());
                String b = glo_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.glo_ok:

                // d = new dateshow(exdate, getActivity());
                String d = glo_date.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //     Toast.makeText(getContext(),res, Toast.LENGTH_LONG ).show();

                //    t = new timeshow(extime, getActivity());
                String g = glo_time.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];



                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addGlucoseRecord(getUser().getId(),
                        res, time, device, test, glouse_result.getText().toString(),
                        unit, txtglucomments.getText().toString(),
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

                getFragmentHandlingActivity().replaceFragment(GlucoseFragment.newInstance());


                break;

            case R.id.glo_cancel:
                dismiss();
                break;
        }
    }

    @OnItemSelected(R.id.fasting_unit) void onRealationselected(AdapterView<?> parent, int position) {
        unit = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.fasting_type_of_taste) void onTest(AdapterView<?> parent, int position) {
        test = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.fasting_glocuse) void onDevice(AdapterView<?> parent, int position) {
        device = (String) parent.getItemAtPosition(position);
    }

}
