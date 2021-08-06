package com.mdcbeta.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;


import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mdcbeta.R;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.util.AppPref;


import java.io.File;

import static com.mdcbeta.util.Utils.deleteDir;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public abstract class FragmentsHandlerActivity extends BaseActivity {



    protected abstract int fragmentContainerId();

    public void initRootFragment(Bundle savedInstanceState, Fragment fragment){
        if (findViewById(fragmentContainerId()) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(fragmentContainerId(), fragment).commit();
        }
    }




    public void replaceFragmentWithBackstack(Fragment fragment) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out);
        trans.replace(fragmentContainerId(), fragment);
        trans.addToBackStack(fragment.getClass().getName());
        trans.commit();
    }

    public void addFragmentWithBackstack(Fragment fragment) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out);
        trans.add(fragmentContainerId(), fragment);
        trans.addToBackStack(fragment.getClass().getName());
        trans.commit();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        //trans.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out);
        trans.replace(fragmentContainerId(), fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //trans.addToBackStack(fragment.getClass().getName());
        trans.commit();
    }

    public void addDialog(DialogFragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, fragment.getClass().getName());
    }

    public void addDialog(Fragment targetFragment,DialogFragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fragment.setTargetFragment(targetFragment, 0);
        fragment.show(fm, fragment.getClass().getName());
    }

    public boolean actionBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setIcon(R.drawable.ic_dialog_alert)
//                .setTitle("You want to exit ?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//
//                    super.onBackPressed();
//
//
//                })
//                .setNegativeButton("No", null)
//                .show();



        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_dialog_alert)
                .setTitle("You want to exit ?")
                .setPositiveButton("Yes", (dialog, which) -> {
              super.onBackPressed();


                })
                .setNegativeButton("No", null)
                .show();
            //additional code
        }
        else if(count==1)
        {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle("You want to go back ?")
                    .setPositiveButton("", (dialog, which) -> {
                        super.onBackPressed();


                    })
                    .setNegativeButton("", null)
                    .show();
            getSupportFragmentManager().popBackStack();

        }



        else {
            getSupportFragmentManager().popBackStack();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


  @Override
  protected void onDestroy() {
  //  Toast.makeText(getApplicationContext(),"on destroy fragmentHandleractivity",Toast.LENGTH_SHORT).show();
    super.onDestroy();
//    AppPref.getInstance(this).clearAll();
//    AppPref.getInstance(this).remove(AppPref.Key.USER_LOGIN, AppPref.Key.IS_PATIENT_LOGIN,AppPref.Key.KEY_RECORD_ID);
//    MainHealthProviderActivity.isReviewer = false;
//    BaseActivity.user = null;
//    LoginActivity.start(this);
//    finish();
//    try {
//      trimCache(); //if trimCache is static
//      finish();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }

  public void trimCache() {
     // Toast.makeText(getApplicationContext(),"memory",Toast.LENGTH_SHORT).show();
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
  protected void onStop() {
  //  Toast.makeText(getApplicationContext(),"on stop fragmentHandleractivity",Toast.LENGTH_SHORT).show();
//    Toast.makeText(getApplicationContext(),"base fragmen destroy",Toast.LENGTH_SHORT).show();
//    Log.e("base","base");
    super.onStop();
  }

  @Override
  protected void onPause() {
   // Toast.makeText(getApplicationContext(),"on pause fragmentHandleractivity",Toast.LENGTH_SHORT).show();
   super.onPause();
  }
}





