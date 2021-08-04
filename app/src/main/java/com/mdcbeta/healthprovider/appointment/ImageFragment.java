package com.mdcbeta.healthprovider.appointment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.base.ImageUploadFragment;
import com.mdcbeta.data.model.GroupAndUser;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Age;
import com.mdcbeta.data.remote.model.GroupItem;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.dialog.AddPatientDialog;
import com.mdcbeta.patient.appointment.MediaAdapter;
import com.mdcbeta.patient.appointment.MediaResultsAdapter;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mdcbeta.widget.HorizontalFlowLayout;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.mdcbeta.patient.appointment.MyStepperAdapter.KEY_POSITION;

public class ImageFragment extends ImageUploadFragment implements BlockingStep {

    @Optional
    @BindView(R.id.camera_fab)
    FloatingActionButton cameraFab;
    @BindView(R.id.lvResults)
    ListView lvResults;

    private int position;


  private MediaResultsAdapter adapter;
 //   private MediaAdapter adapter;
    private static final String TAG = "StepFragment";


    @Override
    public int getLayoutID() {
        return R.layout.fragment_image_upload;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + position);


          //  initImagesUploadList();

            // Permission is not granted


       initImagesUploadList();

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    private void initImagesUploadList() {


       adapter = new MediaResultsAdapter(getActivity());
        //adapter= new MediaAdapter(getActivity());


       lvResults.setAdapter(adapter);
lvResults.clearFocus();
adapter.clearAll();



    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // c by kk 1/18/2020
//        if (getArguments() != null) {
//            position = getArguments().getInt(KEY_POSITION, 0);
//        }

        Log.d(TAG, "onCreate: " + position);



    }


    @Override
    public VerificationError verifyStep() {
        return null;
    }


    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }



    @OnClick(R.id.camera_fab)
    public void onViewClicked() {



        showImageDialog();

    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        adapter.addImage(list);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onSelected() {

        Log.d(TAG, "onSelected: " + position);

//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.FOREGROUND_SERVICE)
//                != PackageManager.PERMISSION_GRANTED) {

            List<ChosenImage> files = getPref().getImageList(AppPref.Key.STEP_LIST_IMAGES);
            if (files != null) {
                adapter.clearAll();
                adapter.addImage(files);
            }

            cameraFab.setVisibility(View.VISIBLE);
            lvResults.setVisibility(View.VISIBLE);


      //  } else {
           // showError("Permission Required");
            // requestPermissions(Manifest.permission.FOREGROUND_SERVICE, );
//            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.FOREGROUND_SERVICE)!=
//                    PackageManager.PERMISSION_GRANTED

//            List<ChosenImage> files = getPref().getImageList(AppPref.Key.STEP_LIST_IMAGES);
//            if (files != null) {
//                adapter.clearAll();
//                adapter.addImage(files);
//            }
//
//            cameraFab.setVisibility(View.VISIBLE);
//            lvResults.setVisibility(View.VISIBLE);


      //  }

    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {



        AppPref.getInstance(getActivity().getApplication())
                .putImageList(AppPref.Key.STEP_LIST_IMAGES, adapter.getFiles());

        callback.goToNextStep();


    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {



        List<ChosenImage> files = adapter.getFiles();
        getPref().putImageList(AppPref.Key.STEP_LIST_IMAGES, files);
        callback.goToPrevStep();




    }


}
