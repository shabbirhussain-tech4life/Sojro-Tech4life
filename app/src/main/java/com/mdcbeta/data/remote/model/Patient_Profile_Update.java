package com.mdcbeta.data.remote.model;
// created by kanwal khan 7/27/2020
public class Patient_Profile_Update  {
  public String user_id;

  public String name;
  public String username;
  public String firstname;
  public String lastname;
  public String gender;
  public String email;
  public String phone;
  public String phone2;
  public Patient_Profile_Update() {
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getUsertname() {
    return username;
  }

  public void setUsername(String username) {
    this.username = firstname;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }


  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }




  public String getId() {
    return user_id;
  }

  public void setId(String id) {
    this.user_id = user_id;
  }



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public Patient_Profile_Update(String user_id,String name,String username,
                                String firstname,String lastname,String phone, String gender, String email,String phone2) {
this.user_id=user_id;

    this.name = name;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.phone = phone;
    this.gender = gender;

    this.email = email;
    this.phone2 = phone2;
  }
}
