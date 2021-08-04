package com.mdcbeta.data.remote.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class DayTime {

    @SerializedName("open")
    @Expose
    public String open;
    @SerializedName("start_time")
    @Expose
    public String start_time;
    @SerializedName("end_time")
    @Expose
    public String end_time;


    public DayTime(String open, String start_time, String end_time) {
        this.open = open;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public DayTime() {
    }


    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this,DayTime.class);
        return  json;
    }


    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
