package com.mdcbeta.base;


import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;

import com.mdcbeta.app.MyApplication;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.data.model.User;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.patient.PatientMainActivity;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;


import java.io.File;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mdcbeta.util.Utils.deleteDir;


/**
 * Created by shakil on 3/8/2017.
 */

public abstract class BaseFragment extends RxFragment implements LifecycleRegistryOwner {

    private Unbinder unbinder;
    private ActionBar actionBar;
    private InputMethodManager imm;

    public View rootView;

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @LayoutRes
    public abstract int getLayoutID();

    public abstract void getActionBar(ActionBar actionBar);

    public abstract void provideInjection(AppComponent appComponent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(getLayoutID(), container, false);
        rootView = view;
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return new LifecycleRegistry(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        provideInjection(((MyApplication)getActivity().getApplicationContext()).getAppComponent());


        imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onStop() {
    //  Toast.makeText(getContext(),"stop",Toast.LENGTH_SHORT).show();
        super.onStop();
//      try {
//      trimCache(); //if trimCache is static
//      getActivity().finish();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public AppPref getPref() {
        return AppPref.getInstance(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();


//        if(getActivity()!=null && getActivity() instanceof  PatientMainActivity){
//            actionBar = ((PatientMainActivity) getActivity()).provideActionBar();
//        }
        if(getActivity()!=null && getActivity() instanceof MainHealthProviderActivity){
            actionBar = ((MainHealthProviderActivity) getActivity()).provideActionBar();
        }
        getActionBar(actionBar);

    }


    @Override
    public void onDestroy() {
     // Toast.makeText(getContext(),"destroy",Toast.LENGTH_SHORT).show();
       super.onDestroy();

    }

    public void hideKeyboard(View view){
        if(imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
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

    public void showErrorWithTitle(String title,String error){
        getBaseActivity().showErrorWithTitle(title,error);
    }


//    @Override
//    public void onDestroyOptionsMenu() {
//        super.onDestroyOptionsMenu();
//    }


  @Override
  public void onPause() {
  //  Toast.makeText(getContext(),"pause",Toast.LENGTH_SHORT).show();
    super.onPause();
 //   AppPref.getInstance(getContext()).clearAll();
    //AppPref.getInstance(getContext()).remove(AppPref.Key.USER_LOGIN, AppPref.Key.IS_PATIENT_LOGIN,AppPref.Key.KEY_RECORD_ID);
   // MainHealthProviderActivity.isReviewer = false;
   // BaseActivity.user = null;
    //LoginActivity.start(getContext());
//    try {
//      trimCache(); //if trimCache is static
//      getActivity().finish();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

  }


  public void trimCache() {
    // Toast.makeText(getApplicationContext(),"memory",Toast.LENGTH_SHORT).show();
    try {
      File dir = getActivity().getCacheDir();
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);

      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

//  @Override
//  public void onDetach() {
//    Toast.makeText(getContext(),"base fragmen destroy",Toast.LENGTH_SHORT).show();
//    Log.e("base","base");
//    super.onDetach();
//  }



}
