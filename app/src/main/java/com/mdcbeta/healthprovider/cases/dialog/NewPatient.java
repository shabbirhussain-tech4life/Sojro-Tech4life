package com.mdcbeta.healthprovider.cases.dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.City;
import com.mdcbeta.data.remote.model.Country;
import com.mdcbeta.data.remote.model.States;
import com.mdcbeta.data.remote.model.User_Patient;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.healthprovider.group.GroupFragment;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewPatient {


//    @BindView(R.id.cancel_btn)
//    ImageButton cancelBtn;
//
//    @NotEmpty
//    @BindView(R.id.profile_name)
//    EditText profile_name;
//
//    @Email
//    @BindView(R.id.email)
//    EditText email;
//
//    @NotEmpty
//    @BindView(R.id.first_name)
//    EditText first_name;
//
//    @NotEmpty
//    @BindView(R.id.last_name)
//    EditText last_name;
//
//    @NotEmpty
//    @BindView(R.id.street_address)
//    EditText street_address;
//
//
//    @BindView(R.id.add_caregiver)
//    Button addCaregiver;
//
//    @BindView(R.id.add_dob)
//    Button add_dob;
//
//    @BindView(R.id.spin_gender)
//    Spinner gender;
//
//    @BindView(R.id.spin_country)
//    Spinner country;
//
//    @BindView(R.id.spin_city)
//    Spinner city;
//
//    @BindView(R.id.spin_state)
//    Spinner state;
//
//    @BindView(R.id.scrollView)
//    ScrollView scroll;
//
//    @Inject
//    ApiFactory apiFactory;
//
//    String relation;
//    int genderPosition;
//
//    private String shareOption;
//    private String countryId;
//    private String stateId;
//    private String cityId;
//
//    private List<Country> countryList;
//    private List<States> stateList;
//    private List<City> cityList;

    public static AddPatientDialog newInstance() {
        AddPatientDialog frag = new AddPatientDialog();
        return frag;
    }


//    @Override
//    public void provideInjection(AppComponent appComponent) {
//        appComponent.inject(this);
//
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//        List<String> genderData = getDataForGenderSpinner();
//        CustomSpinnerAdapter<String> genderAdapter =
//                new CustomSpinnerAdapter<>(getContext(), genderData);
//        gender.setAdapter(genderAdapter);

}
//        compositeDisposable.add(
//                apiFactory.countries()
//                        .compose(RxUtil.applySchedulers())
//                        .subscribe(model -> {
//                            CustomSpinnerAdapter<Country> adapter =
//                                    new CustomSpinnerAdapter<>(getContext(), model.getData());
//                            countryList = model.getData();
//                            country.setAdapter(adapter);
//                        }, throwable -> showError(throwable.getMessage())));


//        compositeDisposable.add(RxAdapterView.itemSelections(country)
//                .filter(ignore -> country.getAdapter() != null && country.getAdapter().getCount() > 0)
//                .flatMap(integer -> {
//                    Country item = (Country) country.getItemAtPosition(integer);
//                    countryId = item.getId();
//
//                    return apiFactory.states(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(model -> {
//                    CustomSpinnerAdapter<States> adapter =
//                            new CustomSpinnerAdapter<>(getContext(), model.getData());
//                    state.setAdapter(adapter);
//                }));


//        compositeDisposable.add(RxAdapterView.itemSelections(state)
//                .filter(ignore -> state.getAdapter() != null && state.getAdapter().getCount() > 0)
//                .flatMap(integer -> {
//                    States item = (States) state.getItemAtPosition(integer);
//                    stateId = item.getId();
//
//                    return apiFactory.cities(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(model -> {
//                    CustomSpinnerAdapter<City> adapter =
//                            new CustomSpinnerAdapter<>(getContext(), model.getData());
//                    city.setAdapter(adapter);
//                }));


//        compositeDisposable.add(RxAdapterView.itemSelections(city)
//                .filter(ignore -> city.getAdapter() != null && city.getAdapter().getCount() > 0)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(pos -> {
//                    City item = (City) city.getItemAtPosition(pos);
//                    cityId = item.getId();
//
//                }));
   // }

//    private List<String> getDataForGenderSpinner() {
//        List<String> items = new ArrayList<>();
//        items.add("Select Gender");
//        items.add("Male");
//        items.add("Female");
//        return items;
//    }

//    @Override
//    public int getLayoutID() {
//        return R.layout.dialog_new_patient;
//    }

//}
//    @Override
//    public void onValidationSucceeded() {

    //  showProgress("Loading");

//
//        User_Patient user = new User_Patient( profile_name.getText().toString(),
//                first_name.getText().toString()+" "+ last_name.getText().toString(),
//
//                first_name.getText().toString(), last_name.getText().toString(), genderPosition == 1 ? "m" : "f", add_dob.getText().toString(),
//                email.getText().toString());


//        apiFactory.signup_patient(user)
//                .compose(RxUtil.applySchedulers())
//                .subscribe(objectResponse -> {
//                    if (objectResponse.status) {
//                        hideProgress();
//                        getFragmentHandlingActivity().replaceFragment(CreateCaseFragment.newInstance());
//                        dismiss();
//                    } else {
//                        showError(objectResponse.message);
//                    }
//
//                });
    //}


//    @OnClick({R.id.cancel_btn, R.id.add_caregiver, R.id.add_dob})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.cancel_btn:
//                dismiss();
//                break;
//            case R.id.add_caregiver:
//                validator.validate();
//                break;
//            case R.id.add_dob:
//                Utils.setBirthDate(getActivity(), date -> {
//                    add_dob.setText(date);
//                });
//                break;
//        }
//    }

//    @OnItemSelected(R.id.spin_gender)
//    void onRelationSelected(Spinner spinner, int position) {
//        genderPosition = position;
//    }
//}
