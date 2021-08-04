package com.mdcbeta.data.remote.model;

public class  User_Patient  {
    public String id;
    public String username;
    public String name;
    public String firstname;
    public String lastname;
    public String gender;
    public String dobirth;
    public String email;
    public String address;
// added by kawal khan 3/20/20
public String licenses_id;
    public String phone;
  public String phone2;
    public User_Patient() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDobirth() {
        return dobirth;
    }

    public void setDobirth(String dobirth) {
        this.dobirth = dobirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public String getLicenses_id() {
        return licenses_id;
    }

    public void setLicenses_id(String licenses_id) {
        this.licenses_id = licenses_id;
    }

    public User_Patient(String licenses_id, String username, String name, String firstname, String lastname, String phone, String gender, String dobirth, String email, String phone2) {
        this.licenses_id = licenses_id;
        this.username = username;
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.gender = gender;
        this.dobirth = dobirth;
        this.email = email;
      this.phone2 = phone2;

    }
}

