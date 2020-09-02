package com.newgrand.secdev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
