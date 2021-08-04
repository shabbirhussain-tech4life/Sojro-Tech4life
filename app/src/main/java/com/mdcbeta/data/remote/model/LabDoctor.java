package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 14/11/2018.
 */

@Generated("com.robohorse.robopojogenerator")
public class LabDoctor{

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;


    public String getUsername(){
        return username;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }


}