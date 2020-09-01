package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DATAINFOS {

    @JsonProperty("UUID")
    private String uuid;

    @JsonProperty("DATAINFO")
    private Object datainfo;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getDatainfo() {
        return datainfo;
    }

    public void setDatainfo(Object datainfo) {
        this.datainfo = datainfo;
    }
}
