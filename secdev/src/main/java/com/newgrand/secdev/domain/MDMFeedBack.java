package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MDMFeedBack implements Serializable {

    @JsonProperty("ESB")
    private Object esb;

    public Object getEsb() {
        return esb;
    }

    public void setEsb(Object esb) {
        this.esb = esb;
    }
}
