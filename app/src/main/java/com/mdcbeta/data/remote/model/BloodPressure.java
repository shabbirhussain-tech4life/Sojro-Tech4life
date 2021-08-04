package com.mdcbeta.data.remote.model;

/**
 * Created by MahaRani on 02/02/2018.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class BloodPressure{

    @SerializedName("users_id")
    private String userId;

    @SerializedName("date")
    private String bpDate;

    @SerializedName("time")
    private String bpTime;

    @SerializedName("systolic")
    private String systolic;

    @SerializedName("diastolic")
    private String diastolic;

    @SerializedName("comment")
    private String comment;

    @SerializedName("id")
    private String id;


    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void setBpDate(String bpDate){
        this.bpDate = bpDate;
    }

    public String getBpDate(){
        return bpDate;
    }

    public void setBpTime(String bpTime){
        this.bpTime = bpTime;
    }

    public String getBpTime(){
        return bpTime;
    }

    public void setSystolic(String systolic){
        this.systolic = systolic;
    }

    public String getSystolic(){
        return systolic;
    }

    public void setDiastolic(String diastolic){
        this.diastolic = diastolic;
    }

    public String getDiastolic(){
        return diastolic;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

}