package com.mdcbeta.healthprovider.cases;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.model.Diagnosis;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.HealthProviderViewModel;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.cases.dialog.AddLaboratoryDialog;
import com.mdcbeta.healthprovider.cases.dialog.AddPrescrptionDialog;
import com.mdcbeta.healthprovider.cases.dialog.AddRadiologyDialog;
import com.mdcbeta.healthprovider.cases.dialog.EditCommentDialog;
import com.mdcbeta.supportservice.Pharmacy.PrescriptionDetailFragment;
import com.mdcbeta.supportservice.laboratory.LabDetailFragment;
import com.mdcbeta.supportservice.radiology.MainRecordDetailFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.SingleUploadBroadcastReceiver;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mdcbeta.widget.FlowLayout;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import net.gotev.uploadservice.MultipartUploadRequest;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MahaRani on 07/05/2018.
 */

public class Appointment1DetailFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {

    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_age)
    TextView txtAge;
    @BindView(R.id.txt_datetime)
    TextView txtDatetime;
    @BindView(R.id.txt_medical_complain)
    TextView txt_medical_complain;
    @BindView(R.id.txt_history)
    TextView txtHistory;
  @BindView(R.id.phone_number)
  TextView phone;
  @BindView(R.id.phone_number2)
  TextView phone2;
    @BindView(R.id.image_container)
    FlowLayout imageContainer;
    @BindView(R.id.label_comment)
    TextView label_comment;
    @BindView(R.id.txt_case_code)
    TextView txt_case_code;
    @BindView(R.id.txt_gender_field)
    TextView txt_gender_field;
    @BindView(R.id.send_comment)
    Button send_comment;
    @BindView(R.id.add_comment)
    EditText add_comment;
    @BindView(R.id.comment_container)
    LinearLayout comment_container;
    @BindView(R.id.send_radiology)
    Button send_radiology;
    @BindView(R.id.send_prescription)
    Button send_prescription;
    @BindView(R.id.send_laboratory)
    Button send_laboratory;
    @BindView(R.id.lab_container)
    LinearLayout lab_container;
    @BindView(R.id.pres_container)
    LinearLayout pres_container;
    @BindView(R.id.expandableButton3)
    Button expandableButton3;
    @BindView(R.id.expandableLayout3)
    ExpandableRelativeLayout expandableLayout3;
    @BindView(R.id.expandableButton4)
    Button expandableButton4;
    @BindView(R.id.expandableLayout4)
    ExpandableRelativeLayout expandableLayout4;
    @BindView(R.id.expandableButton5)
    Button expandableButton5;
    @BindView(R.id.expandableLayout5)
    ExpandableRelativeLayout expandableLayout5;
    @BindView(R.id.comment_container1)
    LinearLayout comm_container;
    @Inject
    MDCRepository mdcRepository;
    @BindView(R.id.vital_layout)
    LinearLayout vital_layout;
    @BindView(R.id.table_main)
    TableLayout stk;
    @BindView(R.id.table_date)
    TableLayout stkdate;
    @BindView(R.id.expandableButton)
    Button expandableButton;
    @BindView(R.id.expandableButton2)
    Button expandableButton2;
    @BindView(R.id.table_main1)
    TableLayout stk1;
    @BindView(R.id.commentImagesContainer)
    LinearLayout commentImages;
    @BindView(R.id.addImageContainer)
    LinearLayout addImageContainer;
    @BindView(R.id.mainScrollView)
    ScrollView mainScrollView;

    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;

    static String mId;
    static String booked_as;
    private CustomSpinnerAdapter<UserNames> adapter;
    ArrayList<String> images;

    private static final String TAG = "CaseDetailFragment";
    private HealthProviderViewModel healthProviderViewModel;
    private LayoutInflater inflater;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    final int ACTIVITY_SELECT_IMAGE = 1234;
    private List<String> commentImagesPath = new ArrayList<>();
  private List<MultipartBody.Part> commentImageFiles = new ArrayList<>();
    private int commentImageCounter = 0;



    String url;
    String recordid;
    String casecode;
  static  String refererid;
    String img_id;
    String lab_image;
    static String case_code;


  LinearLayout containerDiagnosis;
  ImageButton btnAddMore;
  LinearLayout containerDiagnosisByRev, diagnosisByRev;
  EditText etDiagnosisByRev;
  LinearLayout llAddDiagnosis;
  TextView txtDiagnosisByRev;
  TextView txtDiagnosis;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_appointment_details;
    }

    public static Fragment newInstance(String id, String casecode) {
        Appointment1DetailFragment fragment = new Appointment1DetailFragment();
        mId = id;
        case_code = casecode;

        return fragment;
    }


    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        healthProviderViewModel = ViewModelProviders.of(getActivity()).get(HealthProviderViewModel.class);
        inflater = LayoutInflater.from(getContext());
//kanwal

      if (!MainHealthProviderActivity.isReviewer) {
        llAddDiagnosis.setVisibility(View.GONE);
        send_laboratory.setVisibility(View.GONE);
        send_radiology.setVisibility(View.GONE);
        send_prescription.setVisibility(View.GONE);
        // send_comment.layout(100,100,100);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        send_comment.setLayoutParams(lp);
      }

      else {
       }







        stk.removeAllViews();

        TableRow tbrow0 = new TableRow(getContext());
      TextView tv = new TextView(getContext());
      tv.setText(" Date ");
      tv.setTextColor(Color.BLACK);
      tv.setTypeface(null, Typeface.BOLD);
      tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
      tbrow0.addView(tv );
        TextView tv1 = new TextView(getContext());
        tv1.setText(" Probe Temperature ");
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getContext());
        tv2.setText(" Temperature ");
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getContext());
        tv3.setText(" Temp type ");
        tv3.setTextColor(Color.BLACK);
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(getContext());
        tv4.setText("  Blood pressure ");
        tv4.setTextColor(Color.BLACK);
        tv4.setTypeface(null, Typeface.BOLD);
        tv4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));

        tbrow0.addView(tv4);
//        TextView tv5 = new TextView(getContext());
//        tv5.setText(" Arm ");
//        tv5.setTextColor(Color.BLACK);
//        tv5.setTypeface(null, Typeface.BOLD);
//        tv5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//      tv5.setVisibility(View.GONE);
//        tbrow0.addView(tv5);
        TextView tv5 = new TextView(getContext());
      tv5.setText(" B.P position ");
      tv5.setTextColor(Color.BLACK);
      tv5.setTypeface(null, Typeface.BOLD);
      tv5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(getContext());
      tv6.setText(" Pulse rate (beats/min) ");
      tv6.setTextColor(Color.BLACK);
      tv6.setTypeface(null, Typeface.BOLD);
      tv6.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
      tbrow0.addView(tv6);
        TextView tv7 = new TextView(getContext());
      tv7.setText(" Pulse ");
      tv7.setTextColor(Color.BLACK);
      tv7.setTypeface(null, Typeface.BOLD);
      tv7.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv7);
        TextView tv8 = new TextView(getContext());
      tv8.setText(" Resp rate (breaths/min) ");
      tv8.setTextColor(Color.BLACK);
      tv8.setTypeface(null, Typeface.BOLD);
      tv8.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv8);
        TextView tv9 = new TextView(getContext());
      tv9.setText(" Oxygen saturation (%) ");
      tv9.setTextColor(Color.BLACK);
      tv9.setTypeface(null, Typeface.BOLD);
      tv9.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv9);
        TextView tv10 = new TextView(getContext());
      tv10.setText(" Saturation taken ");
      tv10.setTextColor(Color.BLACK);
      tv10.setTypeface(null, Typeface.BOLD);
      tv10.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv10);
        TextView tv11 = new TextView(getContext());
      tv11.setText(" Weight ");
      tv11.setTextColor(Color.BLACK);
      tv11.setTypeface(null, Typeface.BOLD);
      tv11.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv11);
        TextView tv12 = new TextView(getContext());
      tv12.setText(" Height ");
      tv12.setTextColor(Color.BLACK);
      tv12.setTypeface(null, Typeface.BOLD);
      tv12.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv12);
//      TextView tv14 = new TextView(getContext());
//      tv14.setText(" Pain present ");
//      tv14.setTextColor(Color.BLACK);
//      tv14.setTypeface(null, Typeface.BOLD);
//      tv14.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//      tv14.setVisibility(View.GONE);
//      tbrow0.addView(tv14);
      stk.addView(tbrow0);


//        TableRow tbrow1 = new TableRow(getContext());
//        TextView tvdate = new TextView(getContext());
//        tvdate.setText(" Date ");
//        tvdate.setTextColor(Color.BLACK);
//        tvdate.setTypeface(null, Typeface.BOLD);
//        tvdate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//        tbrow1.addView(tvdate);
//        stkdate.addView(tbrow1);

        stk1.removeAllViews();

        TableRow tbrow10 = new TableRow(getContext());
        TextView ph1 = new TextView(getContext());
        ph1.setText(" Date of origin");
        ph1.setTextColor(Color.BLACK);
        ph1.setTypeface(null, Typeface.BOLD);
        ph1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph1);
        TextView ph2 = new TextView(getContext());
        ph2.setText(" Referred to ");
        ph2.setTextColor(Color.BLACK);
        ph2.setTypeface(null, Typeface.BOLD);
        ph2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph2);
        TextView ph3 = new TextView(getContext());
        ph3.setText(" Date of review ");
        ph3.setTextColor(Color.BLACK);
        ph3.setTypeface(null, Typeface.BOLD);
        ph3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph3);
        stk1.addView(tbrow10);


        getData();

    }

    private void showHideUi() {
        label_comment.setVisibility(View.GONE);
        comment_container.setVisibility(View.GONE);
        send_comment.setVisibility(View.GONE);
        txtAge.setText("");
        txt_gender_field.setText("");
        txtLocation.setText("");

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @SuppressLint("CheckResult")
    public void getData() {


        showProgress("Loading...");

    //  Toast.makeText(getContext(), "case_code"+case_code, Toast.LENGTH_SHORT).show();
        apiFactory.mycasesDetails(mId, case_code)

                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {

                    images = new ArrayList<>();


                    hideProgress();

                    if (resource.status) {


                        JsonObject data = resource.data.get("details").getAsJsonObject();
                      refererid = data.get("referer_id").getAsString();
               //       Toast.makeText(getContext(),"referee"+ data.get("referer_id").getAsString(),Toast.LENGTH_SHORT).show();

                        if (data.get("case_code").getAsString().equalsIgnoreCase("no data")
                                || TextUtils.isEmpty(data.get("case_code").getAsString())) {

                            showHideUi();


                            try {
                                txtName.setText(data.get("fname").getAsString());


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                          try {
                            phone.setText(data.get("patient_cell_phone").getAsString());
                          } catch (Exception ex) {
                            ex.printStackTrace();
                          }

                          try {
                            phone2.setText(data.get("patient_cell_phone2").getAsString());
                          } catch (Exception ex) {
                            ex.printStackTrace();
                          }




                            try {
                                txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime
                                        (data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


//                            try {
//                                txt_gender_field.setText(data.get("gender").getAsString().equalsIgnoreCase("f") ? "Female" : "Male");
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }

                          try {
                                txt_gender_field.setText(data.get("gender").getAsString());
                        } catch (Exception ex) {
                        ex.printStackTrace();
                      }

                            try {
                                txtLocation.setText(data.get("area").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        } else {

                            txt_case_code.setText(data.get("case_code").getAsString());

                            try {

                                txtAge.setText(data.get("y_age").getAsString() + " Years " + (!TextUtils.isEmpty(data.get("month_age").getAsString()) ? "" + data.get("month_age").getAsString() + " Months" : ""));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(data.get("created_at").getAsString())));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


//                            try {
//                                txt_gender_field.setText(data.get("gender").getAsString().equalsIgnoreCase("f") ? "Female" : "Male");
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
                          try {
                            txt_gender_field.setText(data.get("gender").getAsString());
                          } catch (Exception ex) {
                            ex.printStackTrace();
                          }

                            try {
                                txtLocation.setText(data.get("area").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                txtName.setText(data.get("fname").getAsString() + " " + data.get("lname").getAsString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                          try {
                            phone.setText(data.get("patient_cell_phone").getAsString());
                          } catch (Exception ex) {
                            ex.printStackTrace();
                          }

                          try {
                            phone2.setText(data.get("patient_cell_phone2").getAsString());
                          } catch (Exception ex) {
                            ex.printStackTrace();
                          }

                        }

                        try {
                            txtHistory.setText(Html.fromHtml(data.get("history").getAsString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        try {
                            txt_medical_complain.setText(Html.fromHtml(data.get("data").getAsString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                       try {
                            int commemtsSize = resource.data.get("recorddata").getAsJsonArray().size();

                            for (int i = 0; i < 10; i++) {
                                JsonObject comment = resource.data.get("recorddata").getAsJsonArray().get(i).getAsJsonObject();

//                                if (!comment.get("oraltemperature").getAsString().isEmpty() ||
//                                        !comment.get("systolic").getAsString().isEmpty() ||
//                                        !comment.get("hands").getAsString().isEmpty()||
//                                        !comment.get("bloodpressureposition").getAsString().isEmpty()
//
//
//                                ) {
//
//                                    TableRow tbrow1 = new TableRow(getContext());
//                                    TextView tvdate1 = new TextView(getContext());
//                                //   tvdate1.setText(Utils.format12HourDateTime(comment.get("created_at").getAsString()));
//                                 //   tvdate1.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("created_at").getAsString())));
//                              tvdate1.setText(comment.get("created_at").getAsString());
//                                    tvdate1.setTextColor(Color.BLACK);
//                                    tvdate1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//                                    tbrow1.addView(tvdate1);
//                                    stkdate.addView(tbrow1);
/*
                                    TableRow tbrow = new TableRow(getContext());
                              TextView tvdate = new TextView(getContext());
                              tvdate.setText(Utils.format12HourDateTime(comment.get("created_at").getAsString()));
//                                 //   tvdate1.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("created_at").getAsString())));
                          //    tvdate.setText(comment.get("created_at").getAsString());
                                    tvdate.setTextColor(Color.BLACK);
                                    tvdate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                    tbrow.addView(tvdate);
                                    TextView t1v = new TextView(getContext());
                                   if (!TextUtils.isEmpty(comment.get("probetemperature").getAsString())) {

                                     t1v.setText(comment.get("probetemperature").getAsString() + " " + comment.get("probetemperaturevalue").getAsString());
                                  }

                              t1v.setTextColor(Color.BLACK);
                              t1v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t1v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t1v);
//
                              TextView t2v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("temperature").getAsString())) {

                                t2v.setText(comment.get("temperature").getAsString() + " " + comment.get("temperaturevalue").getAsString());
                              }

                              t2v.setTextColor(Color.BLACK);
                              t2v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t2v.setGravity(Gravity.CENTER);
                              tbrow.addView(t2v);
////
                              TextView t3v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("temperature").getAsString())) {
                                t3v.setText(comment.get("oraltemperature").getAsString()); }
                              t3v.setTextColor(Color.BLACK);
                              t3v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t3v.setGravity(Gravity.CENTER);
                              tbrow.addView(t3v);
//////
                              TextView t4v = new TextView(getContext());
                                if (!TextUtils.isEmpty(comment.get("systolic").getAsString())) {
                                        t4v.setText(comment.get("systolic").getAsString() + "/" + comment.get("diastolic").getAsString());
                                    }
                              t4v.setTextColor(Color.BLACK);
                              t4v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t4v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t4v);
//                                   TextView t5v = new TextV iew(getContext());
//                                    t5v.setText(comment.get("hands").getAsString());
//                                   t5v.setTextColor(Color.BLACK);
//                                   t5v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//                                   t5v.setGravity(Gravity.CENTER);
//                                  t5v.setVisibility(View.GONE);
//                                   tbrow.addView(t5v);
                                    TextView t5v = new TextView(getContext());
                                  if (!TextUtils.isEmpty(comment.get("systolic").getAsString())) {
                                    t5v.setText(comment.get("bloodpressureposition").getAsString()); }
                              t5v.setTextColor(Color.BLACK);
                              t5v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t5v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t5v);
                                   TextView t6v = new TextView(getContext());
                              t6v.setText(comment.get("pulse").getAsString());
                              t6v.setTextColor(Color.BLACK);
                              t6v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t6v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t6v);
                                 TextView t7v = new TextView(getContext());
                                  if (!TextUtils.isEmpty(comment.get("pulse").getAsString())) {
                                    t7v.setText(comment.get("pulsepres").getAsString()); }
                              t7v.setTextColor(Color.BLACK);
                              t7v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t7v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t7v);
                                    TextView t8v = new TextView(getContext());
                              t8v.setText(comment.get("respiratoryrate").getAsString());
                              t8v.setTextColor(Color.BLACK);
                              t8v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t8v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t8v);
                                    TextView t9v = new TextView(getContext());
                              t9v.setText(comment.get("saturation").getAsString());
                              t9v.setTextColor(Color.BLACK);
                              t9v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t9v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t9v);
                                    TextView t10v = new TextView(getContext());
                                  if (!TextUtils.isEmpty(comment.get("saturation").getAsString())) {
                                    t10v.setText(comment.get("satoxygen").getAsString()); }
                              t10v.setTextColor(Color.BLACK);
                              t10v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t10v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t10v);
                                    TextView t11v = new TextView(getContext());
                                   if (!TextUtils.isEmpty(comment.get("weight").getAsString())) {

                                     t11v.setText(comment.get("weight").getAsString() + " " + comment.get("weightunit").getAsString());

                                    }
                              t11v.setTextColor(Color.BLACK);
                              t11v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t11v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t11v);
                                    TextView t12v = new TextView(getContext());
                                    if (!TextUtils.isEmpty(comment.get("height").getAsString())) {

                                      t12v.setText(comment.get("height").getAsString() + " " + comment.get("heightunit").getAsString());

                                    }
                              t12v.setTextColor(Color.BLACK);
                              t12v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t12v.setGravity(Gravity.CENTER);
                                    tbrow.addView(t12v);
//                                    TextView t14v = new TextView(getContext());
//                                    if (comment.get("painpatienty").getAsString().equals("Yes")) {
//                                      t14v.setText(comment.get("painpatienty").getAsString() + " - " + comment.get("painpatient").getAsString());
//                                    } else if (comment.get("painpatienty").getAsString().equals("No")) {
//                                      t14v.setText("No");
//                                    }
//                              t14v.setTextColor(Color.BLACK);
//                              t14v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
//                              t14v.setGravity(Gravity.CENTER);
//                              t14v.setVisibility(View.GONE);
//                                  tbrow.addView(t14v);
//
////                              TextView t15v = new TextView(getContext());
////                              t15v.setTextColor(Color.BLACK);
////                              t15v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
////                              t15v.setGravity(Gravity.CENTER);
////                              tbrow.addView(t15v);
                              stk.addView(tbrow);

                              */
                              TableRow tbrow = new TableRow(getContext());
                              TextView tv1 = new TextView(getContext());
                              tv1.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("created_at").getAsString())));
                              tv1.setTextColor(Color.BLACK);
                              tv1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              tbrow.addView(tv1);

                              TextView t2v = new TextView(getContext());


                              if (!TextUtils.isEmpty(comment.get("probeval").getAsString())) {

                                t2v.setText(comment.get("probeval").getAsString() + " " + comment.get("probetemperaturevalue").getAsString());
                              }




                              t2v.setTextColor(Color.BLACK);
                              t2v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t2v.setGravity(Gravity.CENTER);
                              tbrow.addView(t2v);


                              TextView t3v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("mantemp").getAsString())) {

                                t3v.setText(comment.get("mantemp").getAsString() + " " + comment.get("temperaturevalue").getAsString());
                              }

                              t3v.setTextColor(Color.BLACK);
                              t3v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t3v.setGravity(Gravity.CENTER);
                              tbrow.addView(t3v);




                              TextView t4v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("mantemp").getAsString())) {
                                t4v.setText(comment.get("tt").getAsString()); }
                              t4v.setTextColor(Color.BLACK);
                              t4v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t4v.setGravity(Gravity.CENTER);
                              tbrow.addView(t4v);
//////
                              TextView t5v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("sys").getAsString())) {
                                t5v.setText(comment.get("sys").getAsString() + "/" + comment.get("dias").getAsString());
                              }
                              t5v.setTextColor(Color.BLACK);
                              t5v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t5v.setGravity(Gravity.CENTER);
                              tbrow.addView(t5v);


                              TextView t6v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("sys").getAsString())) {
                                t6v.setText(comment.get("bpp").getAsString()); }
                              t6v.setTextColor(Color.BLACK);
                              t6v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t6v.setGravity(Gravity.CENTER);
                              tbrow.addView(t6v);

                              TextView t7v = new TextView(getContext());
                              t7v.setText(comment.get("pulses").getAsString());
                              t7v.setTextColor(Color.BLACK);
                              t7v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t7v.setGravity(Gravity.CENTER);
                              tbrow.addView(t7v);


                              TextView t8v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("pulses").getAsString())) {
                                t8v.setText(comment.get("pp").getAsString()); }
                              t8v.setTextColor(Color.BLACK);
                              t8v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t8v.setGravity(Gravity.CENTER);
                              tbrow.addView(t8v);


                              TextView t9v = new TextView(getContext());
                              t9v.setText(comment.get("respiration").getAsString());
                              t9v.setTextColor(Color.BLACK);
                              t9v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t9v.setGravity(Gravity.CENTER);
                              tbrow.addView(t9v);


                              TextView t10v = new TextView(getContext());
                              t10v.setText(comment.get("sat").getAsString());
                              t10v.setTextColor(Color.BLACK);
                              t10v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t10v.setGravity(Gravity.CENTER);
                              tbrow.addView(t10v);

                              TextView t11v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("sat").getAsString())) {
                                t11v.setText(comment.get("sato").getAsString()); }
                              t11v.setTextColor(Color.BLACK);
                              t11v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t11v.setGravity(Gravity.CENTER);
                              tbrow.addView(t11v);
//
                              TextView t12v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("wei").getAsString())) {

                                t12v.setText(comment.get("wei").getAsString() + " " + comment.get("weiunit").getAsString());

                              }
                              t12v.setTextColor(Color.BLACK);
                              t12v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t12v.setGravity(Gravity.CENTER);
                              tbrow.addView(t12v);
//
                              TextView t13v = new TextView(getContext());
                              if (!TextUtils.isEmpty(comment.get("hei").getAsString())) {

                                t13v.setText(comment.get("hei").getAsString() + " " + comment.get("heiunit").getAsString());

                              }
                              t13v.setTextColor(Color.BLACK);
                              t13v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                              t13v.setGravity(Gravity.CENTER);
                              tbrow.addView(t13v);



                              stk.addView(tbrow);
                              }
                   //    }

                        }
                         catch (Exception e) {
                        }

                        try {
                            int commemtsSize = resource.data.get("historydata").getAsJsonArray().size();
                            for (int i = 0; i < 10; i++) {
                                JsonObject comment = resource.data.get("historydata").getAsJsonArray().get(i).getAsJsonObject();


                                TableRow tbrow = new TableRow(getContext());

                                TextView t2v = new TextView(getContext());
                                if (!TextUtils.isEmpty(comment.get("created_at").getAsString())) {

                                    t2v.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("created_at").getAsString())));

                                }
                                t2v.setTextColor(Color.BLACK);
                                t2v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                t2v.setGravity(Gravity.CENTER);
                                tbrow.addView(t2v);
                                TextView t3v = new TextView(getContext());
                                t3v.setText(comment.get("name").getAsString());
                                t3v.setTextColor(Color.BLACK);
                                t3v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                t3v.setGravity(Gravity.CENTER);
                                tbrow.addView(t3v);
                                TextView reviewer = new TextView(getContext());
//                                 if(comment.get("read_at")==null)
//                                {
//                                  reviewer.setText("");
//                                }
//                              String str = null;
//                              if(comment.get("read_at").getAsString()==str)
//                              {
//                                reviewer.setText("");
//                              }
//                              else {
                             //   reviewer.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("read_at").getAsString())));
                            //}
                              reviewer.setText(comment.get("read_at").getAsString());
                                reviewer.setTextColor(Color.BLACK);
                                reviewer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                reviewer.setGravity(Gravity.CENTER);
                                tbrow.addView(reviewer);

                                stk1.addView(tbrow);
                                tbrow.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
//                                        //    Toast.makeText(getContext(), comment.get("id").getAsString(), Toast.LENGTH_SHORT).show();
//
//                                        getFragmentHandlingActivity().addFragmentWithBackstack(CaseDetailFragment.newInstance(
//                                                comment.get("id").getAsString(),  comment.get("case_code").getAsString()));

                                        // or do something more use full here.

                                    }
                                });

                            }
                        } catch (Exception e) {
                        }
                      try {

                        int premilinaryDiagSize = resource.data.get("preliminaydata").getAsJsonArray().size();

                        containerDiagnosis.removeAllViews();
                        if (premilinaryDiagSize == 0) {
                          txtDiagnosis.setVisibility(View.GONE);
                        } else {
                          txtDiagnosis.setVisibility(View.VISIBLE);
                          for (int i = 0; i < premilinaryDiagSize; i++) {
                            JsonObject preliminaryData = resource.data.get("preliminaydata").getAsJsonArray().get(i).getAsJsonObject();

                            View inflatedLayout = inflater.inflate(R.layout.include_diagnosis_layout,
                              containerDiagnosis, false);

                            TextView textView = inflatedLayout.findViewById(R.id.tvDiagnosis);
                            textView.setText(String.format(Locale.getDefault(), "%d. %s", i + 1,
                              preliminaryData.get("preliminay").getAsString()));

                            containerDiagnosis.addView(inflatedLayout);
                          }
                        }

                      } catch (Exception e) {

                      }

                      try {

                        int premilinaryReviewerSize = resource.data.get("preliminay_diagnosis_record").getAsJsonArray().size();

                        diagnosisByRev.removeAllViews();
                        if (premilinaryReviewerSize == 0) {
                          txtDiagnosisByRev.setVisibility(View.GONE);
                        } else {
                          txtDiagnosisByRev.setVisibility(View.VISIBLE);
                          for (int i = 0; i < premilinaryReviewerSize; i++) {
                            JsonObject preliminaryData = resource.data.get("preliminay_diagnosis_record").getAsJsonArray().get(i).getAsJsonObject();

                            View inflatedLayout = inflater.inflate(R.layout.include_diagnosis_layout,
                              diagnosisByRev, false);

                            TextView textView = inflatedLayout.findViewById(R.id.tvDiagnosis);
                            textView.setText(String.format(Locale.getDefault(), "%d. %s", i + 1,
                              preliminaryData.get("preliminay_diagnosis_record").getAsString()));

                            diagnosisByRev.addView(inflatedLayout);
                            //Layout height not increasing, FIX ITTTTTT
                          }
                        }

                      } catch (Exception e) {

                      }


                        //Checking address Key Present or not
                        try {
                            int commemtsSize = resource.data.get("comments").getAsJsonArray().size();

                            comment_container.removeAllViews();

                            for (int i = 0; i < commemtsSize; i++) {

                                View inflatedLayout = inflater.inflate(R.layout.include_comment_layout, comment_container, false);

                                JsonObject comment = resource.data.get("comments").getAsJsonArray().get(i).getAsJsonObject();

                                if (comment.get("comment").getAsString() == "") {
                                    comment_container.setVisibility(View.INVISIBLE);
                                } else {

                                    comment_container.setVisibility(View.VISIBLE);
                                    String recordId = comment.get("record_id").getAsString();
                                    String commentById = comment.get("commented_by").getAsString();
                                    String commentId = comment.get("id").getAsString();

                                    ((TextView) inflatedLayout.findViewById(R.id.txt_comment))
                                            .setText(comment.get("comment").getAsString());

                                    ((TextView) inflatedLayout.findViewById(R.id.txt_date))
                                            .setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(comment.get("created_at").getAsString())));


                                    ((TextView) inflatedLayout.findViewById(R.id.txt_name))
                                            .setText(comment.get("name").getAsString());


                                    ImageButton btnEdit = inflatedLayout.findViewById(R.id.btnEdit);

                                    if (commentById.equals(getUser().getId())) {
                                        btnEdit.setVisibility(View.VISIBLE);
                                        btnEdit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                getFragmentHandlingActivity().addDialog(Appointment1DetailFragment.this,
                                                        EditCommentDialog.newInstance(recordId, comment.get("comment").getAsString(),
                                                                commentById, commentId));
                                            }
                                        });
                                    }

                                    ImageView profileImage = (ImageView) inflatedLayout.findViewById(R.id.profile_image);
                                    ImageView commentImage = (ImageView) inflatedLayout.findViewById(R.id.commentImage);

                                    if (!comment.get("file").getAsString().isEmpty()) {
                                        commentImage.setVisibility(View.VISIBLE);

                                      Picasso.with(getContext())
                                                .load(AppModule.commentimage +
                                                        comment.get("file").getAsString())
                                                .fit()
                                                .placeholder(R.drawable.image_placeholder).into(commentImage);
                                    }

                                    url = AppModule.profileimage + comment.get("image").getAsString();

                                  Picasso.with(getContext())
                                            .load(url)
                                            .fit().centerCrop()
                                            .placeholder(R.drawable.image_placeholder).into(profileImage);

                                    inflatedLayout.setTag(comment.get("comment").getAsString().trim());
                                    comment_container.addView(inflatedLayout);


                                }
                            }


                        } catch (Exception ex) {
                        }

                        try {
                            int updateCommentSize = resource.data.get("updatedcomments").getAsJsonArray().size();

                            for (int i = 0; i < updateCommentSize; i++) {

                                View inflatedLayout = inflater.inflate(R.layout.include_update_comment_layout,
                                        comment_container, false);

                                JsonObject comment = resource.data.get("updatedcomments").getAsJsonArray().get(i).getAsJsonObject();

                                if (comment.get("update_comment").getAsString().equals("")) {
                                    inflatedLayout.setVisibility(View.GONE);
                                } else {

                                    inflatedLayout.setVisibility(View.VISIBLE);

                                    String originalCommentDate = Utils.format12HourDateTime(
                                            Utils.UTCTolocalDateTime(comment.get("commentdate").getAsString()));

                                    String originalComment = comment.get("user_comment").getAsString();

                                    ((TextView) inflatedLayout.findViewById(R.id.originalComment))
                                            .setText(Html.fromHtml(String.format(Locale.getDefault(),
                                                    "<b>Original Comment:</b>&nbsp;&nbsp;%s<br>%s", originalCommentDate, originalComment)));

                                    ((TextView) inflatedLayout.findViewById(R.id.updateComment))
                                            .setText(Html.fromHtml(String.format(Locale.getDefault(),
                                                    "<b>Edited Comment:</b><br>%s", comment.get("update_comment").getAsString())));


                                    ((TextView) inflatedLayout.findViewById(R.id.txt_date))
                                            .setText(Utils.format12HourDateTime(
                                                    Utils.UTCTolocalDateTime(comment.get("create_time").getAsString())));

                                    ((TextView) inflatedLayout.findViewById(R.id.txt_name))
                                            .setText(comment.get("user_name").getAsString());

                                    ImageView profileImage = (ImageView) inflatedLayout.findViewById(R.id.profile_image);

                                    url = AppModule.profileimage + comment.get("user_image").getAsString();

                                  Picasso.with(getContext())
                                            .load(url)
                                            .fit().centerCrop()
                                            .placeholder(R.drawable.image_placeholder).into(profileImage);

                                    inflatedLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            View jumpToView = comment_container.findViewWithTag(
                                                    comment.get("user_comment").getAsString().trim());

                                        }
                                    });

                                    comment_container.addView(inflatedLayout);

                                }
                            }

                        } catch (Exception e) {

                        }


                        try {
                            int commemtsSize = resource.data.get("radiologydata").getAsJsonArray().size();

                            comm_container.removeAllViews();

                            for (int i = 0; i < commemtsSize; i++) {

                                View inflatedLayout = inflater.inflate(R.layout.include_radio_layout, comm_container, false);

                                JsonObject comment = resource.data.get("radiologydata").getAsJsonArray().get(i).getAsJsonObject();

                                comm_container.setVisibility(View.VISIBLE);

                                ((TextView) inflatedLayout.findViewById(R.id.txt_status))
                                        .setText(comment.get("status").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.txt_date))
                                        .setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(comment.get("created_at").getAsString())));

                                ((TextView) inflatedLayout.findViewById(R.id.txt_diagnosis))
                                        .setText(comment.get("diagnosis").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.txt_preg))
                                        .setText(comment.get("pregnancy").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.textView14))

                                           .setText(comment.get("addmoreimages").getAsString());
                              ((TextView) inflatedLayout.findViewById(R.id.txt_instruction))
                                        .setText(comment.get("instructions").getAsString());


                                Button btn_report = ((Button) inflatedLayout.findViewById(R.id.textView15));
                              Button btn_pdf = ((Button) inflatedLayout.findViewById(R.id.textView16));
                                btn_report.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        img_id = comment.get("id").getAsString();
                                        //      Toast.makeText(getContext(),img_id, Toast.LENGTH_LONG).show();

                                        getFragmentHandlingActivity()
                                                .replaceFragmentWithBackstack(RadImageFragment.newInstance(img_id));

                                    }
                                });
                              btn_pdf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                  img_id = comment.get("id").getAsString();
//                                            Toast.makeText(getContext(),img_id, Toast.LENGTH_LONG).show();

                                  getFragmentHandlingActivity().addFragmentWithBackstack(MainRecordDetailFragment.newInstance(
                                    img_id, booked_as));
//Toast.makeText(getContext(),""+img_id,Toast.LENGTH_SHORT).show();

//                                        getFragmentHandlingActivity()
//                                                .replaceFragmentWithBackstack(MainRecordDetailFragment.newInstance(img_id));


                                }
                              });

                                comm_container.addView(inflatedLayout);
                            }

                        } catch (Exception ex) {
                        }

                        try {
                            int labSize = resource.data.get("labdata").getAsJsonArray().size();

                            lab_container.removeAllViews();

                            for (int i = 0; i < labSize; i++) {

                                View inflatedLayout = inflater.inflate(R.layout.include_lab_layout, lab_container, false);

                                JsonObject comment = resource.data.get("labdata").getAsJsonArray().get(i).getAsJsonObject();

                                lab_container.setVisibility(View.VISIBLE);

                                ((TextView) inflatedLayout.findViewById(R.id.txt_status))
                                        .setText(comment.get("status").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.txt_date))
                                        .setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(comment.get("created_at").getAsString())));


                                ((TextView) inflatedLayout.findViewById(R.id.txt_diagnosis))
                                        .setText(comment.get("diagnosisl").getAsString());

//                                ((TextView) inflatedLayout.findViewById(R.id.txt_preg))
//                                        .setText(comment.get("test1").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.txt_instruction))
                                        .setText(comment.get("instructionsl").getAsString());

                                ((TextView) inflatedLayout.findViewById(R.id.txt_name))
                                        .setText(comment.get("test2").getAsString());

                                Button btn_labreport = ((Button) inflatedLayout.findViewById(R.id.view_labimage));
                              Button btn_pdf = ((Button) inflatedLayout.findViewById(R.id.textView16));
                                btn_labreport.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        lab_image = comment.get("id").getAsString();
                                        //    Toast.makeText(getContext(),lab_image, Toast.LENGTH_LONG).show();

                                        getFragmentHandlingActivity()
                                                .replaceFragmentWithBackstack(LabImageFragment.newInstance(lab_image));
                                    }
                                });
                              btn_pdf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                  img_id = comment.get("id").getAsString();
                                  //      Toast.makeText(getContext(),img_id, Toast.LENGTH_LONG).show();

                                  getFragmentHandlingActivity().addFragmentWithBackstack(LabDetailFragment.newInstance(
                                    img_id, booked_as));
                                }
                              });



                              lab_container.addView(inflatedLayout);

                            }

                        } catch (Exception ex) {
                        }

                      try {
                        int presSize = resource.data.get("presdata").getAsJsonArray().size();

                        pres_container.removeAllViews();

                        for (int i = 0; i < presSize; i++) {

                          View inflatedLayout = inflater.inflate(R.layout.include_pres_layout, pres_container, false);

                          JsonObject comment = resource.data.get("presdata").getAsJsonArray().get(i).getAsJsonObject();

                          pres_container.setVisibility(View.VISIBLE);

                          ((TextView) inflatedLayout.findViewById(R.id.txt_status))
                            .setText(comment.get("status").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_date))
//                .setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(comment.get("created_at").getAsString())));
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_diagnosis))
//                .setText(comment.get("medicine").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_preg))
//                .setText(comment.get("doseNumber").getAsString() + " " + comment.get("dose").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_instruction))
//                .setText(comment.get("route").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_name))
//                .setText(comment.get("frequency").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_comment))
//                .setText(comment.get("duration").getAsString());
//
//              ((TextView) inflatedLayout.findViewById(R.id.txt_ins))
//                .setText(comment.get("com").getAsString());
                          ((TextView) inflatedLayout.findViewById(R.id.textarea))
                            .setText(comment.get("textarea").getAsString());
                          Button btn_pdf = ((Button) inflatedLayout.findViewById(R.id.view_labimage));

                          btn_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//
                              img_id = comment.get("id").getAsString();
//Toast.makeText(getContext(),comment.get("id").getAsString(), Toast.LENGTH_LONG).show();
                            //  Toast.makeText(getContext(),img_id, Toast.LENGTH_LONG).show();
                              getFragmentHandlingActivity().addFragmentWithBackstack(PrescriptionDetailFragment.newInstance(
                                img_id, booked_as));

                            }
                          });

                          pres_container.addView(inflatedLayout);



                        }

                      } catch (Exception ex) {
                        // Toast.makeText(getContext(),"dont know",Toast.LENGTH_SHORT).show();
                      }


                        try {
                            int imagesSize = resource.data.get("images").getAsJsonArray().size();

                            imageContainer.removeAllViews();


                            List<String> imageUrls = new ArrayList<String>();
                            List<String> dl = new ArrayList<String>();
                            ImageView imageView = null;
                            View imageViewItem = null;
                            for (int i = 0; i < imagesSize; i++) {
                                JsonObject images = resource.data.get("images").getAsJsonArray().get(i).getAsJsonObject();

                                imageUrls.add(AppModule.rootimage + images.get("image").getAsString());

                                Log.d("image url 2 :  ", AppModule.rootimage + images.get("image").getAsString());


                                imageViewItem = inflater.inflate(R.layout.include_image, imageContainer, false);
                                imageView = (ImageView) imageViewItem.findViewById(R.id.image);


                              Picasso.with(getContext())
                                        .load(AppModule.rootimage + images.get("image").getAsString())
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.image_placeholder).into(imageView);

                                int j = 0;

                                imageView.setTag(j++);

                                imageView.setOnClickListener(v -> {
                                    int pos = (int) v.getTag();

                                    new ImageViewer.Builder(getActivity(), imageUrls)
                                            .setStartPosition(pos)
                                            .show();

                                });

                                Space space = new Space(Appointment1DetailFragment.this.getContext());
                                space.setLayoutParams(new FlowLayout.LayoutParams(dp_10, dp_10));

                                imageContainer.addView(space);
                                imageContainer.addView(imageViewItem);


                            }


                        } catch (Exception ex) {
                        }


                    } else {
                        showError(resource.getMessage());
                       // showError("I am else wala erroe");
                    }

                }, throwable -> {
                    showError(throwable.getMessage());
                   // showError("I am throwable wala erroe");

                });


    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.send_comment)
    void sendComment() {

      if (getDiagnosisList().size() > 0) {
        Diagnosis diagnosis = new Diagnosis(getDiagnosisList(), Utils.localToUTCDateTime(Utils.getDatetime()));
        showProgress("Adding diagnosis");

        apiFactory.addReviewerDiagnose(mId, diagnosis)
          .compose(RxUtil.applySchedulers())
          .subscribe(data -> {
            if (TextUtils.isEmpty(add_comment.getText().toString())) {
              clearFields();
              getData();
            }
          }, throwable -> {
            clearFields();
            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            throwable.printStackTrace();
          });
      }

      if (TextUtils.isEmpty(add_comment.getText().toString()) || !TextUtils.isEmpty(add_comment.getText().toString())) {
        showProgress("Adding comment");

        if (commentImagesPath != null && commentImagesPath.isEmpty()) {
          apiFactory.addCommentsInCase(
            getUser().getId(),
            add_comment.getText().toString(),
            mId,Utils.localToUTCDateTime(Utils.getDatetime()))
            .compose(RxUtil.applySchedulers())
            .subscribe(data -> {
              clearFields();
              getData();

            }, throwable -> {
              clearFields();
              throwable.printStackTrace();
            });
        } else {
          attachImagesWithComment(mId, getUser().getId(), add_comment.getText().toString(),Utils.localToUTCDateTime(Utils.getDatetime()));
        }
      }
    }
//    void sendComment() {
//        if (TextUtils.isEmpty(add_comment.getText().toString()))
//            return;
//
//        showProgress("Adding comment");
//        if (commentImagesPath != null && commentImagesPath.isEmpty()) {
//            apiFactory.addCommentsInCase(
//                    getUser().getId(),
//                    add_comment.getText().toString(),
//                    mId, Utils.localToUTCDateTime(Utils.getDatetime()))
//                    .compose(RxUtil.applySchedulers())
//                    .subscribe(data -> {
//                        add_comment.setText("");
//                        commentImagesPath.clear();
//                        commentImages.removeAllViews();
//                        commentImages.setVisibility(View.GONE);
//                        hideProgress();
//                        getData();
//
//                    }, throwable -> {
//                        add_comment.setText("");
//                        commentImagesPath.clear();
//                        commentImages.removeAllViews();
//                        commentImages.setVisibility(View.GONE);
//                        hideProgress();
//                        throwable.printStackTrace();
//                    });
//    }
// else {
//           attachImagesWithComment(mId,getUser().getId(),add_comment.getText().toString());
//      }
//    }

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getContext());
    }
/*
    private void attachImagesWithComment(String recordId, String commentedBy, String comment) {
        for (int i = 0; i < commentImagesPath.size(); i++) {
           String imagePath = commentImagesPath.get(i);
          Log.e("testpath",imagePath);
        File  file = new File(imagePath);



            if (file.exists()) {
                String uploadId = UUID.randomUUID().toString();
                if (i == commentImagesPath.size() - 1) {
                    uploadReceiver.setDelegate(new SingleUploadBroadcastReceiver.Delegate() {
                        @Override
                        public void onError(Exception exception) {
                            add_comment.setText("");
                            commentImagesPath.clear();
                            commentImages.removeAllViews();
                            commentImages.setVisibility(View.GONE);
                            hideProgress();
                            showError("Something went wrong!");
                        }

                        @Override
                        public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
                            add_comment.setText("");
                            commentImagesPath.clear();
                            commentImages.removeAllViews();
                            commentImages.setVisibility(View.GONE);
                            hideProgress();
                            getData();
                        }
                    });
                    uploadReceiver.setUploadID(uploadId);
                 // Toast.makeText(getContext(),"imagePath"+imagePath,Toast.LENGTH_SHORT).show();
                }
                try {
                    new MultipartUploadRequest(getContext(), uploadId, AppModule.UPLOAD_COMMENT_IMAGES_URL)
                            .addFileToUpload(file.getPath(), "image")
                            .addParameter("commented_by", commentedBy)
                            .addParameter("record_id", recordId)
                            .addParameter("comment", comment)
//                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(1)
                            .startUpload();
                  Toast.makeText(getContext(),"file.get"+file.getPath(),Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    } */

  private void attachImagesWithComment(String recordId, String commentedBy, String comment, String created_at) {
    for (int i = 0; i < commentImagesPath.size(); i++) {
      String imagePath = commentImagesPath.get(i);
      File file = new File(imagePath);

      if (file.exists()) {
        String uploadId = UUID.randomUUID().toString();
        if (i == commentImagesPath.size() - 1) {
          uploadReceiver.setDelegate(new SingleUploadBroadcastReceiver.Delegate() {
            @Override
            public void onError(Exception exception) {
              clearFields();
              showError("Something went wrong!");
            }

            @Override
            public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
              clearFields();
              getData();
            }
          });
          uploadReceiver.setUploadID(uploadId);
        }
        try {
          new MultipartUploadRequest(getContext(), uploadId, AppModule.UPLOAD_COMMENT_IMAGES_URL)
            .addFileToUpload(file.getPath(), "image")
            .addParameter("commented_by", commentedBy)
            .addParameter("record_id", recordId)
            .addParameter("comment", comment)
            .addParameter("created_at", created_at)

//                            .setNotificationConfig(new UploadNotificationConfig())
            .setMaxRetries(1)
            .startUpload();
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

    @OnClick(R.id.send_radiology)
    public void careGiverClick() {


//        Bundle args = new Bundle();
//        args.putBoolean("Key",true);
//
//        DialogFragment newFragment = new RadiologyDialog();
//        newFragment.setArguments(args);
//        newFragment.show(getFragmentManager(), "Appointment1DetailFragment");
        getFragmentHandlingActivity().addDialog

                (this, AddRadiologyDialog.newInstance(refererid, casecode, mId));


    }

    @OnClick(R.id.send_laboratory)
    public void LaboratoryClick() {
        getFragmentHandlingActivity().addDialog(this, AddLaboratoryDialog.newInstance(refererid, case_code, mId));
     //   Toast.makeText(getContext(),"REFERRER"+refererid,Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.send_prescription)
    public void PrescriptionClick() {
        getFragmentHandlingActivity().addDialog(this, AddPrescrptionDialog.newInstance(refererid, casecode, mId));
    }

    @OnClick(R.id.expandableButton3)
    public void Expandable3Click() {
        expandableLayout3.toggle();
    }

    @OnClick(R.id.expandableButton4)
    public void Expandable4Click() {
        expandableLayout4.toggle();
    }

    @OnClick(R.id.expandableButton5)
    public void Expandable5Click() {
        expandableLayout5.toggle();

    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    @OnClick(R.id.expandableButton)
    public void ExpandableClick() {

        vital_layout.setVisibility(vital_layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

    }

    @OnClick(R.id.expandableButton2)
    public void Expandable2Click() {

        stk1.setVisibility(stk1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

    }

    @OnClick(R.id.addImageContainer)
    public void openGallery() {
        verifyStoragePermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Toast.makeText(getContext(), "Error reading Image", Toast.LENGTH_SHORT).show();
                        return;
                    }
/*
                  Uri selectedImageUri = data.getData();
                  String[] proj = { MediaStore.Images.Media.DATA };
                  CursorLoader loader = new CursorLoader(getContext(), selectedImageUri, proj, null, null, null);
                  Cursor cursor = loader.loadInBackground();
                  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                  cursor.moveToFirst();
                  String filePath = cursor.getString(column_index);
                  commentImagesPath.add(filePath);
                  cursor.close();
                  Toast.makeText(getContext(),"filepath"+filePath,Toast.LENGTH_SHORT).show();
                  */
                //  return filePath;

                 //   Uri selectedImageUri = data.getData();
                  /*  String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, proj,
                            null, null, null);
                    if (cursor.moveToFirst()) {

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                      //int column_index = 0;
                        Toast.makeText(getContext(),"column"+column_index,Toast.LENGTH_SHORT).show();
                        String filePath = cursor.getString(column_index);
//                        if (commentImages.getVisibility() == View.GONE)
//                            commentImages.setVisibility(View.VISIBLE);
                    //  filePath = filePath.substring(filePath.lastIndexOf(":")+1);
                      Toast.makeText(getContext(),"filepath"+filePath,Toast.LENGTH_SHORT).show();


                      */
//                  String filePath = "";
//                  String fileId = DocumentsContract.getDocumentId(selectedImageUri);
//                  Toast.makeText(getContext(),"fileId"+fileId,Toast.LENGTH_SHORT).show();
//                  // Split at colon, use second item in the array
//                  String id = fileId.split(":")[1];
//               //   String id = fileId ;
//                  Toast.makeText(getContext(),"id"+id,Toast.LENGTH_SHORT).show();
//                  if (id==null || !(new File(id).canRead() )) {
//                   Toast.makeText(getContext(),
//                      "Can't read your photo. Please pi1ck one in the gallery, not in other folders such as downloads. " +
//                        "Some Android versions mishandle those folders.",Toast.LENGTH_SHORT).show();
//                    return;
//                  }
//                  String[] column = {MediaStore.Images.Media.DATA};
//                  String selector = MediaStore.Images.Media._ID + "=?";
//                  Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    column, selector, new String[]{id}, null);
//                  int columnIndex = cursor.getColumnIndex(column[0]);
//                  if (cursor.moveToFirst()) {
//                    filePath = cursor.getString(columnIndex);
//                  }
//
//                  Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, null, null, null, null);
//                  cursor.moveToFirst();
//                  String image_id = cursor.getString(0);
//                  image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
//                  cursor.close();
//                  cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
//                  cursor.moveToFirst();
//                  String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                  cursor.close();


                  String filePath="";

                  Uri selectedImageUri = data.getData();
                  Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, null, null, null, null);

                  cursor.moveToFirst();
                  if(cursor!=null && cursor.moveToFirst()) {

                    String document_id = cursor.getString(0);
                    document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                    cursor.close();

                    cursor = getActivity().getContentResolver().query(
                      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                      null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    commentImagesPath.add(filePath);
                    cursor.close();

                  }


//one jo hai cursor comment addImafe
                  // two comment cursoe addI
// settings same as two but add cursor.movefirst
                 //   cursor.close();

                    addImageToComment(selectedImageUri);
                 // Toast.makeText(getContext(),"selected Image"+selectedImageUri,Toast.LENGTH_SHORT).show();
                }
        }
    }






  private void addImageToComment(Uri imageUri) {
    ImageView imageView = new ImageView(getContext());
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 300);
    lp.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(lp);

    imageView.setId(commentImageCounter++);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setImageURI(imageUri);

    commentImages.addView(imageView);
  }


    /*



    private void addImageToComment(Uri imageUri) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 300);
        lp.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(lp);

        imageView.setId(commentImageCounter++);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(imageUri);

        commentImages.addView(imageView);
    }
*/


   /* public void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
          Intent intent = new Intent();
          intent.setType("image/*");
          intent.setAction(Intent.ACTION_GET_CONTENT);
          startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

        }
    }
    */
   public void verifyStoragePermissions() {
     int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

     if (permission != PackageManager.PERMISSION_GRANTED) {

       ActivityCompat.requestPermissions(
         getActivity(),
         PERMISSIONS_STORAGE,
         REQUEST_EXTERNAL_STORAGE
       );
     } else {
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

     }
   }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

        }
    }
*/
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

  }
}
  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    containerDiagnosis = view.findViewById(R.id.containerDiagnosis);
    containerDiagnosisByRev = view.findViewById(R.id.containerDiagnosisByRev);
    diagnosisByRev = view.findViewById(R.id.diagnosisByRev);
    etDiagnosisByRev = view.findViewById(R.id.etDiagnosisByRev);
    llAddDiagnosis = view.findViewById(R.id.llAddDiagnosis);
    txtDiagnosisByRev = view.findViewById(R.id.txtDiagnosisByRev);
    txtDiagnosis = view.findViewById(R.id.txtDiagnosis);
    btnAddMore = view.findViewById(R.id.btnAddMore);
  }

  private List<String> getDiagnosisList() {
    List<String> list = new ArrayList<>();
    if (etDiagnosisByRev.getText().toString().isEmpty() && containerDiagnosisByRev.getChildCount() < 1)
      return list;

    if (!etDiagnosisByRev.getText().toString().isEmpty())
      list.add(etDiagnosisByRev.getText().toString());

    if (containerDiagnosisByRev.getChildCount() > 0) {
      for (int i = 0; i < containerDiagnosisByRev.getChildCount(); i++) {
        EditText editText = containerDiagnosisByRev.getChildAt(i).findViewById(R.id.etDiagnosisByRev);

        if (!editText.getText().toString().isEmpty())
          list.add(editText.getText().toString());
      }
    }

    return list;
  }


  private void clearFields() {
    add_comment.setText("");
    etDiagnosisByRev.setText("");
    containerDiagnosisByRev.removeAllViews();
    commentImagesPath.clear();
    commentImageFiles.clear();
    commentImages.removeAllViews();
    commentImages.setVisibility(View.GONE);
    hideProgress();
  }
}
