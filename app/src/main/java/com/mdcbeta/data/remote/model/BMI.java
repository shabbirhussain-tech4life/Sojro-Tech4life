package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 27/06/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class BMI {

    @SerializedName("users_id")
    private String users_id;

    @SerializedName("id")
    private String id;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("height_feet")
    private String height_feet;

    @SerializedName("height_inches")
    private String height_inches;


    @SerializedName("weight")
    private String weight;

    @SerializedName("weight_unit")
    private String weight_unit;

    @SerializedName("calculated_bmi")
    private String calculated_bmi;

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

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public void setHeight_feet(String height_feet){
        this.height_feet = height_feet;
    }

    public String getHeight_feet(){
        return height_feet;
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

    public void setHeight_inches(String height_inches){
        this.height_inches = height_inches;
    }

    public String getHeight_inches(){
        return height_inches;
    }

    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getWeight(){
        return weight;
    }

    public void setWeight_unit(String weight_unit){
        this.weight_unit = weight_unit;
    }

    public String getWeight_unit(){
        return weight_unit;
    }

    public void setCalculated_bmi(String calculated_bmi){
        this.calculated_bmi = calculated_bmi;
    }

    public String getCalculated_bmi(){
        return calculated_bmi;
    }


}
