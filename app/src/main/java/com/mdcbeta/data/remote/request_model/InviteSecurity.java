package com.mdcbeta.data.remote.request_model;

public class InviteSecurity {


    public String id;
    public String code;

    public String email;




    public InviteSecurity() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }







    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }






    public InviteSecurity(String id, String code,
                   String email) {
        this.id = id;
        this.code = code;

        this.email = email;


    }





}
