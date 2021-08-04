package com.mdcbeta.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



/**
 * Created by Shakil Karim on 4/18/17.
 */

@Entity(tableName = "MainAppointment",primaryKeys = {"record_id","id"})
public class MainAppointment {

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("id")
    @Expose
    @NonNull
    public String id;

    @SerializedName("record_id")
    @Expose
    @NonNull
    public String record_id;

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("start_time")
    @Expose
    public String start_time;
    @SerializedName("end_time")
    @Expose
    public String end_time;
    @SerializedName("start_date")
    @Expose
    public String start_date;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("room_url")
    @Expose
    public String room_url;

    @ColumnInfo(name = "patient_id")
    public String patient_id;





}
