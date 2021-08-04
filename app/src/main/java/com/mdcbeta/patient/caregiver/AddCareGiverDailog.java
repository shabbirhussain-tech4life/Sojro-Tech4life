package com.mdcbeta.patient.caregiver;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by Shakil Karim on 4/3/17.
 */

public class AddCareGiverDailog extends BaseDialogFragment {


    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @NotEmpty
    @BindView(R.id.name)
    EditText name;

    @Email
    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.mobile)
    EditText mobile;


    @BindView(R.id.add_caregiver)
    Button addCaregiver;

    @Inject
    ApiFactory apiFactory;

    String relation;

    private String shareOption;


    public static AddCareGiverDailog newInstance() {
        AddCareGiverDailog frag = new AddCareGiverDailog();
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_add_care_giver;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addCareGiver(getUser().getId(), name.getText().toString(),
                email.getText().toString(), mobile.getText().toString(),
                relation, shareOption)
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {

                        showSuccess("Caregiver added successfully");
                        dismiss();

                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));

        getFragmentHandlingActivity().replaceFragment(CareGiverFragment.newInstance());



    }


    @OnClick({R.id.cancel_btn, R.id.add_caregiver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.add_caregiver:
                validator.validate();
                break;
        }
    }



    @OnItemSelected(R.id.spin_realtion) void onRealationselected(AdapterView<?> parent,int position) {
        relation = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_email) void onShareOption(AdapterView<?> parent,int position) {

        if(position == 0){
            shareOption = "email";
        }
        else shareOption = "mobile";
    }






}
