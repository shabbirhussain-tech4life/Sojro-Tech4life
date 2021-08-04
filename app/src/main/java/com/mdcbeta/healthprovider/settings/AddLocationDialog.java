package com.mdcbeta.healthprovider.settings;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.City;
import com.mdcbeta.data.remote.model.Country;
import com.mdcbeta.data.remote.model.States;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public class AddLocationDialog extends BaseDialogFragment  {

    @Inject
    ApiFactory apiFactory;


    private static final String TAG = "AddLocationDialog";

//    @BindView(R.id.cancel_btn)
//    ImageButton cancelBtn;

    @BindView(R.id.country_spin)
    Spinner countrySpin;
    @BindView(R.id.city_spin)
    Spinner citySpin;
    @BindView(R.id.state_spin)
    Spinner stateSpin;
    @BindView(R.id.location_edit)





    @NotEmpty
    EditText locationEdit;
    @BindView(R.id.address_edit)
    EditText addressEdit;
    @BindView(R.id.save_btn)
    Button saveBtn;


    String countryId;
    String stateId;
    String cityId;


    public static AddLocationDialog newInstance() {
        AddLocationDialog fragment = new AddLocationDialog();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();


        compositeDisposable.add(
                apiFactory.countries()
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    CustomSpinnerAdapter<Country> adapter =
                            new CustomSpinnerAdapter<>(getContext(), (List<Country>) model.getData());
                    countrySpin.setAdapter(adapter);
                }, throwable -> showError(throwable.getMessage())));


        compositeDisposable.add(RxAdapterView.itemSelections(countrySpin)
                .filter(ignore -> countrySpin.getAdapter() != null && countrySpin.getAdapter().getCount() > 0)
                .flatMap(integer -> {
                    Country item = (Country) countrySpin.getItemAtPosition(integer);
                    countryId = item.getId();

                    return apiFactory.states(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    CustomSpinnerAdapter<States> adapter =
                            new CustomSpinnerAdapter<>(getContext(), (List<States>) model.getData());
                    stateSpin.setAdapter(adapter);
                }));


        compositeDisposable.add(RxAdapterView.itemSelections(stateSpin)
                .filter(ignore -> stateSpin.getAdapter() != null && stateSpin.getAdapter().getCount() > 0)
                .flatMap(integer -> {
                    States item = (States) stateSpin.getItemAtPosition(integer);
                    stateId = item.getId();

                    return apiFactory.cities(Integer.valueOf(item.getId())).subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    CustomSpinnerAdapter<City> adapter =
                            new CustomSpinnerAdapter<>(getContext(), (List<City>) model.getData());
                    citySpin.setAdapter(adapter);
                }));


        compositeDisposable.add(RxAdapterView.itemSelections(citySpin)
                .filter(ignore -> citySpin.getAdapter() != null && citySpin.getAdapter().getCount() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pos -> {
                    City item = (City) citySpin.getItemAtPosition(pos);
                    cityId = item.getId();

                }));



//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }



    @Override
    public int getLayoutID() {
        return R.layout.dialog_add_location;
    }






    @OnClick(R.id.save_btn)
    public void onViewClicked() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        if(!TextUtils.isEmpty(countryId) && !TextUtils.isEmpty(stateId) && !TextUtils.isEmpty(cityId)){

            showProgress("Loading");

            apiFactory.userLocation(AppPref.getInstance(getContext()).getUser().getId(),
                    countryId,stateId,cityId,addressEdit.getText().toString())
                    .compose(RxUtil.applySchedulers())
                    .subscribe(model -> {
                        if(model.status) {
                            showSuccess("User location updated");
                        }else {
                            showError(model.getMessage());
                        }

                    },throwable -> showError(throwable.getMessage()));

        }else {
            showError("Empty Fields");
        }

    }


}
