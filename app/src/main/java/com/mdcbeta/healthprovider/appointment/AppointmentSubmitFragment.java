package com.mdcbeta.healthprovider.appointment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.model.CaseModel;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
//import com.mdcbeta.data.remote.request_model.AppointmentCreated;
import com.mdcbeta.data.remote.model.AppointmentCreated;
import com.mdcbeta.data.remote.request_model.CaseCreated;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.cases.MyCasesFragment;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.patient.appointment.BookAppointmentService;
import com.mdcbeta.patient.appointment.DoctorsAppointmentAdapter;
import com.mdcbeta.patient.appointment.MediaResultsAdapter;
import com.mdcbeta.patient.appointment.Step5Fragment;
import com.mdcbeta.patient.dashboard.PatientMainFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trello.rxlifecycle2.android.FragmentEvent;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.mdcbeta.base.BaseActivity.user;
import static com.mdcbeta.bluetooth.Const.TAG;

public class AppointmentSubmitFragment extends BaseFragment implements BlockingStep {

    private MediaResultsAdapter adapterM;
    private DoctorsAppointmentAdapter doctorAdapter;

    private boolean isfromcase;
    private String dotorID;
    private String date;
    private String slotId;
    private String caseID;
    private String booked_as = "referrer";

    List<ChosenImage> images_path;
    String case_code;
    String fname;
    String lname;
    String phone;
    String phone2;
    String gender;
    String y_age;
    String month_age;
    String location_id;
    String slot_id;
    String doctor_id;
    String description;
    String docName;
    String docEmail;
    String spo;
    String spo_value;
    String temp;
    String temp_unit;
    String Mtemp;
    String Mtemp_unit;
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
    String user;
    String group;
    String checkvital;

    List<Integer> groups = new ArrayList<>();
    List<Integer> users = new ArrayList<>();
    List<String> diagnosis = new ArrayList<>();

    private ImagePreviewAdapter adapter;

    @BindView(R.id.btnwithoutpaypall)
    Button btnSubmit;

    @Inject
    ApiFactory apiFactory;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static AppointmentSubmitFragment newInstance(boolean isfromcase, String doctorID, String caseID, String booked) {
        AppointmentSubmitFragment fragment = new AppointmentSubmitFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_from_case_detail", isfromcase);
        args.putString("doc_id", doctorID);
        args.putString("case_id", caseID);
        args.putString("booked_as", booked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isfromcase = getArguments().getBoolean("is_from_case_detail");
            dotorID = getArguments().getString("doc_id");
            caseID = getArguments().getString("case_id");
            booked_as = getArguments().getString("booked_as");
        }
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
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        if (AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null) {
            showError("Please select some time slot!");
        } else {
            showProgress("Loading");

            apiFactory.createAppointmentfromMycase(caseID, dotorID, getUser().getId(), slotId, getUser().getId(),
                    date, Utils.getDatetime(), UUID.randomUUID().toString(), booked_as)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(model -> {

                        if (model.status) {
                            //adapter.notifyDataSetChanged();
                            showSuccess(model.getMessage());


                            getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());


                        } else {
                            showError(model.getMessage());
                        }

                    }, throwable -> {
                        hideProgress();
                        throwable.printStackTrace();
                    });


        }

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

    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @OnClick(R.id.btnwithoutpaypall)
    public void submitClick() {


//        if (AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null) {
//            showError("Please select some time slot!");
//        } else {
//            showProgress("Loading");
//
//            getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());
//            getActivity().startService(new Intent(getActivity(), CreateAppointmentService.class));
//
//            showSuccess("Appointment created successfully");
//            hideProgress();
//
//
//        }
        //  manualTemp_txt_temperature=AppPref.getInstance(this.getContext()).getImageList(AppPref.Key.TEMPERATUREMANUAL);
        images_path = AppPref.getInstance(this.getContext()).getImageList(AppPref.Key.STEP_LIST_IMAGES);
        case_code = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PATIENT_ID);
        fname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.FIRST_NAME);
        lname = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LAST_NAME);
        phone = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PHONE);
        phone2 = AppPref.getInstance(this.getContext()).getString(AppPref.Key.PHONE2);
        gender = AppPref.getInstance(this.getContext()).getString(AppPref.Key.GENDER);
        y_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_YEARS);
        month_age = AppPref.getInstance(this.getContext()).getString(AppPref.Key.AGE_MONTHS);
        slot_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_SLOT_ID);
        doctor_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_ID);
        docName = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_NAME);
        docEmail = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DOC_EMAIL);
        description = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DISCRIPTION);
        location_id = AppPref.getInstance(this.getContext()).getString(AppPref.Key.LOCATION);
        date = AppPref.getInstance(this.getContext()).getString(AppPref.Key.STEP_DATE);
        spo = AppPref.getInstance(this.getContext()).getString(AppPref.Key.SPO2);
        spo_value = AppPref.getInstance(this.getContext()).getString(AppPref.Key.RESP_RATE_R);
        temp = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMPERATURE);
        temp_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_UNIT);
        Mtemp = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMPERATUREMANUAL);
        Mtemp_unit = AppPref.getInstance(this.getContext()).getString(AppPref.Key.TEMP_UNIT2);
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
        users = AppPref.getInstance(this.getContext()).getUserList(AppPref.Key.USER_LIST);
        groups = AppPref.getInstance(this.getContext()).getUserList(AppPref.Key.GROUP_LIST);
        //     checkvital = AppPref.getInstance(this.getContext()).getUserList(AppPref.Key.CHECK_VITAL);
        checkvital = AppPref.getInstance(this.getContext()).getString(AppPref.Key.CHECK_VITAL);
        User user = AppPref.getInstance(this.getContext()).getUser(AppPref.Key.USER_LOGIN);


        List<MultipartBody.Part> parts = new ArrayList<>();
// c by kk 1/13/2020
//
//        if (images_path != null && !images_path.isEmpty()) {
//            for (int i = 0; i < images_path.size(); i++) {
//                File file = new File(images_path.get(i).getThumbnailPath());
//                if (file.exists()) {
//                    final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
//                    parts.add(MultipartBody.Part.createFormData("my_images[]",
//                            file.getName(), RequestBody.create(MEDIA_TYPE, file)));
//
//                } else {
//
//                }
//
//            }
//        }

//      CaseModel caseModel = new CaseModel(
//        case_code, fname, lname,
//        user.gender, y_age, month_age, "1", "1", "",
//        "", "1", "null", "0", user.id, complain,
//        comments, Utils.localToUTCDateTime(Utils.getDatetime()),
//        temp, temp_unit, temp_value, systolic,
//        diastolic, bp_arm, bp_value, pulse, pulse_value,
//        res_rate, spo, spo_value, weight,
//        weight_unit, height, height_unit, pain, pain_num,doctor_id);
//
//      showProgress("Creating case...");
//
//      apiFactory.createcase(caseModel)
//        .flatMap(response -> {
//
//          String casecartedID = response.getData();
//          File file;
//          String uploadId = UUID.randomUUID().toString();
//
//
//          if (images_path != null && !images_path.isEmpty()) {
//            for (int i = 0; i < images_path.size(); i++) {
//              file = new File(images_path.get(i).getThumbnailPath());
//
//              Log.d("Image path: ", file.getPath());
//
//              if (file.exists()) {
//
//                new MultipartUploadRequest(this.getContext(), uploadId, AppModule.UPLOAD_URL)
//                  .addFileToUpload(file.getPath(), "image") //Adding file
//                  .addParameter("name", casecartedID) //Adding text parameter to the request
////                                            .setNotificationConfig(new UploadNotificationConfig())
////                                            .setMaxRetries(2)
//                  .startUpload(); //S
//                final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
//                parts.add(MultipartBody.Part.createFormData("my_images[]",
//                  file.getName(), RequestBody.create(MEDIA_TYPE, file)));
//
//
//
//              } else {
//                //  Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
//              }
//
//            }
//          }
//
//
//          return apiFactory.addAppointmentAttachments(casecartedID, parts);
//
//        })
//        .compose(RxUtil.applySchedulers())
//
//        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//        .subscribe(model -> {
//
//          if (model.status) {
//
//
//            showSuccess(model.getMessage());
//            getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());
//          } else {
//            showError(model.getMessage());
//          }
//
//        }, throwable -> {
//          hideProgress();
//          throwable.printStackTrace();
//          showError(throwable.getMessage());
//        });
        AppointmentCreated caseCreated = new AppointmentCreated(

                case_code, fname, lname, phone, phone2,
                gender, y_age, month_age, "1", location_id, "",
                "", "1", "null", "0", user.id, complain,
                comments, groups, users, Utils.localToUTCDateTime(Utils.getDatetime()),
                temp, temp_unit, Mtemp, Mtemp_unit, temp_value, systolic,
                diastolic, bp_arm, bp_value, pulse, pulse_value,
                res_rate, spo, spo_value, weight,
                weight_unit, height, height_unit, pain, pain_num, date, slot_id, booked_as, UUID.randomUUID().toString(), doctor_id, checkvital, "");

        showProgress("Creating Case...");
        //  Toast.makeText(getContext(),"yage"+y_age,Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(),"mage"+month_age,Toast.LENGTH_SHORT).show();
       // Toast.makeText(getContext(),"appoint",Toast.LENGTH_SHORT).show();
        apiFactory.createAppoint(caseCreated)
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
//                                        .setNotificationConfig(new UploadNotificationConfig())
//                                        .setMaxRetries(2)

                                        .startUpload();

                                final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
                                parts.add(MultipartBody.Part.createFormData("my_images[]",
                                        file.getName(), RequestBody.create(MEDIA_TYPE, file)));


                            } else {
                                  Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
                            }

                        }
                    }


                    return apiFactory.addAppointmentAttachments(casecartedID, parts);


                })
                .compose(RxUtil.applySchedulers())

                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
Log.e("status",""+model.status);

                        showSuccess(model.getMessage());
                        // getActivity().finishAffinity();
                        // getFragmentHandlingActivity().getCacheDir().delete();

                        getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());

                        // getActivity().onBackPressed();


                        // getActivity().startService(new Intent(getActivity(), BookAppointmentService.class));
                        //getFragmentHandlingActivity().replaceFragmentWithBackstack(HealthProviderAppointmentFragment.newInstance());


                    } else {
                        showError(model.getMessage());

                    }

                }, throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                    showError(throwable.getMessage());
                });


        //});
    }


//  btnSubmit.setEnabled(false);


//    }


}
