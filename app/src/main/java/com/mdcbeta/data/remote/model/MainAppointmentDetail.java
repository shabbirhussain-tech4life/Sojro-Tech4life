package com.mdcbeta.data.remote.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



/**
 * Created by shakil on 4/18/2017.
 */
@Entity(tableName = "MainAppointmentDetail")
public class MainAppointmentDetail {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @NonNull
    public String id;

    @SerializedName("case_code")
    @Expose
    public String caseCode;
    @SerializedName("fname")
    @Expose
    public String fname;
    @SerializedName("lname")
    @Expose
    public String lname;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("y_age")
    @Expose
    public String yAge;
    @SerializedName("month_age")
    @Expose
    public String monthAge;
    @SerializedName("m_age")
    @Expose
    public String mAge;
    @SerializedName("disease_id")
    @Expose
    public String diseaseId;
    @SerializedName("location_id")
    @Expose
    public String locationId;
    @SerializedName("history")
    @Expose
    public String history;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("form_id")
    @Expose
    public String formId;
    @SerializedName("patient_id")
    @Expose
    public String patientId;
    @SerializedName("referer_id")
    @Expose
    public String refererId;
    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public String deletedAt;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("speciality")
    @Expose
    public String speciality;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("affiliation")
    @Expose
    public String affiliation;
    @SerializedName("degree")
    @Expose
    public String degree;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("remember_token")
    @Expose
    public String rememberToken;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("license_owner")
    @Expose
    public String licenseOwner;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("switch_role")
    @Expose
    public String switchRole;

}
