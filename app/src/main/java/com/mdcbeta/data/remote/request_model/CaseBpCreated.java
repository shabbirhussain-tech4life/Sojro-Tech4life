package com.mdcbeta.data.remote.request_model;

/**
 * Created by MahaRani on 02/02/2018.
 */

public class CaseBpCreated {

    public String user_id;
    public String bpdate;
    public String bptime;
    public String systolic;
    public String diastolic;
    public String bpcomment;


    public CaseBpCreated(String user_id, String bpdate, String bptime, String systolic, String diastolic, String bpcomment) {
        this.user_id = user_id;
        this.bpdate = bpdate;
        this.bptime = bptime;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.bpcomment = bpcomment;

    }
}
