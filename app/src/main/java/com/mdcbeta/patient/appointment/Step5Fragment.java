package com.mdcbeta.patient.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.patient.dashboard.PatientMainFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mdcbeta.util.Utils.deleteDir;

/**
 * Created by MahaRani on 15/05/2018.
 */

public class Step5Fragment extends BaseFragment implements BlockingStep {


    private DoctorsAppointmentAdapter doctorAdapter;

    private boolean isfromcase;
    private String dotorID;
    private String date;
    private String slotId;
    private String caseID;
    private String booked_as = "referrer";

//    @BindView(R.id.btnwithoutpaypall)
////    Button btnSubmit;

    @Inject
    ApiFactory apiFactory;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_step5;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static Step5Fragment newInstance(boolean isfromcase, String doctorID, String caseID, String booked) {
        Step5Fragment fragment = new Step5Fragment();
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
            // added by kanwal khan 3/15/2020
            getLoaderManager();
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

            getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());
            getActivity().startService(new Intent(getActivity(), BookAppointmentService.class));

            showSuccess("Appointment created successfully");

            hideProgress();


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

//    @OnClick(R.id.btnwithoutpaypall)
//    public void submitClick() {
//
//
//        if (AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null) {
//            showError("Please select some time slot!");
//        } else {
//            showProgress("Loading");
//
//            getFragmentHandlingActivity().replaceFragment(PatientMainFragment.newInstance());
//            getActivity().startService(new Intent(getActivity(), BookAppointmentService.class));
//
//            showSuccess("Appointment created successfully");
//            hideProgress();
//
//
//        }

 //   }


   // @Override
//    public void onStop() {
//        super.onStop();
//    }

  //  @Override
//    public void onDestroy() {
//
//        super.onDestroy();
//
//        //added by kk
////        try {
////            trimCache(getContext());
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//
//
//    }

//    public static void trimCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            if (dir != null && dir.isDirectory()) {
//                deleteDir(dir);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }


//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        // The directory is now empty so delete it
//        return dir.delete();
//    }
// added by kk 1/1/2020
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//    }
}
