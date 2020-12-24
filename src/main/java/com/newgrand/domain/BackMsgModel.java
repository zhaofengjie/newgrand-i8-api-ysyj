package com.newgrand.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BackMsgModel {

    /**
     * （成功为true 失败为false）
     */
    private String result;

    /**
     * 消息(保存成功/或失败)
     */
    private String message;

    /**
     * 任意类型数据
     * */
    private Object data;
}
