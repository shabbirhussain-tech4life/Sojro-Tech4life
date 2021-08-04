package com.mdcbeta.data.model;

import java.util.List;

public class Diagnosis {
    List<String> diagnosis;
    String datetime;

    public Diagnosis(List<String> diagnosis, String datetime) {
        this.diagnosis = diagnosis;
        this.datetime = datetime;
    }
}
