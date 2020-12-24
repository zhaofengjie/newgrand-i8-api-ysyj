package com.newgrand.domain.LM;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveResultModel {

    @JSONField(name = "KeyCodes")
    private JSONArray keyCodes = new JSONArray();

    @JSONField(name = "SaveRows")
    private int saveRows;

    @JSONField(name = "AttachMsg")
    private String attachMsg = "";

    @JSONField(name = "ResBillNoOrIdEntity")
    private String resBillNoOrIdEntity = null;

    @JSONField(name = "IsError")
    private boolean errorIs;

    @JSONField(name = "ErrorCode")
    private String errorCode = "";

    @JSONField(name = "Msg")
    private String msg;

    @JSONField(name = "Status")
    private String status;

    @JSONField(name = "Data")
    private Object data = "";
}
