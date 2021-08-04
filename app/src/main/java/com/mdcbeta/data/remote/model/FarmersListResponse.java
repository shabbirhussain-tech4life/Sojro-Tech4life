package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mdcbeta.data.model.Farmer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakil Karim on 4/9/17.
 */

public class FarmersListResponse {
    @SerializedName("farmer")
    @Expose
    public List<Farmer> farmer = new ArrayList<Farmer>();
    @SerializedName("message")
    @Expose
    public String message;
}
