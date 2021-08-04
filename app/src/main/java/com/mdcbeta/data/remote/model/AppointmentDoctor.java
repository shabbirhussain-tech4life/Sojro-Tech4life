package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class AppointmentDoctor {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("speciality")
    @Expose
    public String speciality;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("affiliation")
    @Expose
    public String affiliation;
    @SerializedName("degree")
    @Expose
    public String degree;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("remember_token")
    @Expose
    public String rememberToken;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("license_owner")
    @Expose
    public String licenseOwner;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("switch_role")
    @Expose
    public String switchRole;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("slot_price")
    @Expose
    public String slotPrice;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("bio")
    @Expose
    public String bio;


    @Override
    public String toString() {
        return name;
    }


    public AppointmentDoctor(String name) {
        this.name = name;
    }

    public AppointmentDoctor() {
    }
}
