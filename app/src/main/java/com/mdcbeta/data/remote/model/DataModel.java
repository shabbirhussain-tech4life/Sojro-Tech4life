package com.mdcbeta.data.remote.model;

/**
 * Created by MahaRani on 05/12/2017.
 */

public class DataModel {

    public String name;
    public String county;
    public String city;


    public String referrer;
    public String patientid;
    public String firstname;
    public String lastname;
    public String gender;
    public String ageinyear;
    public String ageinmonth;
    public String location;
    public String complains;
    public String history;
    public String comments;
    public String origindate;
    public String imageurl;
    public String username;
    public String password;
    public String id;
    public int userid;

    public String commented_name;
    public String comment;
    public String created_date;

  /*  public DataModel(String username,String password)
    {
        this.username=username;
        this.password=password;
    }*/

    public int getUserid() {
        return userid;
    }
    //setting id
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeinyear() {
        return ageinyear;
    }

    public void setAgeinyear(String ageinyear) {
        this.ageinyear = ageinyear;
    }

    public String getAgeinmonth() {
        return ageinmonth;
    }

    public void setAgeinmonth(String ageinmonth) {
        this.ageinmonth = ageinmonth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComplains() {
        return complains;
    }

    public void setComplains(String complains) {
        this.complains = complains;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOrigindate() {
        return origindate;
    }

    public void setOrigindate(String origindate) {
        this.origindate = origindate;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommented_name() {
        return commented_name;
    }

    public void setCommented_name(String commented_name) {
        this.commented_name = commented_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

}
