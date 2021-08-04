package com.mdcbeta.data.remote.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mdcbeta.util.DayTimeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class Day {
    @SerializedName("data")
    @Expose
    public List<DayTime> data = new ArrayList<>();

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(DayTime.class, new DayTimeAdapter()).create();
        return gson.toJson(this.data);
    }

}
