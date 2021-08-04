package com.mdcbeta.healthprovider.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.model.GroupAndUser;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Age;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.dialog.AddPatientDialog;
import com.mdcbeta.healthprovider.cases.dialog.BookAnAppointmentUpdatePatient;
import com.mdcbeta.healthprovider.cases.dialog.UpdatePatient;
import com.mdcbeta.patient.appointment.MediaResultsAdapter;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import okhttp3.OkHttpClient;

public class PatientDataFragment extends BaseFragment implements BlockingStep {


    private int position;
    @BindView(R.id.spin_user)
    Spinner spin_user;

//  @BindView(R.id.searchView)
//  SearchView searchView;


    @BindView(R.id.txt_patient_id)
    EditText txtPatientId;


//  @BindView(R.id.phone)
//  EditText phone;
//  @BindView(R.id.ccp)
//  CountryCodePicker ccp;

  @NotEmpty
  @BindView(R.id.phone)
  EditText phone;



 // added by kk
//  @BindView(R.id.ccp2)
//  CountryCodePicker ccp2;

  @NotEmpty
  @BindView(R.id.phone2)
  EditText phone2;

  @BindView(R.id.txt_head_phone)
  TextView PhoneText;

  @BindView(R.id.txt_head_phone2)
  TextView PhoneText2;




    @NotEmpty
    @BindView(R.id.txt_firstname)
    EditText txtFirstname;
    @NotEmpty
    @BindView(R.id.txt_lastname)
    EditText txtLastname;
    @BindView(R.id.txt_age_years)
    EditText txtAgeYears;
    @BindView(R.id.txt_age_month)
    EditText txtAgeMonth;
    @BindView(R.id.spin_gender)
    EditText spinGender;
    @BindView(R.id.spin_location)
    Spinner spinLocation;
//  @NotEmpty
//  @BindView(R.id.phone)
//  EditText phone;

  //added by kk
  @BindView(R.id.update)
  Button UpdateProfile;
  @BindView(R.id.txt_head_patient_id)
  TextView PatientIdText;
  @BindView(R.id.txt_head_firstname)
  TextView FirstnameText;
  @BindView(R.id.txt_head_lastname)
  TextView LastnameText;
  @BindView(R.id.txt_head_age)
  TextView AgeYears;
  @BindView(R.id.location)
  TextView locationText;
  @BindView(R.id.spin_head_gender)
  TextView Sgender;


  @Inject
    ApiFactory apiFactory;

    @Inject
    OkHttpClient okHttpClient;

    @Inject
    RxBus rxBus;

    String dofbirth;
    int genderPosition;
    String PatientId;
    String locationId;
    String year;
    String month;
    int check = 0;

    private CustomSpinnerAdapter<GroupAndUser> adapter1;

    List<Integer> groups = new ArrayList<>();
    List<Integer> users = new ArrayList<>();

    private MediaResultsAdapter adapter;

    private static final String TAG = "StepFragment";



    public static PatientDataFragment newInstance() {
        PatientDataFragment fragment = new PatientDataFragment();
        return fragment;
    }




    @Override
    public int getLayoutID() {
        return R.layout.fragment_patient_data;
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

//      new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {

          initLocation();
     //   Toast.makeText(getContext(), "id"+getUser().id, Toast.LENGTH_SHORT).show();
       // Toast.makeText(getContext(),""+locationId,Toast.LENGTH_SHORT).show();
        initPatientId();

//      ccp.isValid();
//      ccp.registerPhoneNumberTextView(phone);

//      ccp2.isValid();
//      ccp2.registerPhoneNumberTextView(phone2);

//          }
//      },2000);




    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
        //handle error inside of the fragment, e.g. show error on EditText
    }




    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {


        if (TextUtils.isEmpty(txtPatientId.getText().toString()) ) {
            Toast.makeText(getContext(), "Please enter case code", Toast.LENGTH_SHORT).show();
            return;
        }



        if (TextUtils.isEmpty(txtFirstname.getText().toString()) ) {
            Toast.makeText(getContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(locationId)) {
            Toast.makeText(getContext(), "Location is not selected", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (genderPosition == 0) {
//            Toast.makeText(getContext(), "Gender is not selected", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (TextUtils.isEmpty(txtAgeYears.getText().toString()) && TextUtils.isEmpty(txtAgeMonth.getText().toString())) {
            Toast.makeText(getContext(), "Please enter age in years or months", Toast.LENGTH_SHORT).show();
            return;
        }

//        String year = TextUtils.isEmpty(txtAgeYears.getText().toString()) ? "0" : txtAgeYears.getText().toString().split(" ")[0];
//        if (Integer.valueOf(year) > 100) {
//            Toast.makeText(getContext(), "Year cannot be greater than 100", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String month = TextUtils.isEmpty(txtAgeMonth.getText().toString()) ? "0" : txtAgeMonth.getText().toString().split(" ")[0];
//        if (Integer.valueOf(month) > 11) {
//            Toast.makeText(getContext(), "Month cannot be greater than 11", Toast.LENGTH_SHORT).show();
//            return;
      //  }

      //Toast.makeText(getContext(),""+txtAgeYears.getText().toString(),Toast.LENGTH_SHORT).show();
        AppPref.getInstance(getActivity()).put(AppPref.Key.PATIENT_ID,txtPatientId.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.PHONE,phone.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.PHONE2,phone2.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.FIRST_NAME,txtFirstname.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.LAST_NAME,txtLastname.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.GENDER,spinGender.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.AGE_YEARS,year);
        AppPref.getInstance(getActivity()).put(AppPref.Key.AGE_MONTHS,txtAgeMonth.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.LOCATION,locationId);

        if (users.size()>0)
            AppPref.getInstance(getActivity()).putUserList(AppPref.Key.USER_LIST,users);
        //  Toast.makeText(getContext(), users.toString(), Toast.LENGTH_SHORT).show();
        if (groups.size()>0)
            AppPref.getInstance(getActivity()).putUserList(AppPref.Key.GROUP_LIST,groups);
        //  Toast.makeText(getContext(), groups.toString(), Toast.LENGTH_SHORT).show();

        callback.goToNextStep();

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {



    }

    @SuppressLint("CheckResult")

    private void initPatientId() {

        showProgress("Loading");

       //apiFactory.existing_id()

       apiFactory.DemoLicenseData(getUser().id)
                .compose(RxUtil.applySchedulers())
                .subscribe(locationResponse -> {

                    hideProgress();

                    List<String> list1 = new ArrayList<String>();
                    list1.add("Select existing patient");

                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list1.add(locationResponse.data.get(i).getId() + " " + locationResponse.data.get(i).getName());
                    }


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list1);


                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_user.setAdapter(spinnerArrayAdapter);









                  spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                PatientId = null;
                                return;
                            }




                            PatientId = locationResponse.data.get(position - 1).getId();
                            Log.d(TAG, "initLocation: " + PatientId);
                       //     Toast.makeText(getContext(),""+PatientId,Toast.LENGTH_SHORT).show();


                            showProgress("Loading");


UpdateProfile.setVisibility(View.VISIBLE);

                            apiFactory.getPatientData((PatientId))

                                    .compose(RxUtil.applySchedulers())
                                    .subscribe(resource -> {

                                        hideProgress();
                                      txtPatientId.setVisibility(View.VISIBLE);
                                      PatientIdText.setVisibility(View.VISIBLE);
                                      txtFirstname.setVisibility(View.VISIBLE);
                                      FirstnameText.setVisibility(View.VISIBLE);
                                      txtLastname.setVisibility(View.VISIBLE);
                                      LastnameText.setVisibility(View.VISIBLE);
                                      phone.setVisibility(View.VISIBLE);
                                      phone2.setVisibility(View.VISIBLE);
                                      PhoneText.setVisibility(View.VISIBLE);
                                      PhoneText2.setVisibility(View.VISIBLE);

                                      txtAgeYears.setVisibility(View.VISIBLE);
                                      AgeYears.setVisibility(View.VISIBLE);
                                      spinGender.setVisibility(View.VISIBLE);
                                      Sgender.setVisibility(View.VISIBLE);
                                      spinLocation.setVisibility(View.VISIBLE);
                                      locationText.setVisibility(View.VISIBLE);
                                      UpdateProfile.setVisibility(View.VISIBLE);
                                        if (resource.status) {

                                            JsonObject data = resource.data.get("details").getAsJsonObject();





                                          try {
                                                txtPatientId.setText(data.get("id").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                txtFirstname.setText(data.get("firstname").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                txtLastname.setText(data.get("lastname").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();

                                            }
                                          try {
//                                            ccp.setVisibility(View.VISIBLE);
//                                            ccp.showFlag(true);
                                           phone.setText(data.get("phone").getAsString());


                                          } catch (Exception ex) {
                                            ex.printStackTrace();
                                          }

                                          try {
//                                            ccp.setVisibility(View.VISIBLE);
//                                            ccp.showFlag(true);
                                            phone2.setText(data.get("secondphone").getAsString());


                                          } catch (Exception ex) {
                                            ex.printStackTrace();
                                          }


                                          try {
                                            spinGender.setText(data.get("gender").getAsString());
//                                            String gender = data.get("gender").getAsString();
//                                            if (gender.equals("male")) {
//                                              // spinGender.setSelection(1);
//                                              genderPosition = 1;
//                                              spinGender.setText("male");
//                                            }
//                                            else if(gender.equals("female")) {
//                                              genderPosition = 2;
//                                              spinGender.setText("female");
//                                              //  spinGender.setSelection(2);
//
//                                            }
//                                            else
//                                              spinGender.setText("");
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                dofbirth = data.get("dobirth").getAsString();
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Date birthDate = sdf.parse(dofbirth);
                                                Age age = Utils.calculateAge(birthDate);

//                                                txtAgeYears.setText(age.getYears() + " years");
//                                                txtAgeMonth.setText(age.getMonths() + " months");
                                              year = ""+age.getYears();

                                              if(age.getYears()<=2)
                                              {
if(age.getMonths()==0 || age.getMonths()==1)
{
  txtAgeYears.setText(age.getYears() + " years and " + age.getMonths() + " month");

}

                                        else{        txtAgeYears.setText(age.getYears() + " years and " + age.getMonths() + " months"); }
}
                                              else if(age.getYears()==2017)
                                              {
                                                txtAgeYears.setText("");
                                              }

                                              else {
                                                txtAgeYears.setText(age.getYears()+" years");}

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                txtAgeYears.setText("");
                                                txtAgeMonth.setText("");
                                            }




                                        } else {
                                            showError(resource.getMessage());
                                        }



                                    }, throwable -> {
                                        showError(throwable.getMessage());

                                    });

                    }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d(TAG, "initLocation: ");
                        }
                    });
//
//
              });
//




    }

    @OnClick(R.id.btn_new_patient)
    public void NewPatientClick() {
        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        //getFragmentHandlingActivity().addDialog(this, AddPatientDialog.newInstance());
      //  getFragmentHandlingActivity().addDialog(AddPatientDialog.newInstance());

        String str = getUser().licenses_id;
        int val = Integer.parseInt(str);
        int num=27;
        int tr=27;
        if(num==val)
        {
            showError("This feature is  not available for demo accounts");
        }
        else {
        Bundle args = new Bundle();
        args.putBoolean("my_key", true);
        DialogFragment newFragment = new AddPatientDialog();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "fragment_off_patient");}


    }

    private void initLocation() {
        apiFactory.locations()
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(locationResponse -> {

                    List<String> list = new ArrayList<String>();
                    list.add("Select location");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list.add(locationResponse.data.get(i).getArea());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinLocation.setAdapter(spinnerArrayAdapter);
                    spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                locationId = null;
                                return;
                            }

                            locationId = locationResponse.data.get(position - 1).getId();
                            Log.d(TAG, "initLocation: " + locationId);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d(TAG, "initLocation: ");
                        }
                    });


                });





    }

    @Override
    public void onResume() {

      super.onResume();
    }


  @OnClick(R.id.update)
  public void UpdatePatientClick() {
      Bundle args = new Bundle();

    args.putString("keys",txtPatientId.getText().toString());
    args.putString("fname",txtFirstname.getText().toString());
    args.putString("lname",txtLastname.getText().toString());
    args.putString("phone",phone.getText().toString());
    args.putString("phone2",phone2.getText().toString());
    args.putString("gender",spinGender.getText().toString());
   // args.putBoolean("my_key", false);
    DialogFragment newFragment = new BookAnAppointmentUpdatePatient();
    newFragment.setArguments(args);
    newFragment.show(getFragmentManager(), "StepFragment");
//

  }



}
