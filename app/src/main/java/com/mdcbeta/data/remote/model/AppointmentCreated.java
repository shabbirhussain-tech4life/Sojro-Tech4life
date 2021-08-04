//package com.mdcbeta.data.remote.request_model;
package com.mdcbeta.data.remote.model;

import java.util.List;

public class AppointmentCreated {
    public String case_code;
    public String fname;
    public String lname;
  public String phone;
  public String phone2;
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
  public String Mtemperature;
  public String Mtemperaturevalue;
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
    public String date;
    public String slot_id;
    public String booked_as;
    public String unique_key;
    public String doctor_id;
  public String checkvital;
  public String videos;
//public String manualTemp_txt_temperature;

    public AppointmentCreated(String case_code, String fname, String lname,String phone, String phone2,String gender, String y_age, String m_age, String disease_id, String location_id, String history,
                              String question, String city, String form_id, String patient_id, String referer_id, String data, String comment, List<Integer> groups, List<Integer> users,
                              String datetime, String temperature, String temperaturevalue, String Mtemperature, String Mtemperaturevalue,
                              String oraltemperature, String systolic, String diastolic, String hands, String bloodpressureposition, String pulse,
                              String pulsepres, String respiratoryrate, String saturation, String satoxygen, String weight, String weightunit, String height, String heightunit, String painpatient, String painpatienty,
                              String date, String slot_id, String booked_as,String unique_key,String doctor_id,String checkvital,String videos) {
        this.case_code = case_code;
        this.fname = fname;
        this.lname = lname;
      this.phone = phone;
      this.phone2 = phone2;
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
      this.Mtemperature = Mtemperature;
      this.Mtemperaturevalue =Mtemperaturevalue;
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
        this.date = date;
        this.slot_id = slot_id;
        this.booked_as = booked_as;
        this.unique_key = unique_key;
        this.doctor_id = doctor_id;
      this.checkvital = checkvital;
//      this.manualTemp_txt_temperature = manualTemp_txt_temperature;
      this.videos = videos;

    }

}

