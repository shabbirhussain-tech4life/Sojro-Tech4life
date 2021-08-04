package com.mdcbeta.data;

public class Pulse {

       public int PULSE_RATE_INVALID = 255;


    private int pulseRate;
    private int status;

    public Pulse(int pulseRate, int status) {

           this.pulseRate = pulseRate;
        this.status = status;
    }


    public int getPulseRate()
    {
        return pulseRate;
    }


    @Override
    public String toString() {
        return "  Pulse Rate:"+(pulseRate!=PULSE_RATE_INVALID ? pulseRate: "- -");
    }
}
