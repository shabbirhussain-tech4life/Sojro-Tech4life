package com.mdcbeta.base;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;

import com.mdcbeta.R;
import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.model.User;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.AppPref;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.mdcbeta.di.AppModule.profileimage;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements LifecycleRegistryOwner {


    private SweetAlertDialog pDialog;
    public static User user;
    private AppPref appPref;

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performInjection(getAppComponent());


    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return new LifecycleRegistry(this);
    }

    public abstract void performInjection(AppComponent appComponent);




    public void showProgress(String txt){
        hideProgress();
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText(txt+"...");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue));

    }




    public AppComponent getAppComponent(){
        return ((MyApplication)getApplicationContext()).getAppComponent();
    }


    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();

    }


    public void showError(String error){

        if(pDialog!=null && pDialog.isShowing()) {
            pDialog.setTitleText("Oops...")
                    .setContentText(error)
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

        }else {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(error);

            pDialog.show();
        }

    }

    public AppPref getAppPref(){
        if(appPref == null)
            appPref = AppPref.getInstance(this);
        return appPref;
    }

    public void showErrorWithTitle(String title,String error){

        if(pDialog!=null && pDialog.isShowing()) {
            pDialog.setTitleText(title)
                    .setContentText(error)
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

        }else {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(title)
                    .setContentText(error);

            pDialog.show();
        }

    }


    public void hideProgress(){
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }





    public void showSuccess(String message){
        if(pDialog!=null && pDialog.isShowing()) {
            pDialog.setTitleText("Success...")
                    .setContentText(message)
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

        }else {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success...")
                    .setContentText(message);

            pDialog.show();
        }

    }

    public User getUser(){
        if(user == null) {
            user = AppPref.getInstance(this).getUser(AppPref.Key.USER_LOGIN);
        }
        return user;
    }

    public void setUser(User tuser){
        if(tuser != null) {
            user = tuser;
            AppPref.getInstance(this).putUser(AppPref.Key.USER_LOGIN,tuser);
        }
    }

    public void setProfilePic(ImageView view , String path){
        if(path == null || TextUtils.isEmpty(path)) return;
        Picasso.with(getApplicationContext()).load(profileimage+path)
                .placeholder(R.drawable.profilepic)
                .into(view);
    }


    public interface OnTimerEnd{
        public void onEnd();
    }
}
