package com.mdcbeta.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.ImageUploadActivity;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.databinding.ActivitySettingBinding;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.widget.Toast.LENGTH_SHORT;

public class SettingActivity extends ImageUploadActivity implements LifecycleRegistryOwner {

    private static final String TAG = "SettingActivity";
    String path;
    String type;
    private ActivitySettingBinding ly;
    private User user;
    @Inject
    ApiFactory apiFactory;
    String gender;
    static String fyear;
    static String fmonth;
    static String fdayyear;
    //String ageS;
    String url;
    File file;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private SettingViewModel settingViewModel;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       int duration=  Toast.LENGTH_LONG;
//        Toast.makeText(getApplicationContext(),"load", duration).show();

        ly = DataBindingUtil.setContentView(this,R.layout.activity_setting);

        fillUi();
        setListener();
        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        apiFactory.myusersDetails(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(resource -> {

                    hideProgress();

                    if (resource.status) {

                        JsonObject data = resource.data.get("details").getAsJsonObject();

                        url = AppModule.profileimage + data.get("image").getAsString();
                        Picasso.with(getApplicationContext())
                                .load(url)
                                .fit().centerCrop()
                                .placeholder(R.drawable.image_placeholder).into(ly.image);

//                        Picasso.get()
//                                .load(url)
//                                .fit().centerCrop()
//                                .placeholder(R.drawable.image_placeholder).into(ly.image);

                    }

             }, Throwable::printStackTrace);


    }

    public void fillUi() {
        user = getUser();
        ly.setUser(user);

//
        if (user.getGender() != null && (user.gender.contains("f") || user.getGender().contains("F"))) {
            ly.spinGender.setSelection(2);
        } else if (user.getGender() != null && (user.getGender().contains("m") || user.getGender().contains("M"))) {
            ly.spinGender.setSelection(1);
        } else {
            ly.spinGender.setSelection(0);
        }

//        if (AppPref.getInstance(this).getBoolean(AppPref.Key.IS_PATIENT_LOGIN)) {
//            ly.labelSpeciality.setVisibility(View.GONE);
//            ly.editSpeciality.setVisibility(View.GONE);
//            ly.lableDegree.setVisibility(View.GONE);
//            ly.degree.setVisibility(View.GONE);
//            ly.lableAffiliation.setVisibility(View.GONE);
//            ly.affiliation.setVisibility(View.GONE);
//            ly.lableBio.setVisibility(View.GONE);
//            ly.bio.setVisibility(View.GONE);
//
//        }


    }

    public void setListener() {
        ly.saveBtn.setOnClickListener(v -> {


            if (isValidate()) {
                updateProfile();
            }
        });


        ly.birthDate.setOnClickListener(v -> {
            //  Utils.setBirthDate(this, date -> {
            //       ly.birthDate.setText(date);

            setBirthDate(this, date -> {
                        ly.birthDate.setText(date);
                    }

            );

        });


        ly.spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    gender = "m";
                } else if (position == 2) {
                    gender = "f";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ly.image.setOnClickListener(v -> {
          isStoragePermissionGranted();
            showImageDialog();
        });

        ly.placesAutocomplete.setOnPlaceSelectedListener(
                place -> ly.placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {

                        try {
                            for (AddressComponent component : placeDetails.address_components) {
                                for (AddressComponentType type1 : component.types) {
                                    switch (type1) {
                                        case STREET_NUMBER:
                                            break;
                                        case ROUTE:
                                            break;
                                        case NEIGHBORHOOD:
                                            break;
                                        case SUBLOCALITY_LEVEL_1:
                                            break;
                                        case SUBLOCALITY:
                                            break;
                                        case LOCALITY:
                                            ly.city.setText(component.long_name);
                                            break;
                                        case ADMINISTRATIVE_AREA_LEVEL_1:
                                            ly.state.setText(component.long_name);
                                            break;
                                        case ADMINISTRATIVE_AREA_LEVEL_2:
                                            break;
                                        case COUNTRY:
                                            ly.country.setText(component.long_name);
                                            break;
                                        case POSTAL_CODE:
                                            break;
                                        case POLITICAL:
                                            break;
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                })
        );


    }


    @Override
    public void performInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public static void start(Context context) {

        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }


    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        path = list.get(0).getThumbnailPath();
        type = list.get(0).getMimeType();
        setProfilePic(list.get(0).getThumbnailPath());
    }

    public void setProfilePic(String path) {
        Picasso.with(getApplicationContext()).load(new File(path))
                .placeholder(R.drawable.profilepic)
                .into(ly.image);
    }


    @Override
    public void onError(String s) {
        showError(s);
    }


    public void updateProfile() {


        MultipartBody.Part part = null;

        if (path != null && type != null) {
            file = new File(path);
            if (file.exists()) {
                final MediaType MEDIA_TYPE = MediaType.parse(type);
                part = MultipartBody.Part.createFormData("my_field", file.getName(), RequestBody.create(MEDIA_TYPE, file));

                //   Toast.makeText(getApplicationContext(), file.getName(),Toast.LENGTH_LONG).show();


            } else {
                showError("File not exists");
            }
        }

        showProgress("Updating Profile");

        apiFactory.updateProfilePic(ly.firstName.getText().toString(), ly.lastName.getText().toString(),
                user.getId(), ly.editEmail.getText().toString(), ly.profileName.getText().toString(),
                ly.userName.getText().toString(), ly.editSpeciality.getText().toString(),
                gender, TextUtils.isEmpty(ly.editPass.getText().toString()) ? "" : ly.editPass.getText().toString(),
                ly.country.getText().toString(), ly.state.getText().toString(), ly.city.getText().toString(),
                ly.birthDate.getText().toString(), ly.affiliation.getText().toString(),
                ly.degree.getText().toString(), ly.bio.getText().toString(), ly.streetAddress.getText().toString(), part)
                .doOnNext(ignore -> {


                    apiFactory.myusersDetails(getUser().getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe(resource -> {

                                hideProgress();

                                if (resource.status) {

                                    JsonObject data = resource.data.get("details").getAsJsonObject();

                                    url = AppModule.profileimage + data.get("image").getAsString();

                                    Picasso.with(getApplicationContext())
                                            .load(url)
                                            .fit().centerCrop()
                                            .placeholder(R.drawable.image_placeholder).into(ly.image);

                                }

                            }, Throwable::printStackTrace);

                    User user = getUser();
                    user.setGender(gender);
                    user.setEmail(ly.editEmail.getText().toString());
                    user.setSpeciality(ly.editSpeciality.getText().toString());
                    user.setUsername(ly.userName.getText().toString());
                    user.setName(ly.profileName.getText().toString());
                    user.setFirstname(ly.firstName.getText().toString());
                    user.setLastname(ly.lastName.getText().toString());
                    user.setDegree(ly.degree.getText().toString());
                    user.setAffiliation(ly.affiliation.getText().toString());
                    user.setBio(ly.bio.getText().toString());
                    user.setAddress(ly.streetAddress.getText().toString());
                    user.setCity(ly.city.getText().toString());
                    user.setState(ly.state.getText().toString());
                    user.setCountry(ly.country.getText().toString());
                 //   user.setPassword(ly.editPass.getText().toString());
                    //    user.setImage(file.getName());

                    setUser(user);

                })
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(model -> {
                    if (model.status) {
                        showSuccess("Profile Updated");
                        MainHealthProviderActivity.start(SettingActivity.this);
                    } else {
                        showError(model.getMessage());
                    }

                }, Throwable::printStackTrace);


    }

    public boolean isValidate() {

        boolean isValid = true;


    /*    Calendar today = Calendar.getInstance();


        int age = today.get(Calendar.YEAR) - Integer.parseInt(fyear);

        if (today.get(Calendar.DAY_OF_YEAR) < Integer.parseInt(fdayyear)) {
            age--;
        }


        Integer ageInt = new Integer(age);
        ageS = ageInt.toString();*/


        if (!Utils.setRequiredField(ly.editEmail, ly.profileName, ly.userName)) {
            isValid = false;
        }
        if (!Utils.setValidEmail(ly.editEmail)) {
            isValid = false;
        }
        if (!Utils.setIMatchPassword(ly.editPass, ly.editRepass)) {
            isValid = false;
        }
        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Select Gender", LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return new LifecycleRegistry(this);
    }

    public static class SettingViewModel extends ViewModel {
        String id;
        final MutableLiveData<String> login = new MutableLiveData<>();

        public SettingViewModel() {

        }


    }

//    public static void setBirthDate(Activity context, Utils.MyOnDateSetListener callBack) {
//        Calendar now = Calendar.getInstance();
//        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd =
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        (view, year, monthOfYear, dayOfMonth) -> {
//
//                            fyear = String.valueOf(year);
//                            fmonth = ((monthOfYear + 1)) < 10 ? "0" + ((monthOfYear + 1)) : "" + ((monthOfYear + 1));
//                            String fday = ((dayOfMonth)) < 10 ? "0" + ((dayOfMonth)) : "" + ((dayOfMonth));
//                            fdayyear = String.valueOf(now.get(Calendar.DAY_OF_YEAR));
//
//                            callBack.onDateSet(fyear + "-" + fmonth + "-" + fday);
//
//
//                        },
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//        dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2);
//        dpd.show(context.getFragmentManager(), "Datepickerdialog");
//
//    }


    public static void setBirthDate(Activity context, Utils.MyOnDateSetListener callBack) {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd =
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        (view, year, monthOfYear, dayOfMonth) -> {

                            fyear = String.valueOf(year);
                            fmonth = ((monthOfYear + 1)) < 10 ? "0" + ((monthOfYear + 1)) : "" + ((monthOfYear + 1));
                            String fday = ((dayOfMonth)) < 10 ? "0" + ((dayOfMonth)) : "" + ((dayOfMonth));
                            fdayyear = String.valueOf(now.get(Calendar.DAY_OF_YEAR));

                            callBack.onDateSet(fyear + "-" + fmonth + "-" + fday);


                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
        dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2);
//        dpd.show(context.getFragmentManager(), "Datepickerdialog");

    }

//  @Override
//  public void onBackPressed() {
//    int count = getSupportFragmentManager().getBackStackEntryCount();
//
//    if (count == 0) {
//      new AlertDialog.Builder(this)
//        .setIcon(R.drawable.ic_dialog_alert)
//        .setTitle("You want to exit ?")
//        .setPositiveButton("Yes", (dialog, which) -> {
////     Intent intent = new Intent(this,LoginActivity.class);
////     startActivity(intent);
//         // getSupportFragmentManager().popBackStack();
//          suonBackPressed();
//
//
//        })
//        .setNegativeButton("No", null)
//        .show();
//      //additional code
//    }




//    else {
//      getSupportFragmentManager().popBackStack();
//    }
//
//  }

  public  boolean isStoragePermissionGranted() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {
        Log.v(TAG,"Permission is granted");
        return true;
      } else {

        Log.v(TAG,"Permission is revoked");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return false;
      }
    }
    else { //permission is automatically granted on sdk<23 upon installation
      Log.v(TAG,"Permission is granted");
      return true;
    }
  }
//
//  @Override
//  public void onBackPressed() {
////        new AlertDialog.Builder(this)
////                .setIcon(R.drawable.ic_dialog_alert)
////                .setTitle("You want to exit ?")
////                .setPositiveButton("Yes", (dialog, which) -> {
//    //  super.onBackPressed();
//
//
////                })
////                .setNegativeButton("No", null)
////                .show();
//
//
//
//    int count = getSupportFragmentManager().getBackStackEntryCount();
//
//    if (count == 0) {
//      new AlertDialog.Builder(this)
//        .setIcon(R.drawable.ic_dialog_alert)
//        .setTitle("You want to exit ?")
//        .setPositiveButton("Yes", (dialog, which) -> {
//          super.onBackPressed();
////
//        })
//        .setNegativeButton("No", null)
//        .show();
//      //additional code
//    }
//
//
//
//
//    else {
//      getSupportFragmentManager().popBackStack();
//    }
//
//  }
}
