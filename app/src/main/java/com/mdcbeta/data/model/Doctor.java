package com.mdcbeta.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakil Karim on 4/15/17.
 */

public class Doctor {

    private String name;
    private String pic;
    private String type;
    private String rate;

    public Doctor(String name, String pic, String type, String rate) {
        this.name = name;
        this.pic = pic;
        this.type = type;
        this.rate = rate;
    }

    public Doctor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public static List<Doctor> createDummy(){
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Dr AndreaFay","Suha Tirmizi","General Physician","$9.99/Appointment"));
        return doctors;
    }



}
