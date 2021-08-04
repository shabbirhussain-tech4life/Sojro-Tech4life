package com.mdcbeta.data.remote.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



/**
 * Created by Shakil Karim on 4/19/17.
 */
@Entity
public class MainAppointmentDetailImage {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @NonNull
    public String id;
    @SerializedName("record_id")
    @Expose
    public String recordId;
    @SerializedName("image")
    @Expose
    public String image;

}
