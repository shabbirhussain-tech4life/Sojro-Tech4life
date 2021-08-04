package com.mdcbeta.supportservice.radiology;


import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.HealthProviderViewModel;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by MahaRani on 12/11/2018.
 */

public class MainRecordDetailFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {

    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_case_code)
    TextView txt_case_code;
    @BindView(R.id.txt_datetime)
    TextView txtDatetime;
    @BindView(R.id.txt_test)
    TextView txt_test;
    @BindView(R.id.txt_pregnancy)
    TextView txt_pregnancy;
    @BindView(R.id.txt_sign)
    TextView txt_sign;
    @BindView(R.id.txt_instruction)
    TextView txt_instruction;
    @BindView(R.id.txt_diagnosis)
    TextView txt_diagnosis;

    @Inject
    MDCRepository mdcRepository;

    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;

    static String mId;
    static String booked_as;
    private CustomSpinnerAdapter<UserNames> adapter;
    ArrayList<String> images;
    String img_id;
    String lab_image;

    private static final String TAG = "CaseDetailFragment";
    private HealthProviderViewModel healthProviderViewModel;
    private LayoutInflater inflater;

    String url;
    String recordid;
    String casecode;
    String refererid;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_racord_details;
    }

    public static MainRecordDetailFragment newInstance(String id, String booked) {
        MainRecordDetailFragment fragment = new MainRecordDetailFragment();
        mId = id;
        booked_as = booked;
        return fragment;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        healthProviderViewModel = ViewModelProviders.of(getActivity()).get(HealthProviderViewModel.class);
        inflater = LayoutInflater.from(getContext());
        getData();

    }

    private void showHideUi() {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public void getData(){


        showProgress("Loading...");

        apiFactory.radiology_detail(mId)
                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {

                    images = new ArrayList<>();

                    hideProgress();

                    if(resource.status) {

                        JsonObject data = resource.data.get("details").getAsJsonObject();

                        if (data.get("case_code").getAsString().equalsIgnoreCase("no data") || TextUtils.isEmpty(data.get("case_code").getAsString())) {
                            showHideUi();

                            try {
                                casecode = "Case id: details" + data.get("case_code").getAsString();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            try {
                                txtName.setText(data.get("name").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txtDatetime.setText("Created: " + Utils.format12HourDateTime(Utils.UTCTolocalDateTime(data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_test.setText(data.get("addmoreimages").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_diagnosis.setText(data.get("diagnosis").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_instruction.setText(data.get("instructions").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_sign.setText(data.get("name").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        } else {

                            txt_case_code.setText("Case id: " + data.get("case_code").getAsString());

                            try {
                                casecode = data.get("case_code").getAsString();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            try {
                                txtDatetime.setText("Created: " + Utils.format12HourDateTime(Utils.UTCTolocalDateTime(data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            try {
                                txtName.setText(data.get("name").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                String value = data.get("addmoreimages").getAsString();
                                String result = value.replaceAll("\r\n", "<br>");
                                txt_test.setSingleLine(false);
                                txt_test.setText(Html.fromHtml(result));
                                //  txt_test.setText(data.get("addmoreimages").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_pregnancy.setText(data.get("pregnancy").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_diagnosis.setText(data.get("diagnosis").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_instruction.setText(data.get("instructions").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txt_sign.setText(data.get("name").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                    }else {

                        showError("");
                    }

                },throwable -> {

                });
    }




    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    @Override
    public void onStart() {
        super.onStart();

        getData();
    }
}

