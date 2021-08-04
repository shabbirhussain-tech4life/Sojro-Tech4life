package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 29/03/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class Diets {

    @SerializedName("users_id")
    private String userId;

    @SerializedName("date")
    private String Date;

    @SerializedName("time")
    private String Time;

    @SerializedName("type")
    private String foodtype;

    @SerializedName("foodname")
    private String foodname;

    @SerializedName("foodquantity")
    private String foodquantity;

    @SerializedName("foodcalories")
    private String foodcalories;

    @SerializedName("id")
    private String id;


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

    public void setFoodtype(String foodtype){
        this.foodtype = foodtype;
    }

    public String getFoodtype(){
        return foodtype;
    }

    public void setFoodname(String foodname){
        this.foodname = foodname;
    }

    public String getFoodname(){
        return foodname;
    }

    public void setFoodquantity(String foodquantity){
        this.foodquantity = foodquantity;
    }

    public String getFoodquantity(){
        return foodquantity;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setFoodcalories(String foodcalories){
        this.foodcalories = foodcalories;
    }

    public String getFoodcalories(){
        return foodcalories;
    }

}
