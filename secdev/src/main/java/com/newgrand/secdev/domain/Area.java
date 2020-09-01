package com.newgrand.secdev.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Area {
    private Integer areaId;
    private String areaName;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
}
