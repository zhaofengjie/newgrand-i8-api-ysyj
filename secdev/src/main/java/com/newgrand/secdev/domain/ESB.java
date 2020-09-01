package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ESB {
    /**
     * S(成功为S或者0，失败为E或者1)
     */
    @JsonProperty("CODE")
    private String code;
    /**
     * 成功反馈'接收成功'，失败时反馈错误描述
     */
    @JsonProperty("DESC")
    private String desc;
    /**
     * ESB DATA 数据
     */
    @JsonProperty("DATA")
    private Object data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
