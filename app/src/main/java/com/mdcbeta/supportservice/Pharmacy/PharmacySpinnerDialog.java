package com.mdcbeta.supportservice.Pharmacy;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.supportservice.radiology.MainRadiologyFragment;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by MahaRani on 12/11/2018.
 */

public class PharmacySpinnerDialog extends BaseDialogFragment {


    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @BindView(R.id.add_caregiver)
    Button addCaregiver;

    @NotEmpty
    @BindView(R.id.name)
    EditText name;

    @Inject
    ApiFactory apiFactory;

    String relation;
    static String mId;

    private String shareOption;


    public static PharmacySpinnerDialog newInstance(String id) {
        PharmacySpinnerDialog frag = new PharmacySpinnerDialog();
        mId = id;
        return frag;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_add_radio_spinner;
    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.updatePresStatus(mId,
                relation)
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {

                        showSuccess("Caregiver added successfully");
                        dismiss();

                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));

        getFragmentHandlingActivity().replaceFragment(MainRadiologyFragment.newInstance());

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

    @OnItemSelected(R.id.spin_realtion) void onRealationselected(AdapterView<?> parent, int position) {
        relation = (String) parent.getItemAtPosition(position);
    }



}



