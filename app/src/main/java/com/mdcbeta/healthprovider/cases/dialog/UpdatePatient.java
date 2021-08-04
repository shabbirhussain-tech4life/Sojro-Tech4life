package com.mdcbeta.healthprovider.cases.dialog;
// created by kanwal khan 7/27/2020
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Patient_Profile_Update;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class UpdatePatient extends BaseDialogFragment {

  @BindView(R.id.ccp)
  CountryCodePicker ccp;


  @BindView(R.id.phone)
  AppCompatEditText phone;


  @BindView(R.id.ccp2)
  CountryCodePicker ccp2;


  @BindView(R.id.phone2)
  AppCompatEditText phone2;


  @BindView(R.id.cancel_btn)
  ImageButton cancelBtn;

  @BindView(R.id.email)
  EditText email;

  @NotEmpty
  @BindView(R.id.first_name)
  EditText first_name;

  @NotEmpty
  @BindView(R.id.last_name)
  EditText last_name;


  // @NotEmpty
//  @BindView(R.id.phone)
//  EditText phone;



  @BindView(R.id.spin_gender)
  Spinner gender;

  String Patientid="";

  @Inject
  ApiFactory apiFactory;

  int genderPosition;

  Boolean my_value_off;
  boolean checka;
  boolean checkB;
  boolean a;

  public static UpdatePatient newInstance() {
    UpdatePatient frag = new UpdatePatient();
    return frag;


  }

  @Override
  public void provideInjection(AppComponent appComponent) {
    appComponent.inject(this);

  }
  @Override
  public void onStart() {
    super.onStart();

    Bundle mArgs = getArguments();
    Patientid = mArgs.getString("keys");

   // my_value_off = mArgs.getBoolean("my_key");
    checkB = mArgs.getBoolean("Key");
    a=true;
    String fname= mArgs.getString("fname");
    first_name.setText(fname);
    last_name.setText(mArgs.getString("lname"));
phone.setText(mArgs.getString("phone"));
    ccp.isValid();
    ccp.registerPhoneNumberTextView(phone);
    phone2.setText(mArgs.getString("phone2"));
    ccp2.isValid();
    ccp2.registerPhoneNumberTextView(phone2);
   // Toast.makeText(getContext(),""+Patientid,Toast.LENGTH_SHORT).show();
    List<String> genderData = getDataForGenderSpinner();
    CustomSpinnerAdapter<String> genderAdapter =
      new CustomSpinnerAdapter<>(getContext(), genderData);
    gender.setAdapter(genderAdapter);
   // phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
  }

  @Override
  public int getLayoutID() {
    return R.layout.fragment_update_patient_dialog;
  }

  @Override
  public void onValidationSucceeded() {





    showProgress("Loading");


    Patient_Profile_Update patient_profile_update = new Patient_Profile_Update
      (Patientid,
      first_name.getText().toString()+" "+ last_name.getText().toString(),
        first_name.getText().toString()+last_name.getText().toString(),
        first_name.getText().toString(),last_name.getText().toString(),phone.getText().toString(),
      genderPosition == 1 ? "male" : "female",
      email.getText().toString(),phone2.getText().toString());



    apiFactory.updatePatientProfile(patient_profile_update)
      .compose(RxUtil.applySchedulers())
      .subscribe(objectResponse -> {
        if (objectResponse.status) {
          showProgress("Loading");

          //                Toast.makeText(getContext(),"New patient registered successfully",Toast.LENGTH_SHORT).show();
          showProgress("Patient profile updated successfully !");


          hideProgress();



          if(a==checkB)
          {
            getFragmentHandlingActivity().replaceFragment(CreateCaseFragment
              .newInstance());
          }
          else {
            Log.e("khan","kanwal");
            getFragmentHandlingActivity().replaceFragment(AppointmentFragment.newInstance());


            a=false;}

         // getFragmentHandlingActivity().replaceFragment(PatientDataFragment.newInstance());

          dismiss();

        } else {
          showError(objectResponse.message);
        }

      });


  }

  @Override
  public void onResume() {
    super.onResume();
  }


  private List<String> getDataForGenderSpinner() {
    List<String> items = new ArrayList<>();
    items.add("Select Gender");
    items.add("Male");
    items.add("Female");
    return items;
  }



  @OnItemSelected(R.id.spin_gender)
  void onRelationSelected(Spinner spinner, int position) {
    genderPosition = position;
  }



  @OnClick({R.id.cancel_btn, R.id.add_caregiver})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.cancel_btn:
        dismiss();
        break;
      case R.id.add_caregiver:
        validator.validate();
        break;

    }
  }

}
