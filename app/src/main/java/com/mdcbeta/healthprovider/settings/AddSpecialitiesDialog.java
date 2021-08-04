package com.mdcbeta.healthprovider.settings;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.di.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public class AddSpecialitiesDialog extends BaseDialogFragment {

    @BindView(R.id.save)
    Button save;
    @BindView(R.id.textView33)
    TextView textView33;
    @BindView(R.id.editText5)
    EditText editText5;

    public static AddSpecialitiesDialog newInstance() {
        //Bundle args = new Bundle();
        AddSpecialitiesDialog fragment = new AddSpecialitiesDialog();
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_specialities;
    }

    @Override
    public void onValidationSucceeded() {

    }



    @OnClick(R.id.save)
    public void onViewClicked() {


        if(TextUtils.isEmpty(editText5.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter Speciality", Toast.LENGTH_SHORT).show();
            return;
        }

        dismiss();

        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Speciality saved!!")
                .setConfirmText("ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();



    }
}
