package com.mdcbeta.patient.appointment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.model.CaseModel;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.AppointmentDoctor;
//import com.mdcbeta.data.remote.request_model.AppointmentCreated;
//import com.mdcbeta.data.remote.request_model.CaseCreated;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
//import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
//import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.healthprovider.cases.MyCasesFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.TextWatcherAdapter;
import com.mdcbeta.util.Utils;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.mdcbeta.base.BaseActivity.user;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class Step3Fragment extends BaseFragment
        implements BlockingStep,
        DoctorsAppointmentAdapter.ItemProfileListener
{

    @BindView(R.id.doc_list)
    RecyclerView docList;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    @BindView(R.id.search_by_spec)
    EditText searchBySpec;
    @BindView(R.id.search_by_doc_name)
    EditText search_by_doc_name;
    @BindView(R.id.search_by_location)
    EditText search_by_location;
    private DoctorsAppointmentAdapter doctorAdapter;

    List<ChosenImage> images_path;
    String case_code;
    String fname;
    String lname;
    String gender;
    String y_age;
    String month_age;
    String location_id;
    String doctor_id;
    String description;
    String docName;
    String docEmail;
    String spo;
    String spo_value;
    String temp;
    String temp_unit;
    String temp_value;
    String cuff;
    String systolic;
    String diastolic;
    String bp_arm;
    String bp_value;
    String pulse;
    String pulse_value;
    String res_rate;
    String weight;
    String weight_unit;
    String height;
    String height_unit;
    String pain;
    String pain_num;
    String complain;
    String comments;
    // added by kk
    public final String CHANNEL_ID = "001";

    @Inject
    ApiFactory apiFactory;

    private static final String TAG = "Step3Fragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_step3;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initDocList();
        searchBySpec.addTextChangedListener(mTextWatcher);
        search_by_doc_name.addTextChangedListener(mNameTextWatcher);
        search_by_location.addTextChangedListener(mLocationTextWatcher);

    }
    // by kk



    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    private void initDocList() {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
//        {
//NotificationChannel notificationChannel = new NotificationChannel
//        ("001",
//        "001",
//                NotificationManager.IMPORTANCE_DEFAULT
//        );
//notificationChannel.setDescription("This is a description");
//NotificationManager notificationManager = new (NotificationManager)getSystemService
//
//
//        }
//        else {
//
//
//        }








        doctorAdapter = new DoctorsAppointmentAdapter();
        docList.setAdapter(doctorAdapter);
        docList.setHasFixedSize(true);
        docList.setItemAnimator(new DefaultItemAnimator());
        docList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        docList.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorAdapter.setItemProfileListener(this);
        doctorAdapter.setItemClickListener((doctor) -> {

//
        AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DOC_ID, doctor.id);
        AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DOC_NAME, doctor.name);
        AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DOC_EMAIL, doctor.email);
//added by Kanwal Khan 25-11-2019
//              //((PatientAppointmentFragment)getParentFragment()).stepperLayout.onTabClicked(3);
//Fragment fragment = new Fragment();
            ((AppointmentFragment) getParentFragment()).stepperLayout.onTabClicked(4);

        });

        doctorAdapter.setItemReviewerListener((doctor) -> {

            images_path = AppPref.getInstance(this.getContext()).getImageList(AppPref.Key.STEP_LIST_IMAGES);
            case_code = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PATIENT_ID);
            fname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.FIRST_NAME);
            lname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LAST_NAME);
            gender = AppPref.getInstance(this.getContext()).getString(AppPref.Key.GENDER);
            y_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_YEARS);
            month_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_MONTHS);
            doctor_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_ID);
            docName = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_NAME);
            docEmail = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_EMAIL);
            description = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DISCRIPTION);
            location_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LOCATION);
            spo = AppPref.getInstance(this.getContext()).getString(AppPref.Key.SPO2);
            spo_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.RESP_RATE_R);
            temp = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMPERATURE);
            temp_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_UNIT);
            temp_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_R);
            cuff = AppPref.getInstance(this.getContext()).getString(AppPref.Key.CUFF_VALUE);
            systolic = AppPref.getInstance(this.getContext()).getString(AppPref.Key.SYSTOLIC);
            diastolic = AppPref.getInstance(this.getContext()).getString(AppPref.Key.DIASTOLIC);
            bp_arm = AppPref.getInstance(this.getContext()).getString(AppPref.Key.BP_POS);
            bp_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.BP_R);
            pulse = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PULSE_RATE);
            pulse_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PULSE_R);
            res_rate = AppPref.getInstance(this.getContext()).getString(AppPref.Key.RESP_RATE);
            weight = AppPref.getInstance(this.getContext()).getString(AppPref.Key.WEIGHT);
            weight_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.WEIGHT_UNIT);
            height = AppPref.getInstance(this.getContext()).getString(AppPref.Key.HEIGHT);
            height_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.HEIGHT_UNIT);
            pain = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PAIN);
            pain_num = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PAIN_R);
            complain = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DISCRIPTION);

            List<MultipartBody.Part> parts = new ArrayList<>();

            CaseModel caseModel = new CaseModel(
                    case_code, fname, lname,
                    user.gender, y_age, month_age, "1", location_id, "",
                    "", "1", "null", "0", user.id, complain,
                    comments, Utils.localToUTCDateTime(Utils.getDatetime()),
                    temp, temp_unit, temp_value, systolic,
                    diastolic, bp_arm, bp_value, pulse, pulse_value,
                    res_rate, spo, spo_value, weight,
                    weight_unit, height, height_unit, pain, pain_num,doctor.id);

            showProgress("Creating case...");
         //   Toast.makeText(getContext(),"step3 not running",Toast.LENGTH_SHORT).show();
            apiFactory.createcase(caseModel)
                    .flatMap(response -> {

                        String casecartedID = response.getData();
                        File file;
                        String uploadId = UUID.randomUUID().toString();


                        if (images_path != null && !images_path.isEmpty()) {
                            for (int i = 0; i < images_path.size(); i++) {
                                file = new File(images_path.get(i).getThumbnailPath());

                                Log.d("Image path: ", file.getPath());

                                if (file.exists()) {

                                    new MultipartUploadRequest(this.getContext(), uploadId, AppModule.UPLOAD_URL)
                                            .addFileToUpload(file.getPath(), "image") //Adding file
                                            .addParameter("name", casecartedID) //Adding text parameter to the request
//                                            .setNotificationConfig(new UploadNotificationConfig())
//                                            .setMaxRetries(2)
                                            .startUpload(); //S
                                    final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
                                    parts.add(MultipartBody.Part.createFormData("my_images[]",
                                            file.getName(), RequestBody.create(MEDIA_TYPE, file)));



                                } else {
                                    //  Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
                                }

                            }
                        }


                        return apiFactory.addAppointmentAttachments(casecartedID, parts);

                    })
                    .compose(RxUtil.applySchedulers())

                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(model -> {

                        if (model.status) {


                            showSuccess(model.getMessage());
                            getFragmentHandlingActivity().replaceFragment(MyCasesFragment.newInstance());
                        } else {
                            showError(model.getMessage());
                        }

                    }, throwable -> {
                        hideProgress();
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    });



        });
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {


//        if (AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null) {
//         showError("Please select some time slot!");}
//         else {
        callback.goToNextStep();

    //
        //onDestroy();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        showProgress("Loading");
        apiFactory.getUserforAppoint(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.PAUSE))
                .subscribe(model -> {
                    hideProgress();
                    doctorAdapter.swap((List<AppointmentDoctor>) model.getData());

                }, t -> showError(t.getMessage()));


    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    private final TextWatcher mTextWatcher = new TextWatcherAdapter() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            doctorAdapter.setQuery(s != null ? s.toString() : null);
        }
    };

    private final TextWatcher mNameTextWatcher = new TextWatcherAdapter() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            doctorAdapter.setNameQuery(s != null ? s.toString() : null);
        }
    };

    private final TextWatcher mLocationTextWatcher = new TextWatcherAdapter() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            doctorAdapter.setLocationQuery(s != null ? s.toString() : null);
        }
    };

    @Override
    public void clickItem(AppointmentDoctor doctor) {
        ViewDoctorProfileDialog fragment = ViewDoctorProfileDialog.newInstance();
        fragment.setDoctorData(doctor);
        getFragmentHandlingActivity().addDialog(fragment);
    }

//    @Override
//    public void clickReviewer(AppointmentDoctor doctor) {
//
//        images_path = AppPref.getInstance(this.getContext()).getImageList(AppPref.Key.STEP_LIST_IMAGES);
//        case_code = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PATIENT_ID);
//        fname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.FIRST_NAME);
//        lname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LAST_NAME);
//        gender = AppPref.getInstance(this.getContext()).getString(AppPref.Key.GENDER);
//        y_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_YEARS);
//        month_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_MONTHS);
//        doctor_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_ID);
//        docName = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_NAME);
//        docEmail = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_EMAIL);
//        description = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DISCRIPTION);
//        location_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LOCATION);
//        spo = AppPref.getInstance(this.getContext()).getString(AppPref.Key.SPO2);
//        spo_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.RESP_RATE_R);
//        temp = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMPERATURE);
//        temp_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_UNIT);
//        temp_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_R);
//        cuff = AppPref.getInstance(this.getContext()).getString(AppPref.Key.CUFF_VALUE);
//        systolic = AppPref.getInstance(this.getContext()).getString(AppPref.Key.SYSTOLIC);
//        diastolic = AppPref.getInstance(this.getContext()).getString(AppPref.Key.DIASTOLIC);
//        bp_arm = AppPref.getInstance(this.getContext()).getString(AppPref.Key.BP_POS);
//        bp_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.BP_R);
//        pulse = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PULSE_RATE);
//        pulse_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PULSE_R);
//        res_rate = AppPref.getInstance(this.getContext()).getString(AppPref.Key.RESP_RATE);
//        weight = AppPref.getInstance(this.getContext()).getString(AppPref.Key.WEIGHT);
//        weight_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.WEIGHT_UNIT);
//        height = AppPref.getInstance(this.getContext()).getString(AppPref.Key.HEIGHT);
//        height_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.HEIGHT_UNIT);
//        pain = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PAIN);
//        pain_num = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PAIN_R);
//        complain = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DISCRIPTION);
//
//        List<MultipartBody.Part> parts = new ArrayList<>();
//
//        CaseModel caseModel = new CaseModel(
//                case_code, fname, lname,
//                user.gender, y_age, month_age, "1", "1", "",
//                "", "1", "null", "0", user.id, complain,
//                comments, Utils.localToUTCDateTime(Utils.getDatetime()),
//                temp, temp_unit, temp_value, systolic,
//                diastolic, bp_arm, bp_value, pulse, pulse_value,
//                res_rate, spo, spo_value, weight,
//                weight_unit, height, height_unit, pain, pain_num,doctor_id);
//
//        showProgress("Creating case...");
//
//        apiFactory.createcase(caseModel)
//                .flatMap(response -> {
//
//                    String casecartedID = response.getData();
//                    File file;
//                    String uploadId = UUID.randomUUID().toString();
//
//
//                    if (images_path != null && !images_path.isEmpty()) {
//                        for (int i = 0; i < images_path.size(); i++) {
//                            file = new File(images_path.get(i).getThumbnailPath());
//
//                            Log.d("Image path: ", file.getPath());
//
//                            if (file.exists()) {
//
//                                new MultipartUploadRequest(this.getContext(), uploadId, AppModule.UPLOAD_URL)
//                                        .addFileToUpload(file.getPath(), "image") //Adding file
//                                        .addParameter("name", casecartedID) //Adding text parameter to the request
//                                        .setNotificationConfig(new UploadNotificationConfig())
//                                        .setMaxRetries(2)
//                                        .startUpload(); //S
//                                final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
//                                parts.add(MultipartBody.Part.createFormData("my_images[]",
//                                        file.getName(), RequestBody.create(MEDIA_TYPE, file)));
//
//
//
//                            } else {
//                                //  Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
//                            }
//
//                        }
//                    }
//
//
//                    return apiFactory.addAppointmentAttachments(casecartedID, parts);
//
//                })
//                .compose(RxUtil.applySchedulers())
//
//                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(model -> {
//
//                    if (model.status) {
//
//
//                        showSuccess(model.getMessage());
//                        getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());
//                    } else {
//                        showError(model.getMessage());
//                    }
//
//                }, throwable -> {
//                    hideProgress();
//                    throwable.printStackTrace();
//                    showError(throwable.getMessage());
//                });
//
//    }



}
