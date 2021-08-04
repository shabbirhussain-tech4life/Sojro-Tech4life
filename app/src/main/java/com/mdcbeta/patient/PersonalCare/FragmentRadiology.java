package com.mdcbeta.patient.PersonalCare;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Radiology;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class FragmentRadiology extends BaseFragment implements BlockingStep {

    @BindView(R.id.radiologybtn)
    FloatingActionButton radiologybtn;

    @BindView(R.id.main_list)
    RecyclerView exercise_list;

    int listviewSpace;

    private static int RESULT_LOAD_IMG = 1;
    String selectedImagePath = null;
    ImageView pic;
    dateshow d;
    timeshow t;
    String res;
    String rtime;
    private RadiologyAdapter adapter;
    List<Radiology> data = new ArrayList<>();

    @Inject
    ApiFactory apiFactory;

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_radiology;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initList();
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == -1
                    && null != data) {
                // Get the Image from data

                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(selectedImageUri,getActivity());

                Utils.efficientImageSet(selectedImagePath,pic);

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    @OnClick(R.id.radiologybtn)
    public void onViewClicked() {
        radiologydialog();
    }


    public void radiologydialog()
    {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.radiology_create_dailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;


        final Button date = (Button) promptsView.findViewById(R.id.radiology_date);
        final Button time = (Button) promptsView.findViewById(R.id.radiology_time);
        final Spinner type_of_image = (Spinner) promptsView.findViewById(R.id.radiology_type_of_image);
        final EditText radiology_comment = (EditText) promptsView.findViewById(R.id.radiology_comment);
        final Button attachImage = (Button) promptsView.findViewById(R.id.radiology_attach_btn);
        pic = (ImageView) promptsView.findViewById(R.id.radiology_attach_image);
        final Button cancel = (Button) promptsView.findViewById(R.id.radiolog_cancel);
        final Button ok = (Button) promptsView.findViewById(R.id.radiolog_ok);


        final ArrayAdapter spinneradapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.radiology_array,
                android.R.layout.simple_spinner_item);

        spinneradapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        type_of_image.setAdapter(spinneradapter);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //   d = new dateshow(date,getActivity());
            }
        });

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v == time) {


                  //  t = new timeshow(time,getActivity());
                }

            }
        });



        attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t== null || d==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }

                String a = date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String b = time.getText().toString();
                String[] parts = b.split(" ");
                rtime = parts[0];

                if(a== null || b==null){
                    toast.settext(getActivity(),"Time and Date is Empty");
                    return;
                }

                if(a.equals("CHOOSE DATE")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(b.equals("CHOOSE TIME")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }


                if (!TextUtils.isEmpty(date.getText().toString())
                        && !TextUtils.isEmpty(type_of_image.getSelectedItem().toString())
                        && !TextUtils.isEmpty(time.getText().toString())
                        && !TextUtils.isEmpty(selectedImagePath)
                        )
                {

                    showProgress("Loading");

                    apiFactory.addRadiologiesRecord(getUser().getId(), res,
                            rtime, type_of_image.getSelectedItem().toString(),
                            selectedImagePath, radiology_comment.getText().toString(),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    hideProgress();
                                    //   callback.onYesClick();
                                    //   dismiss();

                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));


                }else{


                    Toast t = Toast.makeText(getActivity().getApplicationContext(), " Fields is Empty", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(Color.RED);
                    t.show();
                }

                ex_alert.cancel();

            }
        });

        ex_alert.show();

    }

    private void initList() {
        adapter = new RadiologyAdapter(getContext());
        exercise_list.setAdapter(adapter);
        exercise_list.setHasFixedSize(true);
        exercise_list.setItemAnimator(new DefaultItemAnimator());
        exercise_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        exercise_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:

                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.radiologiesDelete(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    initList();
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;

                case R.id.btn_edit:

                    break;
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();

        getRadiology();

    }

    public void getRadiology()
    {
        apiFactory.getRadiologies(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 ->  {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                } , t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });

    }


}
