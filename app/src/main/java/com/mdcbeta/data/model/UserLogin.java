package com.mdcbeta.data.model;

/**
 * Created by MahaRani on 05/12/2017.
 */

public class UserLogin {

    int id;
    String name;
    String password;
    // Constructor with two parameters name and password
    public UserLogin(String name,String password)
    {
        this.name=name;
        this.password=password;
    }
    //Parameter constructor containing all three parameters
    public UserLogin(int id,String name,String psd)
    {
        this.id=id;
        this.name=name;
        this.password=psd;
    }
    //getting id
    public int getId() {
        return id;
    }
    //setting id
    public void setId(int id) {
        this.id = id;
    }
    //getting name
    public String getName() {
        return name;
    }
    //setting name
    public void setName(String name) {
        this.name = name;
    }
    //getting password
    public String getPassword() {
        return password;
    }
    //setting password
    public void setPassword(String password) {
        this.password = password;
    }
}
