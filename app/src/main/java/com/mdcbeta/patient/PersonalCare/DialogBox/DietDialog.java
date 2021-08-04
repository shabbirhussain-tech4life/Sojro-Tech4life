package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by MahaRani on 05/04/2018.
 */

public class DietDialog extends BaseDialogFragment {

    @BindView(R.id.txtdiet)
    EditText txtdiet;

    @BindView(R.id.txtquantity)
    EditText txtquantity;

    @BindView(R.id.txtcalories)
    EditText txtcalories;

    @BindView(R.id.diet_date)
    Button diet_date;

    @BindView(R.id.diet_time)
    Button diet_time;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.btn_ok)
    Button btn_ok;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    static String food_type;


    private String shareOption;


    public static DietDialog newInstance(String foodtype) {
        DietDialog frag = new DietDialog();
        food_type = foodtype;
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_diet;
    }


    @Override
    public void onValidationSucceeded() {

    }


    @OnClick({R.id.diet_date, R.id.diet_time,R.id.btn_ok,R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.diet_date:
                d = new dateshow(diet_date, getActivity());
                String a = diet_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.diet_time:
                t = new timeshow(diet_time, getActivity());
                String b = diet_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.btn_ok:

                String d = diet_date.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String g = diet_time.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                //   Toast.makeText(getContext(),time, Toast.LENGTH_LONG ).show();
                showProgress("Loading");

                apiFactory.addDietRecord(getUser().getId(), res,
                        time,food_type, txtdiet.getText().toString(), txtquantity.getText().toString()
                        , txtcalories.getText().toString(),
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

            case R.id.cancel:
                dismiss();
                break;
        }
    }

}
