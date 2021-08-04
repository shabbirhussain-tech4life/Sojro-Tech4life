package com.mdcbeta.authenticate;

import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Invited;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DialogActivity extends BaseActivity implements Validator.ValidationListener  {
    @NotEmpty
    @Email
    @BindView(R.id.code_verify_email)
    EditText email;

    @BindView(R.id.send)
    Button send;

    @Inject
    ApiFactory apiFactory;
    private Validator validator;
    private String code ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        code= "27";

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
        Invited invite = new Invited("", code,"",email.getText().toString(),"");


        apiFactory.invite(invite)

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


    private static class EmailSender extends AsyncTask<Void, Void, String> {

        OkHttpClient client = new OkHttpClient();
        String email1, name1;
        List<String> email2, name2;
        String caseCode;

        EmailSender(String selectedPatientEmail, List<String> selectedReferEmail,
                    String selectedPatientName, List<String> selectedReferName, String caseCode) {
            this.email1 = selectedPatientEmail;
            this.email2 = selectedReferEmail;
            this.name1 = selectedPatientName;
            this.name2 = selectedReferName;
            this.caseCode = caseCode;
        }

        @Override

        protected String doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url(AppModule.emailUrl + "email1=" + email1 +
                            "&email2=" + new Gson().toJson(email2) + "&name1=" + name1 +
                            "&name2=" + new Gson().toJson(name2) + "&case_code=" + caseCode +
                            "&meeting-time=" + new Date().toString())
                    .post(RequestBody.create(null, new byte[]{}))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.message();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("EMAIL RESPONSE", s);

        }
    }



}
