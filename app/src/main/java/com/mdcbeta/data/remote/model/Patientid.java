package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Patientid {

    @SerializedName("id")
    private String id;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("name")
    private String name;

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public String getLastname(){
        return lastname;
    }

    @Override
    public String toString() {
        return name;
    }
}
