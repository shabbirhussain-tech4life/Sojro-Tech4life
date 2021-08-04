package com.mdcbeta.authenticate;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.request_model.Login;
import com.mdcbeta.databinding.ActivityLoginBinding;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.patient.video.VideoChatFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;

import javax.inject.Inject;

import static com.mdcbeta.util.Utils.deleteDir;

public class LoginActivity extends BaseActivity {

    boolean isPatient = false;
    boolean isRadiology = false;
    boolean isHealth = false;
    boolean isLaboratory = false;
    boolean isPharmacy = false;
    boolean isRemember = false;
    boolean isLab = false;
    boolean isRad = false;
    boolean isPharm = false;



    private SharedPreferences sharedPreferences ;
    private static final String PREFS_NAME = "PrefsFile";
    private static final String TAG = "LoginActivity";

    @Inject
    ApiFactory apiFactory;

    private ActivityLoginBinding ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// added by kk 12/29/2020
      AppPref.getInstance(this).clearAll();


        Configuration config = getResources().getConfiguration();
        sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        ly =  DataBindingUtil.setContentView(this, R.layout.activity_login);



      ActivityCompat.requestPermissions(LoginActivity.this,
        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
          ,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.FOREGROUND_SERVICE},
        1);


        getPreferenceData();

        showUI();
        setListener();


    }

    private void getPreferenceData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(sp.contains("pref_name"))
        {
            String u = sp.getString("pref_name", "not found");
            ly.username.setText(u.toString());


        }
        if(sp.contains("pref_password"))
        {
            String p = sp.getString("pref_password", "not found");
            ly.password.setText(p.toString());

        }

        if(sp.contains("pref_checkbox"))
        {
            Boolean b  = sp.getBoolean("pref_checkbox", true);
            ly.checkBox.setChecked(b);
        }

    }

    //ADDED BY KANWAL KHAN
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) ||
              (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c,R.style.AlertDialog);

        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });

        return builder;
    }


    private void showUI() {
        if(getAppPref().getBoolean(AppPref.Key.IS_REMEMBER)){
            if (getAppPref().getBoolean(AppPref.Key.IS_REMEMBER_IS_HEALTH)){
                ly.healthRadio.setChecked(true);
                isHealth = true;
            }else if (getAppPref().getBoolean(AppPref.Key.IS_REMEMBER_IS_RADIOLOGY)){
                ly.healthRadio.setChecked(false);
                ly.radiologyRadio.setChecked(true);
                isRad = true;
            }else if (getAppPref().getBoolean(AppPref.Key.IS_REMEMBER_IS_LABORATORY)){
                ly.healthRadio.setChecked(false);
                ly.labRadio.setChecked(true);
                isLab = true;
            }else if (getAppPref().getBoolean(AppPref.Key.IS_REMEMBER_IS_PHARMACY)){
                ly.healthRadio.setChecked(false);
                ly.pharmacyRadio.setChecked(true);
                isPharm = true;
            }


            //  ly.username.setText(getAppPref().getString(AppPref.Key.IS_REMEMBER_USERNAME));
            // ly.password.setText(getAppPref().getString(AppPref.Key.IS_REMEMBER_PASSWORD));
            // ly.checkBox.setChecked(true);
            //isRemember = true;


            if(validate()){
                login();
            }
        }

    }

    private void setListener() {
        ly.button.setOnClickListener(v -> {


//
//            if(ly.radioGroup.getCheckedRadioButtonId()==-1)
//            {
//                Context context = getApplicationContext();
//                CharSequence text = "Select a role first!";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
//                // showProgress("Select a role first !");
//            }
         //   else ISSE PHLE IT WAS else if
              if (validate()){
                if(!isConnected(LoginActivity.this))
                {
                    buildDialog(LoginActivity.this).show();
                }
                else{
                    login(); }
            }
        });


        ly.healthRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isHealth = true;
                isPatient = false;
                ly.radioGroup1.setVisibility(View.GONE);
            }
        });
        ly.facilityRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ly.radioGroup1.setVisibility(View.VISIBLE);
            }
        });
        ly.labRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isLab = true;
            }
        });

        ly.pharmacyRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isPharm = true;
            }
        });

        ly.radiologyRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isRad = true;
            }
        });

        ly.signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        ly.forgotPass.setOnClickListener(v -> {
           FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(android.R.id.content, VideoChatFragment.newInstance(null,true));
            transaction.addToBackStack(null);
            transaction.commit();
       //   startActivity(new Intent(this, ForgotPassword.class));
        });

        ly.checkBox.setOnCheckedChangeListener
                ((buttonView, isChecked) ->
                        isRemember = isChecked);


    }


    private void login() {
        if(ly.checkBox.isChecked())
        {
            Boolean booleanIsChecked = ly.checkBox.isChecked();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("pref_name", ly.username.getText().toString());
            editor.putString("pref_password", ly.password.getText().toString());
            editor.putBoolean("pref_checkbox", booleanIsChecked);
            editor.apply();
         //   Toast.makeText(getApplicationContext(),"Settings have been saved",Toast.LENGTH_SHORT).show();

        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            sharedPreferences.edit().apply();
        }

        Login login = new Login(ly.username.getText().toString(),ly.password.getText().toString(),"1");

        showProgress("Loading");

        apiFactory.login(login)
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(ActivityEvent.STOP))
                .subscribe(response -> {
                    hideProgress();




                    if(response.status){

                        hideProgress();
                        AppPref.getInstance(LoginActivity.this).remove(AppPref.Key.USER_LOGIN);
                        AppPref.getInstance(LoginActivity.this).putUser(response.getData().get(0));


                        //  getAppPref().put(AppPref.Key.IS_REMEMBER,isRemember);
//                        getAppPref().put(AppPref.Key.IS_REMEMBER_IS_PATIENT,isPatient);
//                      getAppPref().put(AppPref.Key.IS_REMEMBER_IS_RADIOLOGY,isRad);
//                      getAppPref().put(AppPref.Key.IS_REMEMBER_IS_LABORATORY,isLab);
//                      getAppPref().put(AppPref.Key.IS_REMEMBER_IS_PHARMACY,isPharm);
//                      getAppPref().put(AppPref.Key.IS_REMEMBER_USERNAME,ly.username.getText().toString());
//                      getAppPref().put(AppPref.Key.IS_REMEMBER_PASSWORD,ly.password.getText().toString());


//                         if (isHealth){

                            AppPref.getInstance(LoginActivity.this).put(AppPref.Key.IS_HEALTH_LOGIN, true);
                           MainHealthProviderActivity.start(LoginActivity.this);
//                        Intent intent = new Intent(LoginActivity.this,MainActivityjitsi.class);
//                        startActivity(intent);

//                        }else if (isRad) {
//
//                            AppPref.getInstance(LoginActivity.this).put(AppPref.Key.IS_RADIOLOGY_LOGIN, true);
//                            MainSupportServiceActivity.start(LoginActivity.this);
//
//                        } else if (isLab){
//
//                            AppPref.getInstance(LoginActivity.this).put(AppPref.Key.IS_LABORATORY_LOGIN, true);
//                            MainLabActivity.start(LoginActivity.this);
//
//                        }else if (isPharm){
//
//                            AppPref.getInstance(LoginActivity.this).put(AppPref.Key.IS_PHARMACY_LOGIN, true);
//                            MainPrescriptionActivity.start(LoginActivity.this);
//                        }
//
//
//                        finish();
//
                   }


                    else {
                        showError(response.message);
                    }

                },throwable -> {
                    showError(throwable.getMessage());
                    throwable.printStackTrace();
                });



    }


    @Override
    public void performInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }


    public boolean validate(){
        return Utils.setRequiredField(ly.username,ly.password);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUser();
    }
// added by kk 7/15/2020 for clear cache
  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      trimCache(getApplicationContext()); //if trimCache is static
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void trimCache(Context applicationContext) {
    try {
      File dir = getCacheDir();
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      switch (requestCode) {
          case 1: {

              // If request is cancelled, the result arrays are empty.
              if (grantResults.length > 0
                      && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                  // permission was granted, yay! Do the
                  // contacts-related task you need to do.
              } else {

                  // permission denied, boo! Disable the
                  // functionality that depends on this permission.
                  Toast.makeText(LoginActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
              }
              return;
          }

          // other 'case' lines to check for other
          // permissions this app might request
      }
  }
}


