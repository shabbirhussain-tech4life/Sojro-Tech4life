package com.mdcbeta.data;

public class NIBP {

    public int HIGH_PRESSURE_INVALID = 0;
    public int LOW_PRESSURE_INVALID  = 0;

    private int highPressure;
    private int meanPressure;
    private int lowPressure;
    private int cuffPressure;
    private int status;

    public NIBP(int highPressure, int meanPressure, int lowPressure, int cuffPressure, int status) {
        this.highPressure = highPressure;
        this.meanPressure = meanPressure;
        this.lowPressure = lowPressure;
        this.cuffPressure = cuffPressure;
        this.status = status;
    }

    public int getMeanPressure() {
        return meanPressure;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public int getCuffPressure() {
        return cuffPressure;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return   (cuffPressure!=0 ? cuffPressure: "- -") + "\r\n" +
                ":"+  (highPressure!=0 ? highPressure: "- -") +
                ":" +  (lowPressure !=0 ? lowPressure : "- -") +
                ":"+  (meanPressure!=0 ? meanPressure: "- -");
    }
}

