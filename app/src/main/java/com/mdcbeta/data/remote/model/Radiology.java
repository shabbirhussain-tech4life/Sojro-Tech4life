package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 28/06/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class Radiology {

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

    @SerializedName("report")
    private String report;

    @SerializedName("upload")
    private String upload;

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

    public void setReport(String report){
        this.report = report;
    }

    public String getReport(){
        return report;
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

    public void setUpload(String upload){
        this.upload = upload;
    }

    public String getUpload(){
        return upload;
    }

}
