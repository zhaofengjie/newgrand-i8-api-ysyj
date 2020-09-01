package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DATA {
    @JsonProperty("DATAINFOS")
    private Object datainfos;
}
