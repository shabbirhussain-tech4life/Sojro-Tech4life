package com.mdcbeta.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Invited;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.FlowLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import javax.inject.Inject;
import javax.security.auth.Destroyable;

import butterknife.BindView;

import static com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent;

public class Email_dialog extends AppCompatDialogFragment implements Validator.ValidationListener {
    @Email
    @BindView(R.id.email_license)
    EditText email;
    @Inject
    ApiFactory apiFactory;
    private Validator validator;
    int license_purchase_id = 27;
    String check = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Email for license code")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                    email.getText().toString();
//                        user.setEmail(email.getText().toString());

                       // send();

                        //    Toast.makeText(getActivity(),"Email Verification"+check,Toast.LENGTH_SHORT).show();


                    }
                });
//        email_license= view.findViewById(R.id.email_license);
        return builder.create();


    }

    public void send()
    {
        Invited invite = new Invited("","","","","");
//        invite.setEmail(email.getText().toString());
        apiFactory.invite(invite);
//                .compose(RxUtil.applySchedulers())
//                .subscribe(objectResponse -> {
//                    if (objectResponse.status) {
//
//
//Toast.makeText(getContext(),"I am email"+email,Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                       // showError(userResponse.message);
//                        Toast.makeText(getContext(),"I am error"+email,Toast.LENGTH_SHORT).show();
//                    }
//                });

       getFragmentManager().popBackStack();
    }


    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }
}
