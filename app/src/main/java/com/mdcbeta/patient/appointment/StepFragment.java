package com.mdcbeta.patient.appointment;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.ImageUploadFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mdcbeta.patient.appointment.MyStepperAdapter.KEY_POSITION;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public class StepFragment extends ImageUploadFragment implements BlockingStep {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.description)
    CardView description;
    @BindView(R.id.camera_fab)
    FloatingActionButton cameraFab;
    @BindView(R.id.lvResults)
    ListView lvResults;




    private int position;


    private MediaResultsAdapter adapter;

    private static final String TAG = "StepFragment";





    @Override
    public int getLayoutID() {
        return R.layout.fragment_step1;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + position);

        if (position == 1) {
            initImagesUploadList();
        }


    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    private void initImagesUploadList() {
        adapter = new MediaResultsAdapter(getActivity());
        lvResults.setAdapter(adapter);
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(KEY_POSITION, 0);
        }

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


    @Override
    public void onSelected() {

        Log.d(TAG, "onSelected: " + position);

        if (position == 0) {

            String text = getPref().getString(AppPref.Key.STEP_DISCRIPTION, null);
            if (text != null) {
                editText.setText(text);
            }

            description.setVisibility(View.VISIBLE);
            cameraFab.setVisibility(View.GONE);
            lvResults.setVisibility(View.GONE);


        } else if (position == 1) {

            List<ChosenImage> files = getPref().getImageList(AppPref.Key.STEP_LIST_IMAGES);
            if (files != null) {
                adapter.clearAll();
                adapter.addImage(files);
            }
            description.setVisibility(View.GONE);
            cameraFab.setVisibility(View.VISIBLE);
            lvResults.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        switch (position) {
            case 0:

//                if(TextUtils.isEmpty(editText.getText().toString())){
//                    showError("Please enter Description");
//                    return;
//                }

                AppPref.getInstance(getActivity().getApplication())
                        .put(AppPref.Key.STEP_DISCRIPTION, ""+editText.getText().toString());

                callback.goToNextStep();

                break;
            case 1:

                AppPref.getInstance(getActivity().getApplication())
                        .putImageList(AppPref.Key.STEP_LIST_IMAGES, adapter.getFiles());

                callback.goToNextStep();


                break;
            case 2:

                callback.goToNextStep();

                break;

        }

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {


        switch (position) {
            case 1:
                List<ChosenImage> files = adapter.getFiles();
                getPref().putImageList(AppPref.Key.STEP_LIST_IMAGES, files);
                callback.goToPrevStep();
                break;
            case 2:
                callback.goToPrevStep();
                break;
            case 3:
                callback.goToPrevStep();
                break;

        }

    }

}
