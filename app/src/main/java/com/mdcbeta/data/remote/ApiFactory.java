package com.mdcbeta.data.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.mdcbeta.data.model.CaseModel;
import com.mdcbeta.data.model.Diagnosis;
import com.mdcbeta.data.remote.model.AppointmentDoctor;
import com.mdcbeta.data.remote.model.BMI;
import com.mdcbeta.data.remote.model.BloodPressure;
import com.mdcbeta.data.remote.model.CareGiver;
import com.mdcbeta.data.remote.model.Cholesterol;
import com.mdcbeta.data.remote.model.City;
import com.mdcbeta.data.remote.model.Country;
import com.mdcbeta.data.remote.model.CreateGroup;
import com.mdcbeta.data.remote.model.Diets;
import com.mdcbeta.data.remote.model.DoctorSchedule;
import com.mdcbeta.data.remote.model.Exercise;
import com.mdcbeta.data.remote.model.ForgotPass;
import com.mdcbeta.data.remote.model.Glucose;
import com.mdcbeta.data.remote.model.GroupItem;
import com.mdcbeta.data.remote.model.Invited;
import com.mdcbeta.data.remote.model.InvitedByAdmin;
import com.mdcbeta.data.remote.model.LabDoctor;
import com.mdcbeta.data.remote.model.Laboratory;
import com.mdcbeta.data.remote.model.Location;
import com.mdcbeta.data.remote.model.Outpatient;
import com.mdcbeta.data.remote.model.PastHistory;
import com.mdcbeta.data.remote.model.Patient_Profile_Update;
import com.mdcbeta.data.remote.model.Patientid;
import com.mdcbeta.data.remote.model.PreviousSchedule;
import com.mdcbeta.data.remote.model.Radiology;
import com.mdcbeta.data.remote.model.SingleResponse;
import com.mdcbeta.data.remote.model.States;
import com.mdcbeta.data.remote.model.TimeSlots;
import com.mdcbeta.data.remote.model.User_Patient;
//import com.mdcbeta.data.remote.request_model.AppointmentCreated;
import com.mdcbeta.data.remote.model.AppointmentCreated;
import com.mdcbeta.data.remote.request_model.CaseCreated;
import com.mdcbeta.data.remote.request_model.InviteSecurity;
import com.mdcbeta.data.remote.request_model.Login;
import com.mdcbeta.data.remote.model.MainAppointmentDetail;
import com.mdcbeta.data.remote.model.MainAppointmentDetailImage;
import com.mdcbeta.data.remote.model.Response;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.model.Schedule;
import com.mdcbeta.data.remote.model.UserNames;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Shakil Karim on 4/9/17.
 */

public interface ApiFactory {


    @FormUrlEncoded
    @POST("GroupDetail")
    Observable<Response<List<JsonObject>>> groupDetail(@Field("group_id") String group_id);


    @FormUrlEncoded
    @POST("myusersDetails")
    Observable<Response<JsonObject>> myusersDetails(@Field("id") String id);

    @FormUrlEncoded
    @POST("groupDelete")
    Observable<Response<List<JsonObject>>> groupDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("DemoLicenseData")
    Observable<Response<List<Patientid>>> DemoLicenseData(@Field("id") String id);


    @POST("groupEdit")
    Observable<Response<List<Object>>> groupEdit(@Body CreateGroup createGroup);

    @POST("groupEditAddMember")
    Observable<Response<List<Object>>> groupEditAddMember(@Body CreateGroup createGroup);

    @FormUrlEncoded
    @POST("groups")
    Observable<Response<List<CreateGroup>>> groups(@Field("user_id") String user_id);

    @GET("users")
    Observable<Response<List<User>>> getUsers();




    @FormUrlEncoded
    @POST("filterUsername")
    Observable<Response<List<UserNames>>> filterUsername(@Field("name") String name, @Field("licenses_id") String licenses_id);

    @FormUrlEncoded
    @POST("isGroupExists")
    Observable<Response<List<Object>>> isGroupExists(@Field("group_name") String group_name);


    @GET("locations")
    Observable<Response<List<Location>>> locations();


    @GET("usersNames/{licenses_id}")
    Observable<Response<List<UserNames>>> usersNames(@Path("licenses_id") String licenses_id);


    @POST("createGroup")
    Observable<Response<List<Object>>> createGroup(@Body CreateGroup createGroup);


    @POST("login")
    Observable<Response<List<User>>> login(@Body Login login);




    @POST("inviteSecurity")
    Observable<Response<List<User>>> inviteSecurity(@Body InviteSecurity inviteSecurity);


    @POST("signup")
    Observable<Response<List<User>>> signup(@Body User user);

    // ADDED BY KANWAL 2/6/2020
    @POST("invite")
    Observable<Response<List<Invited>>> invite(@Body Invited invite);

    // ADDED BY KANWAL 7/7/2020
  @POST("forgotpass")
  Observable<Response<List<ForgotPass>>> forgotPass(@Body ForgotPass forgotPass);

    @POST("create_schedule")
    Observable<Response<List<Object>>> createSchedule(@Body Schedule user);

    @FormUrlEncoded
    @POST("getscheduledata")
    Observable<Response<JsonObject>> getscheduledata(@Field("doctor_id") String doctor_id);

  @FormUrlEncoded
    @POST("usersforappoint")
    Observable<Response<List<AppointmentDoctor>>> getUserforAppoint(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("doctorslots")
    Observable<Response<List<DoctorSchedule>>> getDoctorTimeSlots(@Field("doc_id") String doc_id);


    @FormUrlEncoded
    @POST("scheduleforDay")
    Observable<Response<List<TimeSlots>>> scheduleforDay(@Field("schedule_id") String schedule_id, @Field("day") String day, @Field("date") String date);


    @FormUrlEncoded
    @POST("scheduleforReviewer")
    Observable<Response<List<TimeSlots>>> scheduleforReviewer(@Field("schedule_id") String schedule_id, @Field("day") String day);


    @FormUrlEncoded
    @POST("getAppointmentfordoctors")
    Observable<Response<List<JsonObject>>> getAppointmentfordoctors(@Field("patient_id") String patient_id);

    @FormUrlEncoded
    @POST("getAppointmentfordoctorsPrevious")
    Observable<Response<List<JsonObject>>> getAppointmentfordoctorsPrevious(@Field("patient_id") String patient_id);

    @FormUrlEncoded
    @POST("getAppointmentforHealthProvider")
    Observable<Response<List<JsonObject>>> getAppointmentforHealthProvider(@Field("user_id") String doctor_id);

    @FormUrlEncoded
    @POST("getAppointmentforHealthProviderPrevious")
    Observable<Response<List<JsonObject>>> getAppointmentforHealthProviderPrevious(@Field("user_id") String doctor_id);


    @FormUrlEncoded
    @POST("getAppointmentforReviewer")
    Observable<Response<List<JsonObject>>> getAppointmentforReviewer(@Field("user_id") String doctor_id);

    @FormUrlEncoded
    @POST("getAppointmentforReviewerPrevious")
    Observable<Response<List<JsonObject>>> getAppointmentforReviewerPrevious(@Field("user_id") String doctor_id);


    @FormUrlEncoded
    @POST("getAppointmentDetails")
    Flowable<Response<List<MainAppointmentDetail>>> getAppointmentDetails(@Field("record_id") String record_id);

    @FormUrlEncoded
    @POST("getAppointmentDetailsImages")
    Flowable<Response<List<MainAppointmentDetailImage>>> getAppointmentDetailsImages(@Field("record_id") String record_id);


    @FormUrlEncoded
    @POST("mycases")
    Observable<Response<List<JsonObject>>> mycases(@Field("referer_id") String referer_id);


    @GET("countries")
    Observable<Response<List<Country>>> countries();


    @GET("states/{country_id}")
    Observable<Response<List<States>>> states(@Path("country_id") int country_id);


    @GET("cities/{state_id}")
    Observable<Response<List<City>>> cities(@Path("state_id") int state_id);

    @FormUrlEncoded
    @POST("previousSchedules")
    Observable<Response<List<PreviousSchedule>>> previousSchedules(@Field("doctor_id") String doctor_id);

    @FormUrlEncoded
    @POST("userLocation")
    Observable<Response<List<Object>>> userLocation(@Field("user_id") String user_id,
                                                    @Field("country_id") String country_id,
                                                    @Field("state_id") String state_id,
                                                    @Field("city_id") String city_id,
                                                    @Field("area") String area);

    @FormUrlEncoded
    @POST("addCareGiver")
    Observable<Response<List<Object>>> addCareGiver(@Field("user_id") String user_id,
                                                    @Field("name") String name,
                                                    @Field("email") String email,
                                                    @Field("mobile") String mobile,
                                                    @Field("relation") String relation,
                                                    @Field("send_to") String send_to);

    @FormUrlEncoded
    @POST("getCareGiver")
    Observable<Response<List<CareGiver>>> getCareGiver(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("groupByUsername")
    Observable<Response<List<GroupItem>>> groupByUsername(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("createAppointmentfromMycase")
    Observable<Response<List<GroupItem>>> createAppointmentfromMycase(
            @Field("case_id") String case_id,
            @Field("doctor_id") String doctor_id,
            @Field("referer_id") String referer_id,
            @Field("slot_id") String slot_id,
            @Field("patient_id") String patient_id,
            @Field("date") String date,
            @Field("create_at") String create_at,
            @Field("code") String code,
            @Field("booked_as") String booked_as);



    @FormUrlEncoded
    @POST("ReviewerCasesUser")
    Observable<Response<List<JsonObject>>> ReviewerCasesUser(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("ReviewerCasesGroups")
    Observable<Response<List<JsonObject>>> ReviewerCasesGroups(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("addCommentsInCase")
    Observable<Response<List<JsonObject>>> addCommentsInCase(
            @Field("sender_id") String sender_id,
            @Field("comment") String comment,
            @Field("case_id") String case_id,
            @Field("created_at") String created_at);


    @POST("updateschedule")
    Observable<Response<List<Object>>> updateschedule(@Body Schedule user);


    @Multipart
    @POST("createAppointment")
    Observable<Response<List<String>>> createAppointment(
            @Part("description") String description,
            @Part("referer_id") String referer_id,
            @Part("patient_id") String patient_id,
            @Part("name") String name,
            @Part("lname") String lname,
            @Part("gender") String gender,
            @Part("y_age") String y_age,
            @Part("month_age") String month_age,
            @Part("m_age") String m_age,
            @Part("slot_id") String slot_id,
            @Part("date") String date,
            @Part("created_date") String created_date,
            @Part("booked_as") String booked_as,
            @Part("unique_key") String unique_key,
            @Part List<MultipartBody.Part> files);


    @Multipart
    @POST("createAppointmentfromCase")
    Observable<Response<List<String>>> createAppointmentfromCase(
            @Part("case_code") String case_code,
            @Part("description") String description,
            @Part("referer_id") String referer_id,
            @Part("patient_id") String patient_id,
            @Part("name") String fname,
            @Part("lname") String lname,
            @Part("gender") String gender,
            @Part("y_age") String y_age,
            @Part("month_age") String month_age,
            @Part("m_age") String m_age,
            @Part("slot_id") String slot_id,
            @Part("date") String date,
            @Part("created_date") String created_date,
            @Part("temperature") String temperature,
            @Part("temp_radio") String temp_radio,
            @Part("temp_unit") String temp_unit,
            @Part("systolic") String systolic,
            @Part("diastolic") String diastolic,
            @Part("bp_radio") String bp_radio,
            @Part("bp_arm") String bp_arm,
            @Part("pulse") String pulse,
            @Part("pulse_radio") String pulse_radio,
            @Part("weight") String weight,
            @Part("weight_unit") String weight_unit,
            @Part("height") String height,
            @Part("height_unit") String height_unit,
            @Part("pain_radio") String pain_radio,
            @Part("pain_value") String pain_value,
            @Part("cuff") String cuff,
            @Part("oxygen_sat") String oxygen_sat,
            @Part("oxygen_sat_radio") String oxygen_sat_radio,
            @Part("res_rate") String res_rate,
            @Part("booked_as") String booked_as,
            @Part("unique_key") String unique_key,
            @Part List<Integer> groups,
            @Part List<Integer> users,
            @Part List<MultipartBody.Part> files);



    @POST("createCase")
    Observable<SingleResponse<String>> createCase(@Body CaseCreated caseCreated);

    @POST("createAppoint")
    Observable<SingleResponse<String>> createAppoint(@Body AppointmentCreated caseCreated);


    @POST("createcase")
    Observable<SingleResponse<String>> createcase(@Body CaseModel caseModel);

    @Multipart
    @POST("addCaseAttachments")
    Observable<Response<List<String>>> addCaseAttachments(
            @Part("case_id") String case_id,
            @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("addAppointmentAttachments")
    Observable<Response<List<String>>> addAppointmentAttachments(
            @Part("case_id") String case_id,
            @Part List<MultipartBody.Part> files);


    @GET("existing_id")
    Observable<Response<List<Patientid>>> existing_id();




    @FormUrlEncoded
    @POST("getPatientData")
    Observable<Response<JsonObject>> getPatientData(@Field("id") String id);

    @POST("signup_patient")
    Observable<Response<List<User_Patient>>> signup_patient(@Body User_Patient user);

    //added by kk 7/27/2020

  @POST("updatePatientProfile")
  Observable<Response<List<Patient_Profile_Update>>> updatePatientProfile(@Body Patient_Profile_Update patient_profile_update);

    @Multipart
    @POST("updateProfilePic")
    Observable<Response<List<String>>> updateProfilePic(
            @Part("firstname") String firstname,
            @Part("lastname") String lastname,
            @Part("user_id") String user_id,
            @Part("email") String email,
            @Part("name") String name,
            @Part("username") String username,
            @Part("speciality") String speciality,
            @Part("gender") String gender,
            @Part("password") String password,
            @Part("country") String country,
            @Part("state") String state,
            @Part("city") String city,
            @Part("dobirth") String dobirth,
            @Part("affiliation") String affiliation,
            @Part("degree") String degree,
            @Part("bio") String bio,
            @Part("address") String address,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("addBloodPressureRecord")
    Observable<Response<List<Object>>> addBloodPressureRecord(@Field("user_id") String user_id,
                                                              @Field("date") String date,
                                                              @Field("time") String time,
                                                              @Field("systolic") String systolic,
                                                              @Field("diastolic") String diastolic,
                                                              @Field("result") String result,
                                                              @Field("comment") String comment,
                                                              @Field("created_at") String created_at,
                                                              @Field("updated_at") String updated_at);

    @FormUrlEncoded
    @POST("addBMIRecord")
    Observable<Response<List<Object>>> addBMIRecord(@Field("user_id") String user_id,
                                                    @Field("date") String date,
                                                    @Field("time") String time,
                                                    @Field("height_feet") String height_feet,
                                                    @Field("height_inches") String height_inches,
                                                    @Field("weight") String weight,
                                                    @Field("weight_unit") String weight_unit,
                                                    @Field("calculated_bmi") String calculated_bmi,
                                                    @Field("comment") String comment,
                                                    @Field("created_at") String created_at,
                                                    @Field("updated_at") String updated_at);


    @FormUrlEncoded
    @POST("addGlucoseRecord")
    Observable<Response<List<Object>>> addGlucoseRecord(@Field("user_id") String user_id,
                                                        @Field("date") String date,
                                                        @Field("time") String time,
                                                        @Field("fasting") String fasting,
                                                        @Field("device") String device,
                                                        @Field("result") String result,
                                                        @Field("unit") String unit,
                                                        @Field("comment") String comment,
                                                        @Field("created_at") String created_at,
                                                        @Field("updated_at") String updated_at);


    @FormUrlEncoded
    @POST("addCholesterolRecord")
    Observable<Response<List<Object>>> addCholesterolRecord(@Field("user_id") String user_id,
                                                            @Field("date") String date,
                                                            @Field("time") String time,
                                                            @Field("device") String device,
                                                            @Field("result") String result,
                                                            @Field("unit") String unit,
                                                            @Field("comment") String comment,
                                                            @Field("created_at") String created_at,
                                                            @Field("updated_at") String updated_at);


    @FormUrlEncoded
    @POST("addRadiologiesRecord")
    Observable<Response<List<Object>>> addRadiologiesRecord(@Field("user_id") String user_id,
                                                            @Field("date") String date,
                                                            @Field("time") String time,
                                                            @Field("result") String result,
                                                            @Field("upload") String upload,
                                                            @Field("comment") String comment,
                                                            @Field("created_at") String created_at,
                                                            @Field("updated_at") String updated_at);


    @FormUrlEncoded
    @POST("addLaboratoriesRecord")
    Observable<Response<List<Object>>> addLaboratoriesRecord(@Field("user_id") String user_id,
                                                             @Field("date") String date,
                                                             @Field("time") String time,
                                                             @Field("test") String test,
                                                             @Field("result") String result,
                                                             @Field("comment") String comment,
                                                             @Field("created_at") String created_at,
                                                             @Field("updated_at") String updated_at);

    @FormUrlEncoded
    @POST("addExerciseRecord")
    Observable<Response<List<Object>>> addExerciseRecord(@Field("user_id") String user_id,
                                                         @Field("date") String date,
                                                         @Field("time") String time,
                                                         @Field("duration") String duration,
                                                         @Field("activity") String activity,
                                                         @Field("comment") String comment,
                                                         @Field("created_at") String created_at,
                                                         @Field("updated_at") String updated_at);

    @FormUrlEncoded
    @POST("addDietRecord")
    Observable<Response<List<Object>>> addDietRecord(@Field("users_id") String user_id,
                                                     @Field("date") String date,
                                                     @Field("time") String time,
                                                     @Field("food_type") String food_type,
                                                     @Field("foodname") String foodname,
                                                     @Field("foodquantity") String foodquantity,
                                                     @Field("foodcalories") String foodcalories,
                                                     @Field("created_at") String created_at,
                                                     @Field("updated_at") String updated_at);

    @FormUrlEncoded
    @POST("addOutpatientRecord")
    Observable<Response<List<Object>>> addOutpatientRecord(@Field("users_id") String user_id,
                                                           @Field("date") String date,
                                                           @Field("type") String type,
                                                           @Field("healthfacility") String healthfacility,
                                                           @Field("healthprovider") String healthprovider,
                                                           @Field("complaint") String complaint,
                                                           @Field("diagnosis") String diagnosis,
                                                           @Field("medication") String medication,
                                                           @Field("advice") String advice,
                                                           @Field("fellowup") String fellowup,
                                                           @Field("comment") String comment,
                                                           @Field("created_at") String created_at,
                                                           @Field("updated_at") String updated_at);


    @FormUrlEncoded
    @POST("exerciseDelete")
    Observable<Response<List<JsonObject>>> exerciseDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("bpDelete")
    Observable<Response<List<JsonObject>>> bpDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("glucoseDelete")
    Observable<Response<List<JsonObject>>> glucoseDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("cholesterolDelete")
    Observable<Response<List<JsonObject>>> cholesterolDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("laboratoriesDelete")
    Observable<Response<List<JsonObject>>> laboratoriesDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("radiologiesDelete")
    Observable<Response<List<JsonObject>>> radiologiesDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("outpatientDelete")
    Observable<Response<List<JsonObject>>> outpatientDelete(@Field("id") String id);


    @FormUrlEncoded
    @POST("getExercise")
    Observable<Response<List<Exercise>>> getExercise(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getOutpatient")
    Observable<Response<List<Outpatient>>> getOutpatient(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getBMI")
    Observable<Response<List<BMI>>> getBMI(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getBloodPressure")
    Observable<Response<List<BloodPressure>>> getBloodPressure(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getGlucose")
    Observable<Response<List<Glucose>>> getGlucose(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getCholesterol")
    Observable<Response<List<Cholesterol>>> getCholesterol(@Field("users_id") String user_id);


    @FormUrlEncoded
    @POST("getExerciseDetail")
    Observable<Response<JsonObject>> getExerciseDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("getBMIDetail")
    Observable<Response<JsonObject>> getBMIDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("getOutpatientDetail")
    Observable<Response<JsonObject>> getOutpatientDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("mycasesDetails")
    Observable<Response<JsonObject>> mycasesDetails(@Field("case_id") String case_id, @Field("case_code") String case_code);


    @FormUrlEncoded
    @POST("getDiet")
    Observable<Response<JsonObject>> getDiet(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getDietList")
    Observable<Response<List<Diets>>> getDietList(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("dietDelete")
    Observable<Response<List<JsonObject>>> dietDelete(@Field("id") String id);

    @FormUrlEncoded
    @POST("getBloodpressureDetail")
    Observable<Response<JsonObject>> getBloodpressureDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("getGlucoseDetail")
    Observable<Response<JsonObject>> getGlucoseDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("getCholesterolDetail")
    Observable<Response<JsonObject>> getCholesterolDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("getLaboratoriesDetail")
    Observable<Response<JsonObject>> getLaboratoriesDetail(@Field("id") String id);


    @FormUrlEncoded
    @POST("getLaboratories")
    Observable<Response<List<Laboratory>>> getLaboratories(@Field("users_id") String user_id);

    @FormUrlEncoded
    @POST("getRadiologies")
    Observable<Response<List<Radiology>>> getRadiologies(@Field("users_id") String user_id);

    @POST("ExerciseEdit")
    Observable<Response<List<Object>>> ExerciseEdit(@Body CreateGroup createGroup);

    @Multipart
    @POST("updateExerciseRecord")
    Observable<Response<List<String>>> updateExerciseRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("duration") String duration,
            @Part("activity") String activity,
            @Part("comment") String comment
    );

    @Multipart
    @POST("updateBpRecord")
    Observable<Response<List<String>>> updateBpRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("systolic") String systolic,
            @Part("diastolic") String diastolic,
            @Part("comment") String comment
    );

    @Multipart
    @POST("updateGlucoseRecord")
    Observable<Response<List<String>>> updateGlucoseRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("fasting") String fasting,
            @Part("device") String device,
            @Part("result") String result,
            @Part("unit") String unit,
            @Part("comment") String comment
    );


    @Multipart
    @POST("updateCholesterolRecord")
    Observable<Response<List<String>>> updateCholesterolRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("device") String device,
            @Part("result") String result,
            @Part("unit") String unit,
            @Part("comment") String comment
    );


    @Multipart
    @POST("updateDietRecord")
    Observable<Response<List<String>>> updateDietRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("name") String name,
            @Part("quantity") String quantity,
            @Part("calories") String calories
    );

    @Multipart
    @POST("updateLaboratoryRecord")
    Observable<Response<List<String>>> updateLaboratoryRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("time") String time,
            @Part("test") String test,
            @Part("result") String result,
            @Part("comment") String comment
    );

    @Multipart
    @POST("updateOutpatientRecord")
    Observable<Response<List<String>>> updateOutpatientRecord(
            @Part("id") String id,
            @Part("date") String date,
            @Part("type") String type,
            @Part("healthfacility") String healthfacility,
            @Part("healthprovider") String healthprovider,
            @Part("complaint") String complaint,
            @Part("diagnosis") String diagnosis,
            @Part("medication") String medication,
            @Part("advice") String advice,
            @Part("fellowup") String fellowup,
            @Part("comment") String comment
    );


    @Multipart
    @POST("updateRadiologyStatus")
    Observable<Response<List<String>>> updateRadiologyStatus(@Part("id") String user_id,
                                                             @Part("name") String name);


    @Multipart
    @POST("updateLabStatus")
    Observable<Response<List<String>>> updateLabStatus(@Part("id") String user_id,
                                                       @Part("name") String name);


    @Multipart
    @POST("updatePresStatus")
    Observable<Response<List<String>>> updatePresStatus(@Part("id") String user_id,
                                                        @Part("name") String name);

    @Multipart
    @POST("radiology_image")
    Observable<Response<List<String>>> radiology_image(
            @Part("id") String case_id,
            @Part List<MultipartBody.Part> files,
            @Part("record_id") String record_id);

    @Multipart
    @POST("lab_image")
    Observable<Response<List<String>>> lab_image(
            @Part("id") String case_id,
            @Part List<MultipartBody.Part> files,
            @Part("record_id") String record_id);

    @FormUrlEncoded
    @POST("radiology_detail")
    Observable<Response<JsonObject>> radiology_detail(@Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("laboratory_detail")
    Observable<Response<JsonObject>> laboratory_detail(@Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("pharmacy_detail")
    Observable<Response<JsonObject>> pharmacy_detail(@Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("radiology_cases")
    Observable<Response<List<JsonObject>>> radiology_cases(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("laboratory_cases")
    Observable<Response<List<JsonObject>>> laboratory_cases(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("pharmacy_cases")
    Observable<Response<List<JsonObject>>> pharmacy_cases(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("rad_images")
    Observable<Response<JsonObject>> rad_images(@Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("lab_images")
    Observable<Response<JsonObject>> lab_images(@Field("case_id") String case_id);

    @FormUrlEncoded
    @POST("addRadiology")
    Observable<Response<List<Object>>> addRadiology(@Field("user_id") String user_id,
                                                    @Field("record_id") String record_id,
                                                    @Field("case_code") String case_code,
                                                    @Field("diagnosis") String name,
                                                    @Field("pregnancy") String pregnancy,
                                                    @Field("images23") String images23,
                                                    @Field("typeofimage") String typeofimage,
                                                    @Field("instructions") String instructions,
                                                    @Field("refertoradiology") String refertoradiology,
                                                    @Field("addmoreimages") String addmoreimages,
                                                    @Field("created_at") String created_at);

    @FormUrlEncoded
    @POST("addPrescription")
    Observable<Response<List<Object>>> addPrescription(@Field("user_id") String user_id,
                                                       @Field("record_id") String record_id,
                                                       @Field("case_code") String case_code,
                                                       @Field("medicine") String medicine,
                                                       @Field("doseNumber") String doseNumber,
                                                       @Field("dose") String dose,
                                                       @Field("route") String route,
                                                       @Field("refertopharmacy") String refertopharmacy,
                                                       @Field("frequency") String frequency,
                                                       @Field("duration") String duration,
                                                       @Field("com") String com,
                                                       @Field("created_at") String created_at,
                                                       @Field("textarea") String textarea);

    @FormUrlEncoded
    @POST("addLaboratory")
    Observable<Response<List<Object>>> addLaboratory(@Field("user_id") String user_id,
                                                     @Field("record_id") String record_id,
                                                     @Field("case_code") String case_code,
                                                     @Field("diagnosisl") String diagnosisl,
                                                     @Field("textView1") String textView1,
                                                     @Field("instructionsl") String instructionsl,
                                                     @Field("laboratoryname") String laboratoryname,
                                                     @Field("status") String status,
                                                     @Field("created_at") String created_at);


    @GET("rad_doctor")
    Observable<Response<List<LabDoctor>>> rad_doctor();

    @GET("lab_doctor")
    Observable<Response<List<LabDoctor>>> lab_doctor();

    @GET("pres_doctor")
    Observable<Response<List<LabDoctor>>> pres_doctor();

    @FormUrlEncoded
    @POST("myLabDetails")
    Observable<Response<JsonObject>> myLabDetails(@Field("case_id") String case_id);


    @FormUrlEncoded
    @POST("addChat")
    Observable<Response<JsonObject>> addChat(@Field("message") String messages, @Field("record_id") String recordId);

    @FormUrlEncoded
    @POST("updateCommentsInCase")
    Observable<Response<JsonArray>> updateComment(@Field("record_id") String recordId, @Field("comment") String comment,
                                                  @Field("commented_by") String senderId, @Field("created_at") String createdAt,
                                                  @Field("commented_id") String commentId);

    @POST("reviewer_diagnosis")
    Observable<Response<List<JsonObject>>> addReviewerDiagnose(@Query("record_id") String recordId,
                                                               @Body Diagnosis diagnosis);


    // ADDED BY KANWAL 3/12/2020
    @POST("inviteByAdmin")
    Observable<Response<List<InvitedByAdmin>>>inviteByAdmin(@Body InvitedByAdmin invitedByAdmin);


    //FOR PAST HISTORY
    //@POST("PastHistory")
    //Observable<SingleResponse<String>> PastHistory(@Body PastHistory pastHistory);

  @POST("PastHistory")
  Observable<Response<List<PastHistory>>> PastHistory(@Body PastHistory pastHistory);

}
