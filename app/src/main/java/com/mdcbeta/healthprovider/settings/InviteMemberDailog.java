package com.mdcbeta.healthprovider.settings;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Invited;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * Created by Shakil Karim on 4/8/17.
 */

public class InviteMemberDailog extends BaseDialogFragment implements Validator.ValidationListener {

    @BindView(R.id.invite)
    Button invite;
    @BindView(R.id.textView33)
    TextView textView33;
    @BindView(R.id.editText5)
    EditText email;
    @Inject
    ApiFactory apiFactory;
    private Validator validator;




    public static InviteMemberDailog newInstance() {

        // Bundle args = new Bundle();

        InviteMemberDailog fragment = new InviteMemberDailog();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void provideInjection(AppComponent appComponent) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.dailog_invite_member;
    }



    @OnClick(R.id.invite)
    public void onViewClicked() {

        if(TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        dismiss();

        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Member invited!!")
                .setConfirmText("ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();
    //    validator.validate();


    }

       // String str = getUser().licenses_id;

        @Override
        public void onValidationSucceeded () {
            showProgress("Loading");
            //  Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
            Invited invite = new Invited("", "", "", email.getText().toString(), "");

            apiFactory.invite(invite)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(objectResponse -> {
                        if (objectResponse.status) {
                            showProgress("Loading");

                            //                Toast.makeText(getContext(),"New patient registered successfully",Toast.LENGTH_SHORT).show();
                            showProgress("EMAIL SENT Successfully !");


                            hideProgress();


//


                        } else {
                            showError(objectResponse.message);
                        }

                    });
        }







    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
