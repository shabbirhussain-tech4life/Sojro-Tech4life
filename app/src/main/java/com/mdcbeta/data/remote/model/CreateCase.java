package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by MahaRani on 10/03/2018.
 */
@Generated("com.robohorse.robopojogenerator")
public class CreateCase {

    @SerializedName("case_code")
    private String case_code;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("gender")
    private String gender;

    @SerializedName("y_age")
    private String y_age;

    @SerializedName("month_age")
    private String month_age;

    @SerializedName("m_age")
    private String m_age;



    public void setCase_code(String case_code){
        this.case_code = case_code;
    }

    public String getCase_code(){
        return case_code;
    }


}
