package com.mdcbeta.data.remote.model;

// by kk 3/12/2020
public class InvitedByAdmin {
    public String id;
    public String code;
    public String license_purchase_id;
    public String email;
    public String invited_by;



    public InvitedByAdmin() {
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

    public String getLicense_purchase_id() {
        return license_purchase_id;
    }

    public void setSpeciality(String license_purchase_id) {
        this.license_purchase_id = license_purchase_id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getInvited_by() {
        return invited_by;
    }

    public void setInvited_by(String invited_by) {
        this.invited_by = invited_by;
    }




    public InvitedByAdmin(String id, String code, String license_purchase_id,
                          String email, String invited_by) {
        this.id = id;
        this.code = code;
        this.license_purchase_id = license_purchase_id;
        this.email = email;
        this.invited_by = invited_by;

    }
}

