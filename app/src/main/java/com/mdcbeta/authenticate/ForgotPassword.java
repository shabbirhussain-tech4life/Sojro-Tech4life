package com.mdcbeta.authenticate;


import android.os.Handler;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.ForgotPass;

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


public class ForgotPassword extends BaseActivity implements Validator.ValidationListener {
  @NotEmpty
  @Email
  @BindView(R.id.code_verify_email)
  EditText email;

  @BindView(R.id.send)
  Button send;

  @Inject
  ApiFactory apiFactory;
  private Validator validator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    ButterKnife.bind(this);
    validator = new Validator(this);
    validator.setValidationListener(this);


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

    ForgotPass forgotPass = new ForgotPass("",email.getText().toString());


    apiFactory.forgotPass(forgotPass)

      .compose(RxUtil.applySchedulers())
      .compose(bindUntilEvent(ActivityEvent.DESTROY))
      .subscribe(userResponse -> {
        //   Toast.makeText(getApplicationContext(),"response"+userResponse.status,Toast.LENGTH_SHORT).show();
        if (userResponse.status) {

//                        AppPref.getInstance(TermsActivity.this)
//                                .putUser(AppPref.Key.USER_LOGIN, userResponse.data.get(0));


          Toast.makeText(getApplicationContext(),"Email sent successfully",Toast.LENGTH_SHORT).show();

        }

        else {
          showError(userResponse.message);
        }

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            finish();

          }
        },2000);

      });


//



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

