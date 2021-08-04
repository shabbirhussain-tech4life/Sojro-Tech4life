package com.mdcbeta.data.model;


/**
 * Created by Shakil Karim on 4/9/17.
 */

public class User{

    public String id;
    public String username;
    public String speciality;
    public String name;
    public String firstname;
    public String lastname;
    public String gender;
    public String age;
    public String dobirth;
    public String email;
    public String country;
    public String state;
    public String city;
    public String address;
    public String affiliation;
    public String degree;
    public String bio;
    public String remember_token;
    public String image;
    public String license_owner;
    public String status;
    public String switch_role;
    public String created_at;
    public String updated_at;
    public String password;
    public String licenses_id;
    public String role_id;
// added by kanwal
public String code;
    public String is_admin;
    public User() {
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }


    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLicenses_id() {
        return licenses_id;
    }

    public void setLicenses_id(String licenses_id) {
        this.licenses_id = licenses_id;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLicense_owner() {
        return license_owner;
    }

    public void setLicense_owner(String license_owner) {
        this.license_owner = license_owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSwitch_role() {
        return switch_role;
    }

    public void setSwitch_role(String switch_role) {
        this.switch_role = switch_role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }



    public User(String id, String role_id, String code,String username, String speciality, String name, String gender, String email, String country, String state, String city, String affiliation, String degree, String remember_token, String image, String license_owner, String status, String switch_role) {
        this.id = id;

        this.role_id=role_id;
        this.code=code;
        this.username = username;
        this.speciality = speciality;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.country = country;
        this.state = state;
        this.city = city;
        this.affiliation = affiliation;
        this.degree = degree;
        this.remember_token = remember_token;
        this.image = image;
        this.license_owner = license_owner;
        this.status = status;
        this.switch_role = switch_role;
    }
}
