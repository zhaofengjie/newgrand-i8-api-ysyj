package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WebRv implements Serializable {
    private boolean ok;
    @JsonProperty("success")
    public boolean getOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    private  String msg;
    public  String getMsg(){
        return msg;
    }
    public  void  setMsg(String msg){
        this.msg=msg;
    }

    private  Object data;
    public  Object getData(){
        return data;
    }
    public  void  setData(Object data){
        this.data=data;
    }
}
