package com.mdcbeta.data.remote.request_model;

import java.util.List;

/**
 * Created by Shakil Karim on 5/20/17.
 */

public class CaseCreated {

    public String case_code;
    public String fname;
    public String lname;
    public String gender;
    public String y_age;
    public String m_age;
    public String disease_id;
    public String location_id;
    public String history;
    public String question;
    public String city;
    public String form_id;
    public String patient_id;
    public String referer_id;
    public String data;
    public String comment;
    List<Integer> groups;
    List<Integer> users;
    public String datetime;
    public String temperature;
    public String temperaturevalue;
    public String oraltemperature;
    public String systolic;
    public String diastolic;
    public String hands;
    public String bloodpressureposition;
    public String pulse;
    public String pulsepres;
    public String respiratoryrate;
    public String saturation;
    public String satoxygen;
    public String weight;
    public String weightunit;
    public String height;
    public String heightunit;
    public String painpatient;
    public String painpatienty;
  public String patient_cell_phone;
    public List<String> diagnosis;
    public String checkvital;

    public CaseCreated(String case_code, String fname, String lname, String patient_cell_phone, String gender, String y_age, String m_age, String disease_id, String location_id, String history,
                       String question, String city, String form_id, String patient_id, String referer_id, String data, String comment, List<Integer> groups, List<Integer> users,
                       String datetime, String temperature, String temperaturevalue, String oraltemperature, String systolic, String diastolic, String hands, String bloodpressureposition, String pulse,
                       String pulsepres, String respiratoryrate, String saturation, String satoxygen, String weight, String weightunit, String height, String heightunit, String painpatient, String painpatienty,
                       List<String> diagnosis, String checkvital) {
        this.case_code = case_code;
        this.fname = fname;
        this.lname = lname;
     this.patient_cell_phone = patient_cell_phone;
        this.gender = gender;
        this.y_age = y_age;
        this.m_age = m_age;
        this.disease_id = disease_id;
        this.location_id = location_id;
        this.history = history;
        this.question = question;
        this.city = city;
        this.form_id = form_id;
        this.patient_id = patient_id;
        this.referer_id = referer_id;
        this.data = data;
        this.comment = comment;
        this.groups = groups;
        this.users = users;
        this.datetime = datetime;
        this.temperature = temperature;
        this.temperaturevalue = temperaturevalue;
        this.oraltemperature = oraltemperature;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.hands = hands;
        this.bloodpressureposition = bloodpressureposition;
        this.pulse = pulse;
        this.pulsepres = pulsepres;
        this.respiratoryrate = respiratoryrate;
        this.saturation = saturation;
        this.satoxygen = satoxygen;
        this.weight = weight;
        this.weightunit = weightunit;
        this.height = height;
        this.heightunit = heightunit;
        this.painpatient = painpatient;
        this.painpatienty = painpatienty;
        this.diagnosis = diagnosis;
        this.checkvital=checkvital;
    }

}
