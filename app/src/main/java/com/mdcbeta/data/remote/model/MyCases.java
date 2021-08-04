package com.mdcbeta.data.remote.model;

/**
 * Created by Shakil Karim on 4/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCases {

    @SerializedName("id")
    @Expose
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

}


