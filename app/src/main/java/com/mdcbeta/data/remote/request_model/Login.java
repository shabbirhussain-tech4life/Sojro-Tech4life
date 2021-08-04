package com.mdcbeta.data.remote.request_model;

/**
 * Created by Shakil Karim on 4/11/17.
 */

public class Login {

    public String username;
    public String password;
 public String switch_role;


  public Login(String username, String password, String switch_role) {
    this.username = username;
    this.password = password;
    this.switch_role=switch_role;
  }
}
