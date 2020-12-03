package com.newgrand.secdev.domain.AM;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ResultModel {

    public ResultModel(String result,String code,String message)
    {
        this.result=result;
        this.code=code;
        this.message=message;
    }
    /**
     * （成功为true 失败为false）
     */
    private String result;
    /**
     * 消息
     */
    private String code;
    /**
     * 消息(保存成功/或失败)
     */
    private String message;

    public ResultModel() {

    }
}
