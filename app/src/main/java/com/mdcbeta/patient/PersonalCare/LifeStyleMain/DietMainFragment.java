package com.mdcbeta.patient.PersonalCare.LifeStyleMain;

import android.app.AlertDialog;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mdcbeta.Events;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Diets;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.DietAdapter;
import com.mdcbeta.patient.PersonalCare.dateshow;
import com.mdcbeta.patient.PersonalCare.timeshow;
import com.mdcbeta.patient.PersonalCare.toast;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 05/04/2018.
 */

public class DietMainFragment extends BaseFragment implements BlockingStep {

    Button diet_date,diet_time,cancel,btn_ok;
    EditText txtdiet,txtquantity,txtcalories;

    @BindView(R.id.main_list)
    RecyclerView diet_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private DietAdapter adapter;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.opendialog)
    FloatingActionButton opendialog;

    static String foodtype;

    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @Inject
    MDCRepository repository;

    dateshow d;
    timeshow t;
    String res,time;

    List<Diets> data = new ArrayList<>();

    public DietMainFragment() {
        // Required empty public constructor
    }

    public static DietMainFragment newInstance() {
        DietMainFragment fragment = new DietMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initList();
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_diet_main;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

     //   initList();

        getRecord();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_maincourse:
                    foodtype = "Main Course";

                  //  Toast.makeText(getContext(),"Main Course", Toast.LENGTH_LONG).show();

                    onStart();

                    return true;
                case R.id.navigation_starters:

                    foodtype = "Starter";
                    return true;
                case R.id.navigation_drinks:

                    foodtype = "Drink";

                    return true;
                case R.id.navigation_others:

                    foodtype = "Other";

                    return true;
            }
            return false;
        }
    };


    private void initList() {
        adapter = new DietAdapter(getContext());
        diet_list.setAdapter(adapter);
        diet_list.setHasFixedSize(true);
        diet_list.setItemAnimator(new DefaultItemAnimator());
        diet_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        diet_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                //    getFragmentHandlingActivity()
                 //           .replaceFragmentWithBackstack(DietDetailFragment.newInstance(item.getUserId()));
                      Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.dietDelete(item.getId())
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
                 dietdialog();


               //     Toast.makeText(getContext(),"Demo", Toast.LENGTH_LONG).show();

               apiFactory.getDiet(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .subscribe(resource -> {

                                hideProgress();

                                if(resource.status) {

                                    JsonObject data = resource.data.get("details").getAsJsonObject();

                                    try {
                                        diet_date.setText(Utils.formatDate(data.get("date").getAsString()));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        diet_time.setText(data.get("time").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                   try {
                                        txtdiet.setText(data.get("name").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    try {
                                        txtquantity.setText(data.get("quantity").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        txtcalories.setText(data.get("calories").getAsString());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }else {
                                    showError(resource.getMessage());
                                }

                            },throwable -> {
                                showError(throwable.getMessage());

                            });



                     btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showProgress("Loading");

                            apiFactory.updateDietRecord(item.getId(),diet_date.getText().toString(),
                                    diet_time.getText().toString(), txtdiet.getText().toString(),
                                    txtquantity.getText().toString(), txtcalories.getText().toString()
                            )

                                    .compose(RxUtil.applySchedulers())
                                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                                    .subscribe(model -> {
                                        if(model.status){
                                            showSuccess("Record Updated");
                                            getFragmentHandlingActivity().replaceFragment(DietMainFragment.this);
                                        }
                                        else {
                                            showError(model.getMessage());
                                        }

                                    },Throwable::printStackTrace);

                        }
                    });

                    break;
            }
        });
    }


    public void getRecord()
    {
        showProgress("Loading");
        apiFactory.getDietList(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 -> {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                }, t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });

    }


    @Override
    public void onStart() {
        super.onStart();
        rxBus.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.STOP))
                .subscribe(o -> {

                    if(o instanceof Events.Success){
                        showSuccess("Case Created");
                        getRecord();

                    }
                    if(o instanceof Events.Failed){
                        showError(((Events.Failed) o).getMessage());
                    }


                },Throwable::printStackTrace);

    }


    @OnClick(R.id.opendialog)
    public void onViewClicked() {
       // getFragmentHandlingActivity().addDialog(this, DietDialog.newInstance(foodtype));
        dietdialog();
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

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


    public void dietdialog()
    {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_diet, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;
        diet_date = (Button) promptsView.findViewById(R.id.diet_date);
        diet_time = (Button) promptsView.findViewById(R.id.diet_time);
        txtdiet = (EditText) promptsView.findViewById(R.id.txtdiet);
        txtquantity = (EditText) promptsView.findViewById(R.id.txtquantity);
        txtcalories = (EditText) promptsView.findViewById(R.id.txtcalories);
        btn_ok = (Button) promptsView.findViewById(R.id.btn_ok);
        cancel = (Button) promptsView.findViewById(R.id.cancel);

        alertDialogBuilder.setTitle("Diet");

        diet_date.setOnClickListener(

        new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
           //     d =  new dateshow(diet_date,getActivity());
            }
        });

        diet_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // t =  new timeshow(diet_time,getActivity());
            }

        });


        promptsView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });


        promptsView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = diet_date.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String b = diet_time.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];


                String r = txtdiet.getText().toString();
                String s = txtcalories.getText().toString();
                String m = txtquantity.getText().toString();

                if(t== null || d==null){
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


                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(s))
                {

                    showProgress("Loading");

                  apiFactory.addDietRecord(getUser().getId(), res,
                            time, foodtype,
                            txtdiet.getText().toString(),
                            txtcalories.getText().toString(),txtquantity.getText().toString(),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    showSuccess("Record added successfully");
                                    hideProgress();


                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));



                } else {
                }

                ex_alert.cancel();

            }
        });

        ex_alert.show();

    }

    }

