package com.mdcbeta.base;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.model.User;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.AppPref;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public abstract class BaseDialogFragment extends AppCompatDialogFragment
        implements Validator.ValidationListener {

    Unbinder unbinder;
    protected CompositeDisposable compositeDisposable;
    private static final String TAG = "BaseDialogFragment";
    protected Validator validator;
    protected DialogClickListener callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        Log.d(TAG, "onCreate: ");
        validator = new Validator(this);
        validator.setValidationListener(this);

        try {
            callback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        provideInjection(((MyApplication)getActivity().getApplicationContext()).getAppComponent());

    }


    public abstract void provideInjection(AppComponent appComponent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(getLayoutID(), container);
        unbinder = ButterKnife.bind(this,view);
        Log.d(TAG, "onCreateView: ");
        return view;

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        compositeDisposable = new CompositeDisposable();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        compositeDisposable.dispose();
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @LayoutRes
    public abstract int getLayoutID();


    public AppPref getPref() {
        return AppPref.getInstance(getActivity());
    }

    public User getUser(){
        return getBaseActivity().getUser();
    }


    public FragmentsHandlerActivity getFragmentHandlingActivity() {
        return ((FragmentsHandlerActivity)getActivity());
    }

    public BaseActivity getBaseActivity(){
        return ((BaseActivity)getActivity());
    }

    public void showProgress(String name){
        getBaseActivity().showProgress(name);
    }

    public void hideProgress(){
        getBaseActivity().hideProgress();
    }



    public void showSuccess(String message){
        getBaseActivity().showSuccess(message);
    }

    public void showError(String error){
        getBaseActivity().showError(error);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface DialogClickListener {
        public void onYesClick();
        public void onNoClick();
    }



}
