package com.mdcbeta.patient.PersonalCare;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Exercise;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.*;
import com.mdcbeta.util.Utils;
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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class FragmentExercise extends BaseFragment implements BaseDialogFragment.DialogClickListener,BlockingStep {

    Button  date, extime, ex_ok;
    dateshow d;
    timeshow t;
    String res, time;
    EditText txtexecomments, duration;
    Spinner typteofactivity;

    @BindView(R.id.main_list)
    RecyclerView exercise_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private ExerciseAdapter adapter;

    @BindView(R.id.exercisebtn)
    FloatingActionButton exercisebtn;

    @Inject
    ApiFactory apiFactory;

    List<Exercise> data = new ArrayList<>();

    private static final String TAG = "Step3Fragment";

    public static FragmentExercise newInstance() {
        FragmentExercise fragment = new FragmentExercise();
        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_exercise;
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

   @OnClick(R.id.exercisebtn)
    public void careGiverClick() {

        exercisedialog();

    }


   public void exercisedialog()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.exercisedialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;


        date = (Button) promptsView.findViewById(R.id.exdate);
        ex_ok = (Button) promptsView.findViewById(R.id.ex_ok);
        extime = (Button) promptsView.findViewById(R.id.extime);
         duration = (EditText) promptsView.findViewById(R.id.durationsss);
         typteofactivity= (Spinner) promptsView.findViewById(R.id.typeofactivity);
         txtexecomments = (EditText) promptsView.findViewById(R.id.txtexecomments);

        ArrayAdapter adapte = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_item, android.R.layout.simple_spinner_item);
        adapte.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        typteofactivity.setAdapter(adapte);

        date.setHint("Date");
        duration.setHint("Duration (minutes)");
        extime.setHint("Time");


        alertDialogBuilder.setTitle("EXERCISE");

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
              //  d =  new dateshow(date,getActivity());
            }
        });

        extime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  t =  new timeshow(extime,getActivity());
            }

        });

        promptsView.findViewById(R.id.ex_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        ex_ok.setOnClickListener(new View.OnClickListener() {
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
                String c = duration.getText().toString();

                String b = extime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];

                String d = typteofactivity.getSelectedItem().toString();

                if(a.equals("CHOOSE DATE")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }

                if(b.equals("CHOOSE TIME")){
                    toast.settext(getActivity(),"Select Date");
                    return;
                }


                if(TextUtils.isEmpty(c)){
                    toast.settext(getActivity(),"Duration is Empty");
                    return;
                }


                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty( d)
                        && !TextUtils.isEmpty(c)) {


                    showProgress("Loading");

                    apiFactory.addExerciseRecord(getUser().getId(), res,
                            time, duration.getText().toString(),
                            typteofactivity.getSelectedItem().toString(), txtexecomments.getText().toString(),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()), res+" "+time)
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    hideProgress();
                                    //   callback.onYesClick();
                                    //   dismiss();

                                //    getFragmentHandlingActivity().replaceFragment(FragmentExercise.newInstance());

                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));





                } else {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(), " Fields is Empty", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(Color.RED);
                    t.show();

                }

                ex_alert.cancel();

            }
        });


        ex_alert.show();
    }


    @Override
    public void onStart() {
        super.onStart();

     getExercise();



    }

    private void initList() {
        adapter = new ExerciseAdapter(getContext());
        exercise_list.setAdapter(adapter);
        exercise_list.setHasFixedSize(true);
        exercise_list.setItemAnimator(new DefaultItemAnimator());
        exercise_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        exercise_list.setLayoutManager(new LinearLayoutManager(getContext()));

     /*   adapter.setItemClickListner(object -> {

            getFragmentHandlingActivity().addFragmentWithBackstack(RecordDetailFragment.newInstance(
                    object.get("id").getAsString()));

        });*/
        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()){
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(RecordDetailFragment.newInstance(item.getid()));
                    //  Toast.makeText(getContext(), item.getGroup_id(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_edit:
                   exercisedialog();

                    apiFactory.getExerciseDetail(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {


                                    JsonObject data = resource.data.get("details").getAsJsonObject();


                                    try {
                                        date.setText(Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        extime.setText(data.get("time").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        duration.setText(data.get("duration").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        txtexecomments.setText(data.get("comment").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    }else {
                                        showError(resource.getMessage());
                                    }

                                },throwable -> {
                                    showError(throwable.getMessage());

                                });

                    ex_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                          showProgress("Loading");

                            apiFactory.updateExerciseRecord(item.getid(),date.getText().toString(),extime.getText().toString(), duration.getText().toString(),
                                    typteofactivity.getSelectedItem().toString(), txtexecomments.getText().toString()
                                    )

                                    .compose(RxUtil.applySchedulers())
                                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                                    .subscribe(model -> {
                                        if(model.status){
                                            showSuccess("Record Updated");
                                            getFragmentHandlingActivity().replaceFragment(FragmentExercise.this);
                                        }
                                        else {
                                            showError(model.getMessage());
                                        }

                                    },Throwable::printStackTrace);

                        }
            });

                                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.exerciseDelete(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(FragmentExercise.newInstance());
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;
            }

        });


    }

    @Override
    public void onYesClick() {



    }

    @Override
    public void onNoClick() {

    }

    public void getExercise()
    {
        apiFactory.getExercise(getUser().getId())
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
