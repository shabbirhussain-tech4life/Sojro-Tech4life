package com.mdcbeta.healthprovider.settings;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.InvitedByAdmin;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteMemberActivity extends BaseActivity implements Validator.ValidationListener {
    @NotEmpty
    @Email
    @BindView(R.id.invite_email)
    EditText email;

    @BindView(R.id.invite)
    Button send;

    @Inject
    ApiFactory apiFactory;
    private Validator validator;
    private String license_purchase_id;
    String invited_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);

        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        license_purchase_id = getUser().licenses_id;
        invited_by = getUser().id;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                validator.validate();

            }
        });

    }



    @Override
    public void performInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onValidationSucceeded() {
        showProgress("Loading");
        //  Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
        InvitedByAdmin invitedByAdmin = new InvitedByAdmin("", "", license_purchase_id, email.getText().toString(), invited_by);


        apiFactory.inviteByAdmin(invitedByAdmin)

                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(userResponse -> {
                    //   Toast.makeText(getApplicationContext(),"response"+userResponse.status,Toast.LENGTH_SHORT).show();
                    if (userResponse.status) {

//                        AppPref.getInstance(TermsActivity.this)
//                                .putUser(AppPref.Key.USER_LOGIN, userResponse.data.get(0));


                        Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        showError(userResponse.message);
                    }
                    // it will immediately hide the box

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();

                        }
                    },2000);


                  //  finish();

                });


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
