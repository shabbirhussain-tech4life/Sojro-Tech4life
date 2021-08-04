package com.mdcbeta.data;

public class SpO2 {

    public int SPO2_INVALID = 127;
    public int PULSE_RATE_INVALID = 255;

    private int SpO2;
    private int pulseRate;
    private int status;

    public SpO2(int spO2, int pulseRate, int status) {
        SpO2 = spO2;
        this.pulseRate = pulseRate;
        this.status = status;
    }

    public int getSpO2() {
        return SpO2;
    }

    public void setSpO2(int spO2) {
        SpO2 = spO2;
    }

    public int getPulseRate() {
        return pulseRate;
    }


    @Override
    public String toString() {
        return (SpO2!=SPO2_INVALID ? SpO2: "- -") +
                ": "+(pulseRate!=PULSE_RATE_INVALID ? pulseRate: "- -");
    }
//    public String toString() {
//        return (SpO2!=SPO2_INVALID ? SpO2: "- -") + ":" ;
//
//    }
}