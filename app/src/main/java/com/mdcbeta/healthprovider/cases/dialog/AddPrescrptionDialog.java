package com.mdcbeta.healthprovider.cases.dialog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.CaseDetailFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by MahaRani on 13/11/2018.
 */

public class AddPrescrptionDialog extends BaseDialogFragment {
// Added by kanwal khan



    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @NotEmpty
    @BindView(R.id.provisional_diagnosis)
    EditText provisional_diagnosis;

    @BindView(R.id.spin_pharmacy)
    Spinner spin_pharmacy;
    @BindView(R.id.spin_test1)
    Spinner spin_test1;
    @BindView(R.id.spin_test2)
    Spinner spin_test2;
    @BindView(R.id.spin_laboratory)
    Spinner spin_laboratory;
    @BindView(R.id.spin_frequency)
    Spinner spin_frequency;
    @BindView(R.id.spin_duration)
    Spinner spin_duration;

    @BindView(R.id.add_caregiver)
    Button addCaregiver;

    @BindView(R.id.add_images)
    Button add_images;

    @BindView(R.id.textview1)
    TextView textview1;

    @BindView(R.id.txt_comment)
    EditText txt_comment;

    @BindView(R.id.txt_others)
    EditText txt_others;

    //added by kanwal khan 22-11-19

    @BindView(R.id.details_prescription)
    LinearLayout details_prescription;

    @BindView(R.id.medi_pres)
    TextView medi_pres;

    @BindView(R.id.medi_pres_show)
    TextView medi_pres_show;


//    @BindView(R.id.dose_pres)
//    TextView dose_pres;
//
//    @BindView(R.id.dose_pres_show)
//    TextView dose_pres_show;

    @BindView(R.id.unit_pres)
    TextView unit_pres;

    @BindView(R.id.unit_pres_show)
    TextView unit_pres_show;

    @BindView(R.id.route_pres)
    TextView route_pres;

    @BindView(R.id.route_pres_show)
    TextView route_pres_show;

    @BindView(R.id.frequency_pres)
    TextView frequency_pres;

    @BindView(R.id.frequency_pres_show)
    TextView frequency_pres_show;

    @BindView(R.id.duration_pres)
    TextView duration_pres;

    @BindView(R.id.duration_pres_show)
    TextView duration_pres_show;


//    @BindView(R.id.comments_pres)
//    TextView comments_pres;
//
//   @BindView(R.id.comments_pres_show)
//    TextView comments_pres_show;



    @Inject
    ApiFactory apiFactory;

    String test1;
    String test2;
    String referlaboratory;
    String frequency;
    String duration;
    String locationId;
    String route;

    static String casecode;
    static String refererid;
    static String mId;
    int clickcount=0;
    String test="";
    String medicine="";
    String dose="";
    String image1="";
    String freq="";
    String dur="";
    String comments="";

    public static AddPrescrptionDialog newInstance(String referer_id,String case_code, String record_id) {
        AddPrescrptionDialog frag = new AddPrescrptionDialog();
        refererid = referer_id;
        casecode = case_code;
        mId = record_id;
        return frag;
    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_add_prescription;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLocation();
//comments_pres_show=(TextView)getView().findViewById(R.id.comments_pres_show);


    }

    @Override
    public void onValidationSucceeded() {
        test = "Medicine: "+test1+"\nDose: "+provisional_diagnosis.getText().toString()
                + " " +test2+"\nRoute: "+route
                +"\nFrequency: "+frequency+"\nDuration: "+duration
                +"\nComments: "+txt_comment.getText().toString();

      //  Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();

        showProgress("Loading");
        apiFactory.addPrescription(mId,refererid,casecode,test1, provisional_diagnosis.getText().toString(),
                test2,route,locationId, duration,frequency,txt_comment.getText().toString(), Utils.localToUTCDateTime(Utils.getDatetime()),test)
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {

                        showSuccess("Record added successfully");


                        dismiss();



                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));

      //  getFragmentHandlingActivity().replaceFragment(CaseDetailFragment.newInstance(mId,casecode));
      getFragmentHandlingActivity().actionBack();

    }

    @OnClick({R.id.cancel_btn, R.id.add_caregiver,R.id.add_images})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.add_caregiver:

                //  Toast.makeText(getContext(), mId + "refererid" + refererid , Toast.LENGTH_SHORT).show();
                validator.validate();
                break;
            case R.id.add_images:


                test = "Medicine: "+test1+"\nDose: "+provisional_diagnosis.getText().toString()+ " "
                        +test2+"\nRoute: "+route
                        +"\nFrequency: "+frequency+"\nDuration: "+duration+"\nComments: "+
                        txt_comment.getText().toString();

                details_prescription.setVisibility(View.VISIBLE);

                medi_pres_show.setText(test1);
                //  dose_pres_show.setText((CharSequence) provisional_diagnosis);
                unit_pres_show.setText(test2);
                route_pres_show.setText(route);
                frequency_pres_show.setText(frequency);
                duration_pres_show.setText(duration);
                //  comments_pres_show.setText(txt_comment);
                // comments_pres_show.setText((CharSequence) txt_comment);


                //    Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();

//                showProgress("Loading");
//                apiFactory.addPrescription(mId,refererid,casecode,test1, provisional_diagnosis.getText().toString(),
//                        test2,route,locationId, duration,frequency,txt_comment.getText().toString(),test,Utils.localToUTCDateTime(Utils.getDatetime()))
//                        .compose(RxUtil.applySchedulers())
//                        .subscribe(model -> {
//                            if(model.status) {
//
//                                showSuccess("Record added successfully");
//
//
//                            }else {
//                                showError(model.getMessage());
//                            }
//
//                        },throwable -> showError(throwable.getMessage()));
//
//
//                textview1.setText(textview1.getText().toString()  + test + "\n");
// comment by kanwal khan 22-11-2019
//                provisional_diagnosis.setText("");
//                txt_comment.setText("");
//                spin_pharmacy.setSelection(0);
//                spin_test1.setSelection(0);
//                spin_test2.setSelection(0);
//                spin_duration.setSelection(0);
//                spin_frequency.setSelection(0);
//                spin_laboratory.setSelection(0);
                // upto this

            /*    clickcount=clickcount+1;
                if(clickcount==1)
                {
                    test = test+"Medicine: "+test1+"\nDose: "+provisional_diagnosis.getText().toString()+ " " +test2+"\nRoute: "+route
                            +"\nFrequency: "+frequency+"\nDuration: "+duration+"\nComments: "+txt_comment.getText().toString();
                    textview1.setText(test);
                    textview1.setMovementMethod(new ScrollingMovementMethod());
                }
                else if(clickcount==2)
                {
                    test = test+"Medicine: "+test1+"\nDose: "+provisional_diagnosis.getText().toString()+ " " +test2+"\nRoute: "+route
                            +"\nFrequency: "+frequency+"\nDuration: "+duration+"\nComments: "+txt_comment.getText().toString();
                    textview1.setText(test);
                    textview1.setMovementMethod(new ScrollingMovementMethod());
                }
                else
                {
                    test = test+"Medicine: "+test1+"\nDose: "+provisional_diagnosis.getText().toString()+ " " +test2+"\nRoute: "+route
                            +"\nFrequency: "+frequency+"\nDuration: "+duration+"\nComments: "+txt_comment.getText().toString();
                    textview1.setText(test);
                }*/
                break;
        }
    }

    private  void initLocation(){
        apiFactory.pres_doctor()
                .compose(RxUtil.applySchedulers())
                .subscribe(locationResponse -> {

                    List<String> list = new ArrayList<String>();
                    list.add("Refer to ePrescription");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list.add(locationResponse.data.get(i).getName());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_pharmacy.setAdapter(spinnerArrayAdapter);
                    spin_pharmacy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position == 0) { locationId = null; return; }

                            locationId = locationResponse.data.get(position-1).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                });
    }

    @OnItemSelected(R.id.spin_test1) void onPregnancyselected(AdapterView<?> parent, int position) {
        test1 = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_test2) void onShareOption(AdapterView<?> parent,int position) {
        test2 = (String) parent.getItemAtPosition(position);

    }

    @OnItemSelected(R.id.spin_laboratory) void onImagetypeselected(AdapterView<?> parent, int position) {
        referlaboratory = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_frequency) void onfreselected(AdapterView<?> parent, int position) {
        frequency  = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_duration) void ondurselected(AdapterView<?> parent, int position) {
        duration  = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_laboratory) void onrouselected(AdapterView<?> parent, int position) {

        if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Other"))
        {

            txt_others.setVisibility(View.VISIBLE);
            route = txt_others.getText().toString();
        }
        else {
            route = (String) parent.getItemAtPosition(position);
            txt_others.setVisibility(View.GONE);
        }
    }

}
