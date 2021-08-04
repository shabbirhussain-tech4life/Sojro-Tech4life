package com.mdcbeta.healthprovider.cases.dialog;

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

public class AddLaboratoryDialog extends BaseDialogFragment {


    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @NotEmpty
    @BindView(R.id.provisional_diagnosis)
    EditText provisional_diagnosis;

    @BindView(R.id.spin_laboratory)
    Spinner spin_laboratory;
    @BindView(R.id.spin_test1)
    Spinner spin_test1;
    @BindView(R.id.spin_test2)
    Spinner spin_test2;

    @BindView(R.id.txtinstruction)
    EditText txtinstruction;

    @BindView(R.id.add_caregiver)
    Button addCaregiver;

    @BindView(R.id.add_images)
    Button add_images;

    @BindView(R.id.textview1)
    TextView textview1;

    @Inject
    ApiFactory apiFactory;

    String test1;

    String test2;

    String referlaboratory;
    String locationId;

    static String casecode;
    static String refererid;
    static String mId;
    //added by kk 8/10/2020
  static String textView;
    int clickcount=0;
    String test="";

    boolean clicked=false;

    public static AddLaboratoryDialog newInstance(String referer_id,String case_code, String record_id) {
        AddLaboratoryDialog frag = new AddLaboratoryDialog();
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
        return R.layout.dialog_add_laboratory;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLocation();

    }

    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");
        apiFactory.addLaboratory(mId,refererid,casecode, provisional_diagnosis.getText().toString(),
          textview1.getText().toString(),txtinstruction.getText().toString(),locationId,"pending",
          Utils.localToUTCDateTime(Utils.getDatetime()))
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {
//Toast.makeText(getContext(),"case_code"+casecode ,Toast.LENGTH_SHORT).show();
                        showSuccess("Record added successfully");
                        dismiss();

                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));
getFragmentHandlingActivity().actionBack();
      //  getFragmentHandlingActivity().replaceFragment(CaseDetailFragment.newInstance(mId,casecode));

    }

    @OnClick({R.id.cancel_btn, R.id.add_caregiver,R.id.add_images})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.add_caregiver:

                validator.validate();
                //                else
//                {
//                    Toast.makeText(getContext(),"Please first click add to images", Toast.LENGTH_LONG).show();
//                }

                break;
            case R.id.add_images:

//                showProgress("Loading");
//                apiFactory.addLaboratory(mId,refererid,casecode, provisional_diagnosis.getText().toString(),
//                        test1,test,  txtinstruction.getText().toString(),locationId,"pending",Utils.localToUTCDateTime(Utils.getDatetime()))
//                        .compose(RxUtil.applySchedulers())
//                        .subscribe(model -> {
//                            if(model.status) {
//
//                                showSuccess("Record added successfully");
//
//                            }else {
//                                showError(model.getMessage());
//                            }
//
//                        },throwable -> showError(throwable.getMessage()));


//                txtinstruction.setText("");
//                provisional_diagnosis.setText("");
//                spin_laboratory.setSelection(0);
//                spin_test1.setSelection(0);
//                spin_test2.setSelection(0);
                test1 = "Test #1: "+test1 ;
                test = "Test #2: "+test2 ;

                textview1.setText(textview1.getText().toString()  + test1 + "\n" + test + "\n");

               /* clicked=true;
                clickcount=clickcount+1;
                if(clickcount==1)
                {
                    test = "Additional tests: "+test2 ;
                    textview1.setText(test);

                }
                else if(clickcount==2)
                {
                    test = test+"\nAdditional tests: "+test2;
                    textview1.setText(test);

                }
                else
                {
                    test = test+"\nAdditional tests: "+test2;
                    textview1.setText(test);
                }*/
                break;
        }
    }

    private  void initLocation(){
        apiFactory.lab_doctor()
                .compose(RxUtil.applySchedulers())
                .subscribe(locationResponse -> {

                    List<String> list = new ArrayList<String>();
                    list.add("Refer to Laboratory");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list.add(locationResponse.data.get(i).getUsername());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_laboratory.setAdapter(spinnerArrayAdapter);
                    spin_laboratory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


}



