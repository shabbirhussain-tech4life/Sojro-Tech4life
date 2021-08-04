package com.mdcbeta.healthprovider.cases.dialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;
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

public class AddRadiologyDialog extends BaseDialogFragment {

    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @NotEmpty
    @BindView(R.id.provisional_diagnosis)
    EditText provisional_diagnosis;

    @BindView(R.id.image_type)
    EditText image_type;

    @BindView(R.id.spin_test2)
    Spinner spin_test2;

    @BindView(R.id.spin_laboratory)
    Spinner spin_laboratory;


    @BindView(R.id.spin_test1)
    Spinner spin_test1;

    @BindView(R.id.txtinstruction)
    EditText txtinstruction;

    @BindView(R.id.add_caregiver)
    Button addCaregiver;

    @BindView(R.id.add_images)
    Button add_images;

    @BindView(R.id.textview1)
    TextView textview1;



    private ArrayAdapter<String> adapter;

    @Inject
    ApiFactory apiFactory;

    String test1;

    String test2;

    String referlaboratory;
    String locationId;

    static String casecode;
    static String refererid;
    static String mId;
    String image1="";
    String type1="";

    int clickcount=0;


    public static AddRadiologyDialog newInstance(String referer_id,String case_code, String record_id) {
        AddRadiologyDialog frag = new AddRadiologyDialog();
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
        return R.layout.dialog_add_radiology;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Spinner spinner = (Spinner)findViewById(R.id.spin_laboratory);
        initLocation();


    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addRadiology(mId,refererid,casecode,
                provisional_diagnosis.getText().toString(),test1, image_type.getText().toString(),
                referlaboratory,txtinstruction.getText().toString()
                ,locationId,  image1+type1,
                Utils.localToUTCDateTime(Utils.getDatetime()))
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



      // getFragmentHandlingActivity().replaceFragment(Appointment1DetailFragment.newInstance(mId,casecode));

getFragmentHandlingActivity().actionBack();
//getFragmentHandlingActivity().onBackPressed();
      //  getFragmentManager().popBackStack();


    }

    @OnClick({R.id.cancel_btn, R.id.add_caregiver,R.id.add_images})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.add_caregiver:

                validator.validate();

                break;
            case R.id.add_images:

                apiFactory.addRadiology(mId,refererid,casecode, provisional_diagnosis.getText().toString(),
                        test1,image_type.getText().toString(),
                        referlaboratory,txtinstruction.getText().toString(),locationId,
                        image1+type1,Utils.localToUTCDateTime(Utils.getDatetime()))
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {
                            if(model.status) {

                                showSuccess("Record added successfully");

                            }else {
                                showError(model.getMessage());
                            }

                        },throwable -> showError(throwable.getMessage()));

                image1 = "Image: "+image_type.getText().toString()+"\nType of image: "+referlaboratory ;
                textview1.setText(textview1.getText().toString()  + image1 + "\n");
                //provisional_diagnosis.setText("");
                txtinstruction.setText("");
                image_type.setText("");
                // spin_test1.setSelection(-1);
                // Spin_test 1 is type of pregnancy
                //spin_test2.setSelection(0);
                // Spin_test 2 is a  refer to radiology
                // comment by kanwal khan 22-11-1

                // spin_laboratory.setSelection(0,true);

                break;
        }
    }

    private  void initLocation(){
        apiFactory.rad_doctor()
                .compose(RxUtil.applySchedulers())
                .subscribe(locationResponse -> {

                    List<String> list = new ArrayList<String>();
                    list.add("Refer to radiology");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list.add(locationResponse.data.get(i).getName());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_test2.setAdapter(spinnerArrayAdapter);
                    spin_test2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @OnItemSelected(R.id.spin_laboratory) void onImagetypeselected(AdapterView<?> parent, int position) {
        referlaboratory = (String) parent.getItemAtPosition(position);
    }

    @OnItemSelected(R.id.spin_test1) void onPregselected(AdapterView<?> parent, int position) {
        test1 = (String) parent.getItemAtPosition(position);
    }

}
