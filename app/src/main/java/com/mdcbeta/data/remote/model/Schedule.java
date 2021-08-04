package com.mdcbeta.data.remote.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class Schedule {

    @SerializedName("doctor_id")
    @Expose
    public String doctor_id;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("start_date")
    @Expose
    public String start_date;
    @SerializedName("end_date")
    @Expose
    public String end_date;
    @SerializedName("slot_price")
    @Expose
    public String slot_price;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("mon")
    @Expose
    public Day mon;
    @SerializedName("tue")
    @Expose
    public Day tue;
    @SerializedName("wed")
    @Expose
    public Day wed;
    @SerializedName("thr")
    @Expose
    public Day thr;
    @SerializedName("fri")
    @Expose
    public Day fri;
    @SerializedName("sat")
    @Expose
    public Day sat;
    @SerializedName("sun")
    @Expose
    public Day sun;


    public Schedule(String id,String doctor_id, String start_date, String end_date, String slot_price, String currency, Day mon, Day tue, Day wed, Day thr, Day fri, Day sat, Day sun) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.slot_price = slot_price;
        this.currency = currency;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thr = thr;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public Schedule() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSlot_price() {
        return slot_price;
    }

    public void setSlot_price(String slot_price) {
        this.slot_price = slot_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Day getMon() {
        return mon;
    }

    public void setMon(Day mon) {
        this.mon = mon;
    }

    public Day getTue() {
        return tue;
    }

    public void setTue(Day tue) {
        this.tue = tue;
    }

    public Day getWed() {
        return wed;
    }

    public void setWed(Day wed) {
        this.wed = wed;
    }

    public Day getThr() {
        return thr;
    }

    public void setThr(Day thr) {
        this.thr = thr;
    }

    public Day getFri() {
        return fri;
    }

    public void setFri(Day fri) {
        this.fri = fri;
    }

    public Day getSat() {
        return sat;
    }

    public void setSat(Day sat) {
        this.sat = sat;
    }

    public Day getSun() {
        return sun;
    }

    public void setSun(Day sun) {
        this.sun = sun;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this,Schedule.class);
        return  json;
    }
}
