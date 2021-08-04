package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 28/06/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class Outpatient {

    @SerializedName("users_id")
    private String users_id;

    @SerializedName("id")
    private String id;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("date")
    private String date;

    @SerializedName("type")
    private String type;

    @SerializedName("healthfacility")
    private String healthfacility;

    @SerializedName("healthprovider")
    private String healthprovider;

    @SerializedName("complaint")
    private String complaint;

    @SerializedName("diagnosis")
    private String diagnosis;

    @SerializedName("medication")
    private String medication;

    @SerializedName("advice")
    private String advice;

    @SerializedName("fellowup")
    private String fellowup;

    @SerializedName("comment")
    private String comment;

    @SerializedName("created_at")
    private String createdAt;


    public void setUsers_id(String users_id){
        this.users_id = users_id;
    }

    public String getUsers_id(){
        return users_id;
    }

    public void setid(String id){
        this.id = id;
    }

    public String getid(){
        return id;
    }

    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt(){
        return updatedAt;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setHealthfacility(String healthfacility){
        this.healthfacility = healthfacility;
    }

    public String getHealthfacility(){
        return healthfacility;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }

    public void setHealthprovider(String healthprovider){
        this.healthprovider = healthprovider;
    }

    public String getHealthprovider(){
        return healthprovider;
    }


    public void setComplaint(String complaint){
        this.complaint = complaint;
    }

    public String getComplaint(){
        return complaint;
    }


    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis(){
        return diagnosis;
    }

    public void setMedication(String medication){
        this.medication = medication;
    }

    public String getMedication(){
        return medication;
    }

    public void setAdvice(String advice){
        this.advice = advice;
    }

    public String getAdvice(){
        return advice;
    }

    public void setFellowup(String fellowup){
        this.fellowup = fellowup;
    }

    public String getFellowup(){
        return fellowup;
    }


}
