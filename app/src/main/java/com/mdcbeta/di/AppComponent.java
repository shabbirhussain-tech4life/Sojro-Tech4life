package com.mdcbeta.di;

import com.google.gson.Gson;

import android.app.Application;
import android.content.SharedPreferences;


import com.mdcbeta.authenticate.AuthenticationDialog;
import com.mdcbeta.authenticate.DialogActivity;
import com.mdcbeta.authenticate.ForgotPassword;
import com.mdcbeta.authenticate.TermsActivity;
import com.mdcbeta.healthprovider.HealthProviderViewModel;
import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
import com.mdcbeta.healthprovider.appointment.AppointmentSubmitFragment;
import com.mdcbeta.healthprovider.appointment.ImageFragment;
import com.mdcbeta.healthprovider.appointment.PatientDataFragment;
import com.mdcbeta.healthprovider.appointment.VitalSignFragment;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;
import com.mdcbeta.healthprovider.cases.DetailAppointmentFragment;
import com.mdcbeta.healthprovider.cases.LabImageFragment;
import com.mdcbeta.healthprovider.cases.OfflineCasesFragment;
import com.mdcbeta.healthprovider.cases.RadImageFragment;
import com.mdcbeta.healthprovider.cases.dialog.AddLaboratoryDialog;
import com.mdcbeta.healthprovider.cases.dialog.AddPatientDialog;
import com.mdcbeta.healthprovider.cases.dialog.AddPrescrptionDialog;
import com.mdcbeta.healthprovider.cases.dialog.AddRadiologyDialog;
import com.mdcbeta.healthprovider.cases.dialog.BookAnAppointmentUpdatePatient;
import com.mdcbeta.healthprovider.cases.dialog.EditCommentDialog;
import com.mdcbeta.healthprovider.cases.dialog.NewPatient;
import com.mdcbeta.healthprovider.cases.dialog.UpdatePatient;
import com.mdcbeta.healthprovider.group.GroupDetailFragment;
import com.mdcbeta.healthprovider.patientmonitor.PatientMonitorFragment;
import com.mdcbeta.healthprovider.settings.InviteMemberActivity;
import com.mdcbeta.jitsi.SplitFragment;
import com.mdcbeta.jitsi.WelcomeActivity;
import com.mdcbeta.patient.PatientViewModel;
import com.mdcbeta.patient.PersonalCare.BpDetailFragment;
import com.mdcbeta.patient.PersonalCare.DialogBox.BMIDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.BloodPressureDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.BpUpdateDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.CholesterolDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.CholesterolUpdateDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.DietDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.ExerciseDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.GlucoseDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.GlucoseUpdateDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.LaboratoryDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.OutpatientDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.RadiologyDialog;
import com.mdcbeta.patient.PersonalCare.DietDetailFragment;
import com.mdcbeta.patient.PersonalCare.FragmentBloodPressure;
import com.mdcbeta.patient.PersonalCare.FragmentBloodPressureGraph;
import com.mdcbeta.patient.PersonalCare.FragmentCholesterol;
import com.mdcbeta.patient.PersonalCare.FragmentDiet;
import com.mdcbeta.patient.PersonalCare.FragmentDietList;
import com.mdcbeta.patient.PersonalCare.FragmentExercise;
import com.mdcbeta.patient.PersonalCare.FragmentGlucose;
import com.mdcbeta.patient.PersonalCare.FragmentLaboratory;
import com.mdcbeta.patient.PersonalCare.FragmentRadiology;
import com.mdcbeta.patient.PersonalCare.GlucoseDetailFragment;
import com.mdcbeta.patient.PersonalCare.HealthCareMonitoringFragment;
import com.mdcbeta.patient.PersonalCare.HealthCareMonitoringGraphFragment;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.BloodPressureFragment;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.CholesterolDetailFragment;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.CholesterolFragment;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.GlucoseFragment;
import com.mdcbeta.patient.PersonalCare.HealthMonitoring.HealthMonitoringMain;
import com.mdcbeta.patient.PersonalCare.LifeStyleFragment;
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.BMIDetailFragment;
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.BMIMainFragment;
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.DietMainFragment;
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.MainLifeStyleFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.LaboratoryDetailFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.LaboratoryMainFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.MainMedicalRecord;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.OutpatientDetailFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.OutpatientMainFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecords.RadiologyMainFragment;
import com.mdcbeta.patient.PersonalCare.MedicalRecordsFragment;
import com.mdcbeta.patient.PersonalCare.PersonalCareFragment;
import com.mdcbeta.patient.PersonalCare.Personalcare;
import com.mdcbeta.patient.PersonalCare.RecordDetailFragment;
import com.mdcbeta.patient.appointment.Step5Fragment;
import com.mdcbeta.patient.appointment.Step6Fragment;
import com.mdcbeta.patient.appointment.ViewDoctorProfileDialog;
import com.mdcbeta.setting.SettingActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.healthprovider.cases.CaseDetailFragment;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.healthprovider.cases.MyCasesFragment;
import com.mdcbeta.healthprovider.group.GroupFragment;
import com.mdcbeta.healthprovider.group.NewGroupDailog;
import com.mdcbeta.healthprovider.schedule.ScheduleFragment;
import com.mdcbeta.healthprovider.settings.AddLocationDialog;
import com.mdcbeta.patient.appointment.BookAppointmentService;
import com.mdcbeta.patient.appointment.PatientAppointmentFragment;
import com.mdcbeta.patient.appointment.Step3Fragment;
import com.mdcbeta.patient.appointment.Step4Fragment;
import com.mdcbeta.patient.appointment.StepFragment;
import com.mdcbeta.patient.caregiver.AddCareGiverDailog;
import com.mdcbeta.patient.caregiver.CareGiverFragment;
import com.mdcbeta.patient.dashboard.AppointmentDetailFragment;
import com.mdcbeta.patient.dashboard.PatientMainFragment;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.authenticate.SignUpActivity;
import com.mdcbeta.supportservice.Pharmacy.MainPrescriptionFragment;
import com.mdcbeta.supportservice.Pharmacy.PharmacySpinnerDialog;
import com.mdcbeta.supportservice.Pharmacy.PrescriptionDetailFragment;
import com.mdcbeta.supportservice.laboratory.LabDetailFragment;
import com.mdcbeta.supportservice.laboratory.LabImaageUpload;
import com.mdcbeta.supportservice.laboratory.LabSpinnerDialog;
import com.mdcbeta.supportservice.laboratory.LaboratoryImageFragment;
import com.mdcbeta.supportservice.laboratory.MainLaboratoryFragment;
import com.mdcbeta.supportservice.radiology.AddRadiologySpinnerDialog;
import com.mdcbeta.supportservice.radiology.MainRadiologyFragment;
import com.mdcbeta.supportservice.radiology.MainRecordDetailFragment;
import com.mdcbeta.supportservice.radiology.RadiologyImageFragment;
import com.mdcbeta.supportservice.radiology.RadiologyImgeUpload;
import com.mdcbeta.widget.MembersView;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Shakil Karim on 4/9/17.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainHealthProviderActivity activity);
    void inject(LoginActivity activity);
    void inject(SignUpActivity activity);
    void inject(StepFragment stepFragment);
    void inject(Step4Fragment stepFragment);
    void inject(Step3Fragment stepFragment);
    void inject(Step5Fragment step5Fragment);
    void inject(Step6Fragment step6Fragment);
    void inject(OfflineCasesFragment offlineCasesFragment);
    void inject(PatientMainFragment patientMainFragment);
    void inject(PatientAppointmentFragment patientAppointmentFragment);
    void inject(ScheduleFragment scheduleFragment);
    void inject(DetailAppointmentFragment detailAppointmentFragment);
    void inject(AppointmentDetailFragment appointmentDetailFragment);
    void inject(BookAppointmentService imageUploadService);
    void inject(CreateCaseFragment createCaseFragment);
    void inject(MyCasesFragment myCasesFragment);
    void inject(CaseDetailFragment caseDetailFragment);
    void inject(NewGroupDailog newGroupDailog);
    void inject(MembersView membersView);
    void inject(GroupFragment groupFragment);
    void inject(AddLocationDialog addLocationDialog);
    void inject(PersonalCareFragment personalCareFragment);
    void inject(HealthCareMonitoringFragment healthCareMonitoringFragment);
    void inject(FragmentBloodPressure fragmentBloodPressure);
    void inject(FragmentCholesterol fragmentCholesterol);
    void inject(FragmentGlucose fragmentBloodPressure);
    void inject(MedicalRecordsFragment medicalRecordsFragment);
    void inject(FragmentRadiology fragmentRadiology);
    void inject(FragmentLaboratory fragmentLaboratory);
    void inject(LifeStyleFragment lifeStyleFragment);
    void inject(FragmentExercise fragmentExercise);
    void inject(FragmentDiet fragmentDiet);
    void inject(HealthCareMonitoringGraphFragment healthCareMonitoringGraphFragment);
    void inject(RecordDetailFragment recordDetailFragment);
    void inject(BpDetailFragment bpDetailFragment);
    void inject(GlucoseDetailFragment glucoseDetailFragment);
    void inject(FragmentDietList fragmentDietList);
    void inject(DietDetailFragment dietDetailFragment);
    void inject(ExerciseDialog exerciseDialog);
    void inject(Personalcare personalcare);
    void inject(BloodPressureDialog bloodPressureDialog);
    void inject(BloodPressureFragment bloodPressureFragment);
    void inject(HealthMonitoringMain healthMonitoringMain);
    void inject(MainLifeStyleFragment mainLifeStyleFragment);
    void inject(DietMainFragment dietMainFragment);
    void inject(DietDialog dietDialog);
    void inject(GlucoseFragment glucoseFragment);
    void inject(CholesterolFragment cholesterolFragment);
    void inject(GlucoseDialog glucoseDialog);
    void inject(CholesterolDialog cholesterolDialog);
    void inject(CholesterolDetailFragment cholesterolDetailFragment);
    void inject(BMIDialog bmiDialog);
    void inject(BMIMainFragment bmiMainFragment);
    void inject(Appointment1DetailFragment appointment1DetailFragment);
    void inject(BMIDetailFragment bmiDetailFragment);
    void inject(MainMedicalRecord mainMedicalRecord);
    void inject(LaboratoryMainFragment laboratoryMainFragment);
    void inject(RadiologyMainFragment radiologyMainFragment);
    void inject(OutpatientMainFragment outpatientMainFragment);
    void inject(LaboratoryDialog laboratoryDialog);
    void inject(LaboratoryDetailFragment laboratoryDetailFragment);
    void inject(OutpatientDialog outpatientDialog);
    void inject(OutpatientDetailFragment outpatientDetailFragment);
    void inject(RadiologyDialog radiologyDialog);
    void inject(BpUpdateDialog bpUpdateDialog);
    void inject(CholesterolUpdateDialog cholesterolUpdateDialog);
    void inject(GlucoseUpdateDialog glucoseUpdateDialog);
    void inject(FragmentBloodPressureGraph fragmentBloodPressureGraph);
    void inject(LabImaageUpload labImageUpload);
    void inject(LaboratoryImageFragment laboratoryImageFragment);
    void inject(LabSpinnerDialog labSpinnerDialog);
    void inject(LabDetailFragment labDetailFragment);
    void inject(PharmacySpinnerDialog pharmacySpinnerDialog);
    void inject(PrescriptionDetailFragment prescriptionDetailFragment);
    void inject(AddRadiologySpinnerDialog addRadiologySpinnerDialog);
    void inject(MainPrescriptionFragment mainPrescriptionFragment);
    void inject(MainRecordDetailFragment recordDetailFragment);
    void inject(MainRadiologyFragment mainRadiologyFragment);
    void inject(RadiologyImageFragment radiologyImageFragment);
    void inject(RadiologyImgeUpload radiologyImageUpload);
    void inject(MainLaboratoryFragment mainLaboratoryFragment);
    void inject(AddLaboratoryDialog addLaboratoryDialog);
    void inject(AddPrescrptionDialog addPrescrptionDialog);
    void inject(AddRadiologyDialog addRadiologyDialog);
    void inject(LabImageFragment labImageFragment);
    void inject(RadImageFragment radImageFragment);
    void inject(AddPatientDialog addPatientDialog);
    void inject(ViewDoctorProfileDialog profileDialog);
    void inject(PatientMonitorFragment patientMonitorFragment);
    void inject(PatientDataFragment patientDataFragment);
    void inject(VitalSignFragment vitalSignFragment);
    void inject(AppointmentFragment appointmentFragment);
    void inject(ImageFragment imageFragment);
    void inject(AppointmentSubmitFragment appointmentSubmitFragment);
    void inject(EditCommentDialog editCommentDialog);

    Application provideApp();
    Retrofit retrofit();
    Gson gson();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
    ApiFactory mdcApi();


    void inject(AddCareGiverDailog addCareGiverDailog);

    void inject(CareGiverFragment careGiverFragment);

    void inject(HealthProviderAppointmentFragment healthProviderAppointmentFragment);

    void inject(SettingActivity settingActivity);

    void inject(GroupDetailFragment groupDetailFragment);

    void inject(HealthProviderViewModel healthProviderViewModel);

    void inject(PatientViewModel patientViewModel);


    void inject(NewPatient newPatient);

    void inject(AuthenticationDialog authenticationDialog);

    void inject(TermsActivity termsActivity);

    void inject(DialogActivity dialogActivity);

    void inject(InviteMemberActivity inviteMemberActivity);

  void inject(ForgotPassword forgotPassword);

  void inject(UpdatePatient updatePatient);

  void inject(BookAnAppointmentUpdatePatient bookAnAppointmentUpdatePatient);

    void inject(WelcomeActivity welcomeActivity);

    void inject(SplitFragment splitFragment);
}
