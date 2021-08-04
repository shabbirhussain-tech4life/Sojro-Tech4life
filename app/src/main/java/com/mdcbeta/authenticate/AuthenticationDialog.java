package com.mdcbeta.authenticate;

import android.widget.EditText;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.dialog.AddPatientDialog;
import com.mobsandgeeks.saripaar.annotation.Email;

import butterknife.BindView;

public class AuthenticationDialog extends BaseDialogFragment {


    @Email
    @BindView(R.id.email)
    EditText email;


    public static AuthenticationDialog newInstance() {
        AuthenticationDialog frag = new AuthenticationDialog();
        return frag;


    }




    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);

    }

    @Override
    public int getLayoutID() {
        return R.layout.layout_dialog;
    }

    @Override
    public void onValidationSucceeded() {
        showProgress("Loading");


    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
