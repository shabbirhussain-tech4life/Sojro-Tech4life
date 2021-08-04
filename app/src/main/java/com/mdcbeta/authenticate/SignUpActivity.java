package com.mdcbeta.authenticate;


import android.content.Intent;

import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;

import com.mdcbeta.di.AppComponent;


import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.List;
import java.util.Timer;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;

import static com.mdcbeta.util.Utils.deleteDir;

public class SignUpActivity extends BaseActivity implements Validator.ValidationListener {

    @Checked(message = "You must agree to the terms.")
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @NotEmpty
    @Password(min = 8, scheme = Password.Scheme.ANY)
    @BindView(R.id.password)
    EditText password;


    //by kk
    @BindView(R.id.sdone)
    Button done;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.demo)
    Button demo;

  @NotEmpty
    @BindView(R.id.codeOf)
    EditText code;

    @NotEmpty
    @BindView(R.id.fullname)
    EditText fullname;

    @BindView(R.id.signup)
    Button signup;

    @NotEmpty
    @Email
    @BindView(R.id.email)
    EditText email;


//    @BindView(R.id.code)
//    EditText Licensecode;

    @ConfirmPassword
    @BindView(R.id.repassword)
    EditText repassword;

    @Length(min = 6, message = "Enter atleast 8 characters.")
    @NotEmpty
    @BindView(R.id.username)
    EditText username;





    @BindView(R.id.terms_conditions)
    TextView terms;

    private Validator validator;

    @Inject
    ApiFactory apiFactory;

    boolean isPatient =false ;
    boolean support =false ;
    boolean health =false ;

public int key;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
      SpannableString content = new SpannableString("I agree to the terms and conditions");
      content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
      //  terms.setText(Html.fromHtml(String.format(getString(R.string.terms))));
        terms.setText(content);
      //  code.setVisibility(View.GONE);
        key=0;
      //terms.setPaintFlags(terms.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
//        ly =  DataBindingUtil.setContentView(this,R.layout.activity_setting);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),TermsActivity.class);
                startActivity(intent);

            }
        });

     done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

         }
     });


        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code.setVisibility(View.VISIBLE);
                //demo.setVisibility(View.GONE);
              openDialog();


key=1;

            }
        });
    }

    private void openDialog() {
//        Email_dialog email_dialog = new Email_dialog();
//        email_dialog.show(getSupportFragmentManager(), "signup");
        Intent intent = new Intent(this,DialogActivity.class);
        startActivity(intent);
    }


    @Override
    public void performInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @OnClick(R.id.signup)
    public void onViewClicked() {

      validator.validate();


    }




//    @OnCheckedChanged({R.id.patient_radio, R.id.health_provider})
//    public void
//    onCheckChange(CompoundButton view, boolean checked) {
//        switch (view.getId()) {
//            case R.id.patient_radio:
//                if (checked) {
//                    isPatient = true;
//                    support=true;
//                }
//                break;
//            case R.id.health_provider:
//                if (checked) {
//                  code.setVisibility(View.VISIBLE);
//                    isPatient = true;
//                    health=true;
//                }
//                break;
//        }
//    }


    @Override
    public void onValidationSucceeded() {




        showProgress("Loading");


       // InviteSecurity inviteSecurity = new InviteSecurity("",code.getText().toString(),email.getText().toString());

//        apiFactory.inviteSecurity(inviteSecurity)
//
//                .compose(RxUtil.applySchedulers())
//                .compose(bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(userResponse -> {
//                    if (userResponse.status) {
//
//
//                    }
//
//                    else {
//                        showError(userResponse.message);
//                    }
//                    finish();
//
//                });


        // int ispat = isPatient ? 1 : 0 ;
       // int ispat = isPatient ? 1:0 ;

//
        User user = new User("","5",code.getText().toString(),username.getText().toString(),
                "",fullname.getText().toString(),"",email.getText().toString(),
                "","","","","","","","","",
                "1");
        user.setName(fullname.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setCode(code.getText().toString());


        apiFactory.signup(user)
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(userResponse -> {

                    if (userResponse.status) {

                        AppPref.getInstance(SignUpActivity.this)
                                .putUser(AppPref.Key.USER_LOGIN, userResponse.data.get(0));

//                        if (support==true) {
//                            AppPref.getInstance(SignUpActivity.this).put(AppPref.Key.IS_PATIENT_LOGIN, true);
//                            //main support instead of PatientMainActivity
//                          //  startActivity(new Intent(SignUpActivity.this, MainSupportServiceActivity.class));
//                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                            //added by kanwal khan
//                            showProgress("Successfully Registered");
//                            //  Toast.makeText("Congratulations").show();
//
//                        } else if(health==true){
                          //  Toast.makeText(this, "health", Toast.LENGTH_LONG).show();
                         //   AppPref.getInstance(SignUpActivity.this).put(AppPref.Key.IS_PATIENT_LOGIN, true);


                          //  startActivity(new Intent(SignUpActivity.this, LoginActivity.class));



                         showProgress("Successfully Registered");


                      startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

                        }

//                        else {
//                            showError("");
//                        }


                        //else{
//                        AlertDialog.Builder builder;
//                        builder = new AlertDialog.Builder(this);
//                        //Uncomment the below code to Set the message and title from the strings.xml file
//                        builder.setMessage("Select a role first") .setTitle("Error");
                        //  showError("Select a role first");


                        // }

                       //finish();

                    //}
                    else {
                        showError(userResponse.message);
                    }

                });




    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      trimCache(); //if trimCache is static
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void trimCache() {
    try {
      File dir = getCacheDir();
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

}
