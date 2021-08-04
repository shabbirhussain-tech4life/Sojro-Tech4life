package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 26/03/2018.
 */
@Generated("com.robohorse.robopojogenerator")
public class Glucose {

    @SerializedName("users_id")
    private String userId;

    @SerializedName("date")
    private String Date;

    @SerializedName("time")
    private String Time;

    @SerializedName("device")
    private String device;

    @SerializedName("result")
    private String result;

    @SerializedName("unit")
    private String unit;

    @SerializedName("comment")
    private String comment;

    @SerializedName("id")
    private String id;

    @SerializedName("fasting")
    private String fasting;


    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void setDate(String Date){
        this.Date = Date;
    }

    public String getDate(){
        return Date;
    }

    public void setTime(String Time){
        this.Time = Time;
    }

    public String getTime(){
        return Time;
    }

    public void setDevice(String device){
        this.device = device;
    }

    public String getDevice(){
        return device;
    }

    public void setResult(String result){
        this.result = result;
    }

    public String getResult(){
        return result;
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

    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getUnit(){
        return unit;
    }

    public void setFasting(String fasting){
        this.fasting = fasting;
    }

    public String getFasting(){
        return fasting;
    }

}
