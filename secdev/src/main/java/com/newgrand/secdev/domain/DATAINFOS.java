package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DATAINFOS {
    @JsonProperty("UUID")
    private String uuid;

    @JsonProperty("DATAINFO")
    private Object datainfo;
}
