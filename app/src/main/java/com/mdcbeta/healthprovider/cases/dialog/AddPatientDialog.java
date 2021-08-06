package com.mdcbeta.healthprovider.cases.dialog;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.City;
import com.mdcbeta.data.remote.model.Country;
import com.mdcbeta.data.remote.model.CreateCase;
import com.mdcbeta.data.remote.model.States;
import com.mdcbeta.data.remote.model.User_Patient;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
import com.mdcbeta.healthprovider.appointment.PatientDataFragment;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.healthprovider.group.GroupFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddPatientDialog extends BaseDialogFragment {


    // AppCompatEditText edtPhoneNumber;


    @BindView(R.id.ccp)
    CountryCodePicker ccp;


    @BindView(R.id.ccp2)
    CountryCodePicker ccp2;

    @NotEmpty
    @BindView(R.id.phone)
    AppCompatEditText phone;

    @BindView(R.id.phone2)
    AppCompatEditText phone2;


    @BindView(R.id.cancel_btn)
    ImageButton cancelBtn;

    @NotEmpty
    @BindView(R.id.profile_name)
    EditText profile_name;

    // @Email
    @BindView(R.id.email)
    EditText email;

    @NotEmpty
    @BindView(R.id.first_name)
    EditText first_name;

    @NotEmpty
    @BindView(R.id.last_name)
    EditText last_name;


    // @NotEmpty
//    @BindView(R.id.phone)
//    EditText phone;


    // @NotEmpty
    @BindView(R.id.street_address)
    EditText street_address;


    @BindView(R.id.add_caregiver)
    Button addCaregiver;


    @BindView(R.id.add_dob)
    ImageButton add_dob;

    @BindView(R.id.add_dob_choose)
    TextView add_dob_choose;

    @BindView(R.id.spin_gender)
    Spinner gender;

    @BindView(R.id.spin_country)
    Spinner country;

    @BindView(R.id.spin_city)
    Spinner city;

    @BindView(R.id.spin_state)
    Spinner state;

    @BindView(R.id.scrollView)
    ScrollView scroll;

    @Inject
    ApiFactory apiFactory;

    String relation;
    String genderPosition;
    String chckG;

    String check;
    String val;
    private String shareOption;
    private String countryId;
    private String stateId;
    private String cityId;

    private List<Country> countryList;
    private List<States> stateList;
    private List<City> cityList;
    String myValue;
    String match;

    //  String str = getUser().licenses_id;
    Boolean my_value_off;
    boolean checka;
    boolean checkB;
    boolean a;
    String secondaryphone;
    // boolean varyPiable for button click atleast one time
    boolean buttonDob = false;
    boolean buttonGender = false;

    public static AddPatientDialog newInstance() {
        AddPatientDialog frag = new AddPatientDialog();
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
        //  myValue = mArgs.getString("key");
        //  my_value_off = mArgs.getBoolean("my_key");
        // checkB = mArgs.getBoolean("Key");
        a = true;
        //  phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        ccp.isValid();
        ccp.registerPhoneNumberTextView(phone);


//Toast.makeText(getContext(), "create "+my_value_off, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "create "+checkB, Toast.LENGTH_SHORT).show();

        List<String> genderData = getDataForGenderSpinner();
        CustomSpinnerAdapter<String> genderAdapter =
                new CustomSpinnerAdapter<>(getContext(), genderData);
        gender.setAdapter(genderAdapter);


        compositeDisposable.add(
                apiFactory.countries()
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {

                            ArrayAdapter<Country> adapter =
                                    new ArrayAdapter<Country>(getContext(), android.R.layout.simple_spinner_item, model.getData());
                            countryList = model.getData();
                            country.setAdapter(adapter);
                        }, throwable -> showError(throwable.getMessage())));


        compositeDisposable.add(RxAdapterView.itemSelections(country)
                .filter(ignore -> country.getAdapter() != null && country.getAdapter().getCount() > 0)
                .flatMap(integer -> {
                    Country item = (Country) country.getItemAtPosition(integer);
                    countryId = item.getId();

                    return apiFactory.states(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    ArrayAdapter<States> adapter =
                            new ArrayAdapter<States>(getContext(), android.R.layout.simple_spinner_item, model.getData());
                    state.setAdapter(adapter);

                }));


        compositeDisposable.add(RxAdapterView.itemSelections(state)
                .filter(ignore -> state.getAdapter() != null && state.getAdapter().getCount() > 0)
                .flatMap(integer -> {
                    States item = (States) state.getItemAtPosition(integer);
                    stateId = item.getId();

                    return apiFactory.cities(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    ArrayAdapter<City> adapter =
                            new ArrayAdapter<City>(getContext(), android.R.layout.simple_spinner_item, model.getData());
                    city.setAdapter(adapter);
                }));


        compositeDisposable.add(RxAdapterView.itemSelections(city)
                .filter(ignore -> city.getAdapter() != null && city.getAdapter().getCount() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pos -> {
                    City item = (City) city.getItemAtPosition(pos);
                    cityId = item.getId();

                }));
    }

    private List<String> getDataForGenderSpinner() {
        List<String> items = new ArrayList<>();
        items.add("Select gender");
        items.add("Male");
        items.add("Female");
        items.add("Others");
        return items;
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_new_patient;
    }

    @Override
    public void onValidationSucceeded() {


        showProgress("Loading");

        if (phone2.getText().toString().isEmpty()) {
            secondaryphone = "";
        } else {
            secondaryphone = ccp2.getSelectedCountryCodeWithPlus() + " " + phone2.getText().toString();
        }

        User_Patient user = new User_Patient(getUser().licenses_id, first_name.getText().toString() + last_name.getText().toString(),
                first_name.getText().toString() + " " + last_name.getText().toString(),
                first_name.getText().toString(), last_name.getText().toString(), ccp.getSelectedCountryCodeWithPlus() + " " + phone.getText().toString(),
                genderPosition, add_dob_choose.getText().toString(),
                email.getText().toString(), secondaryphone);


//int i =1;
        apiFactory.signup_patient(user)
                .compose(RxUtil.applySchedulers())
                .subscribe(objectResponse -> {
                    if (objectResponse.status) {
                        showProgress("Loading");

                        //                Toast.makeText(getContext(),"New patient registered successfully",Toast.LENGTH_SHORT).show();
                        showProgress("New Patient created successfully !");


                        hideProgress();

//         if(a==checkB)
//         {
//             getFragmentHandlingActivity().replaceFragment(CreateCaseFragment
//                     .newInstance());
//         }
//         else {
                        getFragmentHandlingActivity().replaceFragment(AppointmentFragment.newInstance());
                        //  a=false;}

//if(checka==checkB) {}
//
//    getFragmentHandlingActivity().replaceFragment(CreateCaseFragment.newInstance());
//    checka = checkB;
//
//}

//    getFragmentHandlingActivity().replaceFragment(PatientDataFragment.newInstance());
//else if(checka==false)
//{
//    getFragmentHandlingActivity().replaceFragment(PatientDataFragment.newInstance());
//    checka = false;
//}
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        fragmentTransaction.detach(AddPatientDialog.this).attach(AddPatientDialog.this).commit();


                        dismiss();
//                        dismissAllowingStateLoss();
                    } else {
                        showError(objectResponse.message);
                    }

                });
    }


    @OnClick({R.id.cancel_btn, R.id.add_caregiver, R.id.add_dob})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.add_caregiver:
                if (buttonDob == true) {
                    if (buttonGender == true && chckG != "0") {
                        validator.validate();
                    } else {
                        Toast.makeText(getContext(), "Gender is mandatory", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Date of birth is mandatory", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_dob:
                buttonDob = true;
                Utils.setBirthDate(requireParentFragment(), date -> {
                    add_dob_choose.setText(date);
                });
                break;
        }
    }

    @OnItemSelected(R.id.spin_gender)
    void onRelationSelected(Spinner spinner, int position) {
        genderPosition = Integer.toString(position);
        buttonGender = true;
        chckG = genderPosition;

        // Toast.makeText(getContext(),"gender"+chckG,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}

