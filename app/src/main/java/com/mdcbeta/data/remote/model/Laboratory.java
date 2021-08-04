package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 28/06/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class Laboratory {

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

    @SerializedName("test")
    private String test;

    @SerializedName("result")
    private String result;

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

    public void setTest(String test){
        this.test = test;
    }

    public String getTest(){
        return test;
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

    public void setResult(String result){
        this.result = result;
    }

    public String getResult(){
        return result;
    }

}
